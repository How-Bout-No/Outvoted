package io.github.how_bout_no.outvoted.entity;

import com.google.common.collect.Lists;
import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Meerkat extends Animal implements IAnimatable {
    private static final Ingredient TAMING_INGREDIENT;
    private static final EntityDataAccessor<Boolean> TRUSTING;
    private static final EntityDataAccessor<Optional<UUID>> TRUSTED_UUID;
    private BlockPos structurepos = null;
    private int animtimer = 0;
    private int cannotEnterBurrowTicks;
    private int ticksLeftToFindBurrow = 0;
    @Nullable
    private BlockPos burrowPos = null;
    private MoveToBurrowGoal moveToBurrowGoal;
    private TemptGoal temptGoal;

    public Meerkat(EntityType<? extends Meerkat> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F); // no like da water
    }

    protected void registerGoals() {
        this.temptGoal = new TemptGoal(this, 0.6D, TAMING_INGREDIENT, false);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, this.temptGoal);
        this.goalSelector.addGoal(3, new VentureGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new EnterBurrowGoal());
        this.goalSelector.addGoal(5, new AttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FindBurrowGoal());
        this.moveToBurrowGoal = new MoveToBurrowGoal();
        this.goalSelector.addGoal(7, this.moveToBurrowGoal);
        this.goalSelector.addGoal(8, new BreedGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Player.class, Meerkat.class)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false, e -> e.getMobType() == MobType.ARTHROPOD));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        HealthUtil.setConfigHealth(this, Config.meerkatHealth.get());

        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    static {
        TRUSTING = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.BOOLEAN);
        TRUSTED_UUID = SynchedEntityData.defineId(Meerkat.class, EntityDataSerializers.OPTIONAL_UUID);
        TAMING_INGREDIENT = Ingredient.of(Items.SPIDER_EYE);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TRUSTING, false);
        this.entityData.define(TRUSTED_UUID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.hasStructurePos()) tag.put("StructPos", NbtUtils.writeBlockPos(this.getStructurePos()));
        tag.putBoolean("Trusting", this.isTrusting());
        if (this.hasTrusted()) tag.putUUID("Trusted", getTrusted());
        if (this.hasBurrow()) {
            tag.put("BurrowPos", NbtUtils.writeBlockPos(this.getBurrowPos()));
        }
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        this.structurepos = null;
        if (tag.contains("StructPos")) this.structurepos = NbtUtils.readBlockPos(tag.getCompound("StructPos"));

        this.burrowPos = null;
        if (tag.contains("BurrowPos")) {
            this.burrowPos = NbtUtils.readBlockPos(tag.getCompound("BurrowPos"));
        }

        super.readAdditionalSaveData(tag);
        this.setTrusting(tag.getBoolean("Trusting"));
        if (tag.contains("Trusted")) this.setTrusted(tag.getUUID("Trusted"));
    }

    private boolean isTrusting() {
        return this.entityData.get(TRUSTING);
    }

    private void setTrusting(boolean trusting) {
        this.entityData.set(TRUSTING, trusting);
    }

    private boolean hasTrusted() {
        return getTrusted() != null;
    }

    @Nullable
    private UUID getTrusted() {
        return (UUID) ((Optional) this.entityData.get(TRUSTED_UUID)).orElse(null);
    }

    private void setTrusted(@Nullable UUID trusted) {
        this.entityData.set(TRUSTED_UUID, Optional.ofNullable(trusted));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MEERKAT_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.MEERKAT_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.MEERKAT_HURT.get();
    }

    public boolean isFood(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    private boolean doesBurrowHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (blockEntity instanceof BurrowBlockEntity) {
            Direction dir = this.level.getBlockState(pos).getValue(DirectionalBlock.FACING);
            return !((BurrowBlockEntity) blockEntity).isFull() && this.level.getBlockState(pos.relative(dir)).isAir();
        } else {
            return false;
        }
    }

    public boolean hasBurrow() {
        return this.burrowPos != null;
    }

    @Nullable
    public BlockPos getBurrowPos() {
        return this.burrowPos;
    }

    @Nullable
    public BlockPos getBurrowOffsetPos() {
        Meerkat mob = Meerkat.this;
        Level world = mob.level;
        BlockPos burrow = mob.getBurrowPos();
        return burrow.relative(world.getBlockState(burrow).getValue(DirectionalBlock.FACING));
    }

    private boolean isBurrowValid() {
        if (!this.hasBurrow()) {
            return false;
        } else {
            BlockEntity blockEntity = this.level.getBlockEntity(this.burrowPos);
            return blockEntity != null && blockEntity.getType() == ModEntities.BURROW.get();
        }
    }

    private boolean hasStructurePos() {
        return this.structurepos != null;
    }

    @Nullable
    private BlockPos getStructurePos() {
        return this.structurepos;
    }

    private BlockPos findStructure() {
        if (!this.level.isClientSide) {
            BlockPos blockPos = new BlockPos(this.blockPosition());
            if (this.getServer().overworld() != null) {
                ServerLevel serverLevel = this.getServer().overworld();
                return serverLevel.findNearestMapFeature(ModTags.IN_DESERT, blockPos, 100, false);
            }
        }
        return null;
    }

    public static boolean canSpawn(EntityType<Meerkat> entity, LevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        return world.getRawBrightness(blockPos, 0) > 8 && checkMobSpawnRules(entity, world, spawnReason, blockPos, random) && world.getBlockState(blockPos.below()).is(Blocks.SAND);
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.cannotEnterBurrowTicks > 0) {
                --this.cannotEnterBurrowTicks;
            }

            if (this.ticksLeftToFindBurrow > 0) {
                --this.ticksLeftToFindBurrow;
            }

            if (this.tickCount % 20 == 0 && !this.isBurrowValid()) {
                this.burrowPos = null;
            }
        }

    }

    private void showEmoteParticle(boolean positive) {
        ParticleOptions particleEffect = ParticleTypes.HEART;
        if (!positive) {
            particleEffect = ParticleTypes.SMOKE;
        }

        for (int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02D;
            double e = this.random.nextGaussian() * 0.02D;
            double f = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(particleEffect, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d, e, f);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        MobEffect statusEffect = effect.getEffect();
        if (statusEffect == MobEffects.POISON) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if ((this.temptGoal == null || this.temptGoal.isRunning()) && !this.isTrusting() && this.isFood(itemStack) && player.distanceToSqr(this) < 9.0D) {
            this.usePlayerItem(player, hand, itemStack);
            if (!this.level.isClientSide) {
                if (this.random.nextInt(3) == 0) {
                    this.setTrusting(true);
                    this.showEmoteParticle(true);
                    this.level.broadcastEntityEvent(this, (byte) 41);
                } else {
                    this.showEmoteParticle(false);
                    this.level.broadcastEntityEvent(this, (byte) 40);
                }
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (this.isTrusting() && player.distanceToSqr(this) < 9.0D) {
            if (itemStack.getItem() == Items.STICK) {
                if (!this.level.isClientSide) {
                    structurepos = findStructure();
                    setTrusted(player.getUUID());
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte status) {
        if (status == 41) {
            this.showEmoteParticle(true);
        } else if (status == 40) {
            this.showEmoteParticle(false);
        } else {
            super.handleEntityEvent(status);
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (0.5F * this.getEyeHeight()), (this.getBbWidth() * 0.05F));
    }

    private static float getDistance(int a, int b, int x, int y) {
        int i = x - a;
        int j = y - b;
        return Mth.sqrt((float) (i * i + j * j));
    }

    static class VentureGoal extends Goal {
        protected final Meerkat mob;
        public final double speed;
        protected BlockPos targetPos;
        private boolean reached = false;

        public VentureGoal(Meerkat mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public boolean canUse() {
            return this.mob.isTrusting() && this.mob.getStructurePos() != null;
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.reached;
        }

        public void stop() {
            this.mob.structurepos = null;
            this.mob.setTrusted(null);
        }

        public void start() {
            this.targetPos = this.mob.level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, this.mob.getStructurePos());
            this.startMovingToTarget();
        }

        protected void startMovingToTarget() {
            this.mob.getNavigation().moveTo((double) ((float) this.targetPos.getX()) + 0.5D, this.targetPos.getY(), (double) ((float) this.targetPos.getZ()) + 0.5D, this.speed);
        }

        public void tick() {
            if (this.mob.hasTrusted()) {
                Player trusted = this.mob.level.getPlayerByUUID(this.mob.getTrusted());
                if (this.mob.distanceToSqr(trusted) > 64) {
                    this.mob.getNavigation().stop();
                    this.mob.getLookControl().setLookAt(trusted.getX(), trusted.getEyeY(), trusted.getZ());
                } else if (this.mob.getNavigation().isDone() || this.mob.getNavigation().getPath() == null) {
                    this.startMovingToTarget();
                }
                if (this.mob.distanceToSqr(this.mob.getStructurePos().getX(), this.mob.getY(), this.mob.getStructurePos().getZ()) <= 100)
                    this.reached = true;
            }
        }
    }

    private boolean canEnterBurrow() {
        return this.getTarget() == null;
    }

    class EnterBurrowGoal extends Goal {
        public boolean canUse() {
            if (Meerkat.this.hasBurrow() && Meerkat.this.canEnterBurrow()) {
                BlockEntity blockEntity = Meerkat.this.level.getBlockEntity(Meerkat.this.burrowPos);
                if (blockEntity instanceof BurrowBlockEntity && Meerkat.this.getBurrowOffsetPos().closerToCenterThan(Meerkat.this.position(), 1.0D)) {
                    BurrowBlockEntity burrowBlockEntity = (BurrowBlockEntity) blockEntity;
                    if (!burrowBlockEntity.isFull()) {
                        return true;
                    }

                    Meerkat.this.burrowPos = null;
                }
            }

            return false;
        }

        public void start() {
            Level world = Meerkat.this.level;
            BlockEntity blockEntity = world.getBlockEntity(Meerkat.this.burrowPos);
            BlockPos pos = Meerkat.this.getBurrowOffsetPos();
            boolean bl = Meerkat.this.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 1.0;
            if (blockEntity instanceof BurrowBlockEntity && bl) {
                BurrowBlockEntity burrowBlockEntity = (BurrowBlockEntity) blockEntity;
                burrowBlockEntity.tryEnterBurrow(Meerkat.this, false);
            }
        }
    }

    class FindBurrowGoal extends Goal {
        public boolean canUse() {
            return Meerkat.this.ticksLeftToFindBurrow == 0 && !Meerkat.this.hasBurrow() && Meerkat.this.canEnterBurrow();
        }

        public void start() {
            Meerkat.this.ticksLeftToFindBurrow = 200;
            List<BlockPos> list = this.getNearbyFreeBurrows();
            if (!list.isEmpty()) {
                Iterator<BlockPos> var2 = list.iterator();

                BlockPos blockPos;
                do {
                    if (!var2.hasNext()) {
                        Meerkat.this.moveToBurrowGoal.clearPossibleBurrows();
                        Meerkat.this.burrowPos = list.get(0);
                        return;
                    }

                    blockPos = var2.next();
                } while (Meerkat.this.moveToBurrowGoal.isPossibleBurrow(blockPos));

                Meerkat.this.burrowPos = blockPos;
            }
        }

        private List<BlockPos> getNearbyFreeBurrows() {
            BlockPos blockPos = Meerkat.this.blockPosition();
            PoiManager pointOfInterestStorage = ((ServerLevel) Meerkat.this.level).getPoiManager();
            Stream<PoiRecord> stream = pointOfInterestStorage.getInRange(
                    (pointOfInterestType) -> pointOfInterestType == ModPOI.BURROW.get(), blockPos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(Meerkat.this::doesBurrowHaveSpace).sorted(Comparator.comparingDouble(
                    (blockPos2) -> blockPos2.distSqr(blockPos))).collect(Collectors.toList());
        }
    }

    private boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), distance);
    }

    private boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    private void startMovingTo(BlockPos pos) {
        Vec3 vec3d = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int) vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3 vec3d2 = AirRandomPos.getPosTowards(this, k, l, i, vec3d, 0.3141592741012573D);
        if (vec3d2 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec3d2.x + 0.5, vec3d2.y, vec3d2.z + 0.5, 1.0D);
        }
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(PathfinderMob mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.mob.getHealth() > 3.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.mob.getHealth() > 3.0D;
        }
    }

    class MoveToBurrowGoal extends Goal {
        private int ticks;
        private List<BlockPos> possibleBurrows;
        @Nullable
        private Path path;
        private int ticksUntilLost;

        MoveToBurrowGoal() {
            super();
            this.ticks = Meerkat.this.level.random.nextInt(10);
            this.possibleBurrows = Lists.newArrayList();
            this.path = null;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            Meerkat mob = Meerkat.this;
            Level world = mob.level;
            BlockPos burrow = mob.getBurrowPos();
            return mob.hasBurrow() &&
                    !mob.hasRestriction() &&
                    mob.canEnterBurrow() &&
                    !this.isCloseEnough(burrow) &&
                    world.getBlockState(burrow).is(ModBlocks.BURROW.get()) &&
                    world.getBlockState(mob.getBurrowOffsetPos()).isAir();
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }

        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            Meerkat.this.navigation.stop();
            Meerkat.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (Meerkat.this.hasBurrow()) {
                BlockPos blockPos = Meerkat.this.getBurrowOffsetPos();
                ++this.ticks;
                if (this.ticks > 600) {
                    this.makeChosenBurrowPossibleBurrow();
                } else if (!Meerkat.this.navigation.isInProgress()) {
                    if (!Meerkat.this.isWithinDistance(blockPos, 16)) {
                        if (Meerkat.this.isTooFar(blockPos)) {
                            this.setLost();
                        } else {
                            Meerkat.this.startMovingTo(blockPos);
                        }
                    } else {
                        boolean bl = this.startMovingToFar(blockPos);
                        if (!bl) {
                            this.makeChosenBurrowPossibleBurrow();
                        } else if (this.path != null && Meerkat.this.navigation.getPath().sameAs(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = Meerkat.this.navigation.getPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            Meerkat.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
            Meerkat.this.navigation.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.0D);
            return Meerkat.this.navigation.getPath() != null && Meerkat.this.navigation.getPath().canReach();
        }

        private boolean isPossibleBurrow(BlockPos pos) {
            return this.possibleBurrows.contains(pos);
        }

        private void addPossibleBurrow(BlockPos pos) {
            this.possibleBurrows.add(pos);

            while (this.possibleBurrows.size() > 3) {
                this.possibleBurrows.remove(0);
            }

        }

        private void clearPossibleBurrows() {
            this.possibleBurrows.clear();
        }

        private void makeChosenBurrowPossibleBurrow() {
            if (Meerkat.this.hasBurrow()) {
                this.addPossibleBurrow(Meerkat.this.burrowPos);
            }

            this.setLost();
        }

        private void setLost() {
            Meerkat.this.burrowPos = null;
            Meerkat.this.ticksLeftToFindBurrow = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            if (pos.closerThan(Meerkat.this.blockPosition(), 1.0D)) {
                return true;
            } else {
                Path path = Meerkat.this.navigation.getPath();
                return path != null && path.getTarget().equals(pos) && path.canReach() && path.isDone();
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float g = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            if (((LivingEntity) target).getMobType() == MobType.ARTHROPOD) {
                f += 5.0F;
            }
        }

        boolean bl = target.hurt(DamageSource.mobAttack(this), f);
        if (bl) {
            if (g > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).knockback(g * 0.5F, Mth.sin(this.getYRot() * 0.017453292F), -Mth.cos(this.getYRot() * 0.017453292F));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            this.doEnchantDamageEffects(this, target);
            this.setLastHurtMob(target);
        }

        return bl;
    }

    @Nullable
    @Override
    public Meerkat getBreedOffspring(ServerLevel world, AgeableMob entity) {
        Meerkat meerkat = ModEntities.MEERKAT.get().create(world);
        meerkat.finalizeSpawn(world, world.getCurrentDifficultyAt(meerkat.blockPosition()), MobSpawnType.BREEDING, null, null);
        return meerkat;
    }

    @Override
    public float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        if (pose == Pose.CROUCHING) {
            return 0.5F;
        }
        return super.getStandingEyeHeight(pose, dimensions);
    }

    private AABB calcBox() {
        EntityDimensions entityDimensions = this.getDimensions(Pose.STANDING);
        boolean bl = this.getPose() == Pose.STANDING;
        double f = (double) entityDimensions.width / (bl ? 4.0F : 2.0F);
        double height = (double) entityDimensions.height / (bl ? 1.0F : 2.0F);
        Vec3 vec3d = new Vec3(this.getX() - f, this.getY(), this.getZ() - f);
        Vec3 vec3d2 = new Vec3(this.getX() + f, this.getY() + height, this.getZ() + f);
        return new AABB(vec3d, vec3d2);
    }

    @Override
    public void setPose(Pose pose) {
        if (pose != this.getPose()) {
            super.setPose(pose);
            this.setBoundingBox(calcBox());
        }
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.getController().getAnimationState().equals(AnimationState.Stopped) || (animtimer == 10 && !this.isInWaterOrBubble() && !event.isMoving())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("stand"));
            this.setPose(Pose.STANDING);
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk"));
            this.setPose(Pose.CROUCHING);
            animtimer = 0;
        }
        if (animtimer < 10) animtimer++;
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<Meerkat> controller = new AnimationController<>(this, "controller", 2, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}

