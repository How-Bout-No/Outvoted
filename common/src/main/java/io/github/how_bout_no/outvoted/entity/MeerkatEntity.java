package io.github.how_bout_no.outvoted.entity;

import com.google.common.collect.Lists;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import io.github.how_bout_no.outvoted.entity.util.EntityUtils;
import io.github.how_bout_no.outvoted.init.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
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

public class MeerkatEntity extends AnimalEntity implements IAnimatable {
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(Items.SPIDER_EYE);
    private static final TrackedData<Boolean> TRUSTING;
    private static final TrackedData<Optional<UUID>> TRUSTED_UUID;
    private BlockPos structurepos = null;
    private int animtimer = 0;
    private int cannotEnterBurrowTicks;
    private int ticksLeftToFindBurrow = 0;
    @Nullable
    private BlockPos burrowPos = null;
    private MeerkatEntity.MoveToBurrowGoal moveToBurrowGoal;

    public MeerkatEntity(EntityType<? extends MeerkatEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F); // no like da water
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeerkatEntity.VentureGoal(this, 1.25D));
        this.goalSelector.add(2, new MeerkatEntity.EnterBurrowGoal());
        this.goalSelector.add(3, new AnimalMateGoal(this, 0.8D));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(6, new MeerkatEntity.FindBurrowGoal());
        this.moveToBurrowGoal = new MeerkatEntity.MoveToBurrowGoal();
        this.goalSelector.add(7, this.moveToBurrowGoal);
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this, PlayerEntity.class, MeerkatEntity.class)));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, HostileEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

    @Nullable
    public net.minecraft.entity.EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, @Nullable net.minecraft.entity.EntityData spawnDataIn, @Nullable CompoundTag dataTag) {
        EntityUtils.setConfigHealth(this, Outvoted.commonConfig.entities.meerkat.health);

        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    static {
        TRUSTING = DataTracker.registerData(MeerkatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        TRUSTED_UUID = DataTracker.registerData(MeerkatEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TRUSTING, false);
        this.dataTracker.startTracking(TRUSTED_UUID, Optional.empty());
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        if (this.hasStructurePos()) tag.put("StructPos", NbtHelper.fromBlockPos(this.getStructurePos()));
        tag.putBoolean("Trusting", this.isTrusting());
        if (this.hasTrusted()) tag.putUuid("Trusted", getTrusted());
        if (this.hasBurrow()) {
            tag.put("BurrowPos", NbtHelper.fromBlockPos(this.getBurrowPos()));
        }
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        this.structurepos = null;
        if (tag.contains("StructPos")) this.structurepos = NbtHelper.toBlockPos(tag.getCompound("StructPos"));

        this.burrowPos = null;
        if (tag.contains("BurrowPos")) {
            this.burrowPos = NbtHelper.toBlockPos(tag.getCompound("BurrowPos"));
        }

        super.readCustomDataFromTag(tag);
        this.setTrusting(tag.getBoolean("Trusting"));
        if (tag.contains("Trusted")) this.setTrusted(tag.getUuid("Trusted"));
    }

    private boolean isTrusting() {
        return this.dataTracker.get(TRUSTING);
    }

    private void setTrusting(boolean trusting) {
        this.dataTracker.set(TRUSTING, trusting);
    }

    private boolean hasTrusted() {
        return getTrusted() != null;
    }

    @Nullable
    private UUID getTrusted() {
        return (UUID) ((Optional) this.dataTracker.get(TRUSTED_UUID)).orElse((Object) null);
    }

    private void setTrusted(@Nullable UUID trusted) {
        this.dataTracker.set(TRUSTED_UUID, Optional.ofNullable(trusted));
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

    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    private boolean doesBurrowHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.world.getBlockEntity(pos);
        if (blockEntity instanceof BurrowBlockEntity) {
            Direction dir = this.world.getBlockState(pos).get(FacingBlock.FACING);
            return !((BurrowBlockEntity) blockEntity).isFullOfMeerkats() && this.world.getBlockState(pos.offset(dir)).isAir();
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

    private boolean isBurrowValid() {
        if (!this.hasBurrow()) {
            return false;
        } else {
            BlockEntity blockEntity = this.world.getBlockEntity(this.burrowPos);
            return blockEntity != null && blockEntity.getType() == ModBlockEntityTypes.BURROW.get();
        }
    }

    private boolean hasStructurePos() {
        return this.structurepos != null;
    }

    @Nullable
    private BlockPos getStructurePos() {
        return this.structurepos;
    }

    private BlockPos findStructure(StructureFeature<?> structureFeature) {
        if (!this.world.isClient) {
            BlockPos blockPos = new BlockPos(this.getBlockPos());
            if (this.getServer().getOverworld() != null) {
                ServerWorld serverWorld = this.getServer().getOverworld();
                BlockPos blockPos2 = serverWorld.locateStructure(structureFeature, blockPos, (int) Math.floor(10 * serverWorld.getBiome(blockPos).getScale()), false);
                return blockPos2;
            }
        }
        return null;
    }

    public static boolean canSpawn(EntityType<MeerkatEntity> entity, WorldAccess world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return world.getBaseLightLevel(blockPos, 0) > 8 && canMobSpawn(entity, world, spawnReason, blockPos, random) && world.getBlockState(blockPos.down()).isOf(Blocks.SAND);
    }

    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            if (this.cannotEnterBurrowTicks > 0) {
                --this.cannotEnterBurrowTicks;
            }

            if (this.ticksLeftToFindBurrow > 0) {
                --this.ticksLeftToFindBurrow;
            }

            if (this.age % 20 == 0 && !this.isBurrowValid()) {
                this.burrowPos = null;
            }
        }

    }

    private void showEmoteParticle(boolean positive) {
        ParticleEffect particleEffect = ParticleTypes.HEART;
        if (!positive) {
            particleEffect = ParticleTypes.SMOKE;
        }

        for (int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02D;
            double e = this.random.nextGaussian() * 0.02D;
            double f = this.random.nextGaussian() * 0.02D;
            this.world.addParticle(particleEffect, this.getParticleX(1.0D), this.getRandomBodyY() + 0.5D, this.getParticleZ(1.0D), d, e, f);
        }
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        StatusEffect statusEffect = effect.getEffectType();
        if (statusEffect == StatusEffects.POISON) {
            return false;
        }
        return super.canHaveStatusEffect(effect);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!this.isTrusting() && this.isBreedingItem(itemStack) && player.squaredDistanceTo(this) < 9.0D) {
            this.eat(player, itemStack);
            if (!this.world.isClient) {
                if (this.random.nextInt(3) == 0) {
                    this.setTrusting(true);
                    this.showEmoteParticle(true);
                    this.world.sendEntityStatus(this, (byte) 41);
                } else {
                    this.showEmoteParticle(false);
                    this.world.sendEntityStatus(this, (byte) 40);
                }
            }

            return ActionResult.success(this.world.isClient);
        } else if (this.isTrusting()) {
            if (!this.world.isClient) {
                if (itemStack.getItem() == Items.STICK) {
                    BlockPos pyramidPos = findStructure(StructureFeature.DESERT_PYRAMID);
                    BlockPos treasurePos = findStructure(StructureFeature.BURIED_TREASURE);
                    BlockPos struct = null;
                    if (pyramidPos != null) {
                        struct = pyramidPos;
                    }
                    if (treasurePos != null) {
                        struct = treasurePos;
                    }
                    if (pyramidPos != null && treasurePos != null) {
                        if (getDistance(this.getBlockPos().getX(), this.getBlockPos().getZ(), treasurePos.getX(), treasurePos.getZ()) > getDistance(this.getBlockPos().getX(), this.getBlockPos().getZ(), pyramidPos.getX(), pyramidPos.getZ())) {
                            struct = pyramidPos;
                        }
                    }
                    structurepos = struct;
                    setTrusted(player.getUuid());
                    return ActionResult.CONSUME;
                }
            }
        }
        return super.interactMob(player, hand);
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 41) {
            this.showEmoteParticle(true);
        } else if (status == 40) {
            this.showEmoteParticle(false);
        } else {
            super.handleStatus(status);
        }
    }

    private static float getDistance(int a, int b, int x, int y) {
        int i = x - a;
        int j = y - b;
        return MathHelper.sqrt((float) (i * i + j * j));
    }

    static class VentureGoal extends Goal {
        protected final MeerkatEntity mob;
        public final double speed;
        protected BlockPos targetPos;
        private boolean reached = false;

        public VentureGoal(MeerkatEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.LOOK));
        }

        public boolean canStart() {
            return this.mob.getStructurePos() != null && this.mob.isTrusting();
        }

        public boolean shouldContinue() {
            return super.shouldContinue() && !hasReached();
        }

        public void stop() {
            this.mob.structurepos = null;
            this.mob.setTrusted(null);
        }

        public void start() {
            this.targetPos = this.mob.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, this.mob.getStructurePos());
            this.startMovingToTarget();
        }

        protected void startMovingToTarget() {
            this.mob.getNavigation().startMovingTo((double) ((float) this.targetPos.getX()) + 0.5D, (double) (this.targetPos.getY()), (double) ((float) this.targetPos.getZ()) + 0.5D, this.speed);
        }

        protected boolean hasReached() {
            return this.reached;
        }

        public void tick() {
            if (this.mob.hasTrusted()) {
                PlayerEntity trusted = this.mob.world.getPlayerByUuid(this.mob.getTrusted());
                if (this.mob.squaredDistanceTo(trusted) > 64) {
                    this.mob.getNavigation().stop();
                    this.mob.getLookControl().lookAt(trusted.getX(), trusted.getEyeY(), trusted.getZ());
                } else if (this.mob.getNavigation().isIdle() || this.mob.getNavigation().getCurrentPath() == null) {
                    this.startMovingToTarget();
                }
                if (this.mob.squaredDistanceTo(this.mob.getStructurePos().getX(), this.mob.getY(), this.mob.getStructurePos().getZ()) <= 100)
                    this.reached = true;
            }
        }
    }

    private boolean canEnterBurrow() {
        return this.getTarget() == null;
    }

    class EnterBurrowGoal extends Goal {
        public boolean canStart() {
            if (MeerkatEntity.this.burrowPos != null && MeerkatEntity.this.hasBurrow() && MeerkatEntity.this.canEnterBurrow()) {
                BlockEntity blockEntity = MeerkatEntity.this.world.getBlockEntity(MeerkatEntity.this.burrowPos);
                if (blockEntity instanceof BurrowBlockEntity && MeerkatEntity.this.burrowPos.offset(MeerkatEntity.this.world.getBlockState(MeerkatEntity.this.burrowPos).get(FacingBlock.FACING)).isWithinDistance(MeerkatEntity.this.getPos(), 1.5D)) {
                    BurrowBlockEntity burrowBlockEntity = (BurrowBlockEntity) blockEntity;
                    if (!burrowBlockEntity.isFullOfMeerkats()) {
                        return true;
                    }

                    MeerkatEntity.this.burrowPos = null;
                }
            }

            return false;
        }

        public void start() {
            BlockEntity blockEntity = MeerkatEntity.this.world.getBlockEntity(MeerkatEntity.this.burrowPos);
            if (blockEntity instanceof BurrowBlockEntity) {
                BurrowBlockEntity burrowBlockEntity = (BurrowBlockEntity) blockEntity;
                burrowBlockEntity.tryEnterBurrow(MeerkatEntity.this, false);
            }
        }
    }

    class FindBurrowGoal extends Goal {
        public boolean canStart() {
            return MeerkatEntity.this.ticksLeftToFindBurrow == 0 && !MeerkatEntity.this.hasBurrow() && MeerkatEntity.this.canEnterBurrow();
        }

        public void start() {
            MeerkatEntity.this.ticksLeftToFindBurrow = 200;
            List<BlockPos> list = this.getNearbyFreeBurrows();
            if (!list.isEmpty()) {
                Iterator<BlockPos> var2 = list.iterator();

                BlockPos blockPos;
                do {
                    if (!var2.hasNext()) {
                        MeerkatEntity.this.moveToBurrowGoal.clearPossibleBurrows();
                        MeerkatEntity.this.burrowPos = list.get(0);
                        return;
                    }

                    blockPos = var2.next();
                } while (MeerkatEntity.this.moveToBurrowGoal.isPossibleBurrow(blockPos));

                MeerkatEntity.this.burrowPos = blockPos;
            }
        }

        private List<BlockPos> getNearbyFreeBurrows() {
            BlockPos blockPos = MeerkatEntity.this.getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = ((ServerWorld) MeerkatEntity.this.world).getPointOfInterestStorage();
            Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(
                    (pointOfInterestType) -> pointOfInterestType == ModPOITypes.BURROW.get(), blockPos, 20, PointOfInterestStorage.OccupationStatus.ANY);
            return stream.map(PointOfInterest::getPos).filter(MeerkatEntity.this::doesBurrowHaveSpace).sorted(Comparator.comparingDouble(
                    (blockPos2) -> blockPos2.getSquaredDistance(blockPos))).collect(Collectors.toList());
        }
    }

    private boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), (double) distance);
    }

    private boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    private void startMovingTo(BlockPos pos) {
        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
        int i = 0;
        BlockPos blockPos = this.getBlockPos();
        int j = (int) vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.getManhattanDistance(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3d vec3d2 = TargetFinder.findGroundTargetTowards(this, k, l, i, vec3d, 0.3141592741012573D);
        if (vec3d2 != null) {
            this.navigation.setRangeMultiplier(0.5F);
            this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0D);
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
            this.ticks = MeerkatEntity.this.world.random.nextInt(10);
            this.possibleBurrows = Lists.newArrayList();
            this.path = null;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return MeerkatEntity.this.burrowPos != null &&
                    !MeerkatEntity.this.hasPositionTarget() &&
                    MeerkatEntity.this.canEnterBurrow() &&
                    !this.isCloseEnough(MeerkatEntity.this.burrowPos) &&
                    MeerkatEntity.this.world.getBlockState(MeerkatEntity.this.burrowPos).isOf(ModBlocks.BURROW.get()) &&
                    MeerkatEntity.this.world.getBlockState(MeerkatEntity.this.burrowPos.offset(MeerkatEntity.this.world.getBlockState(MeerkatEntity.this.burrowPos).get(FacingBlock.FACING))).isAir();
        }

        public boolean shouldContinue() {
            return this.canStart();
        }

        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            MeerkatEntity.this.navigation.stop();
            MeerkatEntity.this.navigation.resetRangeMultiplier();
        }

        public void tick() {
            if (MeerkatEntity.this.burrowPos != null) {
                BlockPos blockPos = MeerkatEntity.this.burrowPos.offset(world.getBlockState(MeerkatEntity.this.burrowPos).get(FacingBlock.FACING));
                ++this.ticks;
                if (this.ticks > 600) {
                    this.makeChosenBurrowPossibleBurrow();
                } else if (!MeerkatEntity.this.navigation.isFollowingPath()) {
                    if (!MeerkatEntity.this.isWithinDistance(blockPos, 16)) {
                        if (MeerkatEntity.this.isTooFar(blockPos)) {
                            this.setLost();
                        } else {
                            MeerkatEntity.this.startMovingTo(blockPos);
                        }
                    } else {
                        boolean bl = this.startMovingToFar(blockPos);
                        if (!bl) {
                            this.makeChosenBurrowPossibleBurrow();
                        } else if (this.path != null && MeerkatEntity.this.navigation.getCurrentPath().equalsPath(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = MeerkatEntity.this.navigation.getCurrentPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            MeerkatEntity.this.navigation.setRangeMultiplier(10.0F);
            MeerkatEntity.this.navigation.startMovingTo((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 1.0D);
            return MeerkatEntity.this.navigation.getCurrentPath() != null && MeerkatEntity.this.navigation.getCurrentPath().reachesTarget();
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
            if (MeerkatEntity.this.burrowPos != null) {
                this.addPossibleBurrow(MeerkatEntity.this.burrowPos);
            }

            this.setLost();
        }

        private void setLost() {
            MeerkatEntity.this.burrowPos = null;
            MeerkatEntity.this.ticksLeftToFindBurrow = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            if (pos.isWithinDistance(MeerkatEntity.this.getBlockPos(), 1.5D)) {
                return true;
            } else {
                Path path = MeerkatEntity.this.navigation.getCurrentPath();
                return path != null && path.getTarget().equals(pos) && path.reachesTarget() && path.isFinished();
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        float f = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        float g = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            if (((LivingEntity) target).getGroup() == EntityGroup.ARTHROPOD) {
                f += 5.0F;
            }
        }

        boolean bl = target.damage(DamageSource.mob(this), f);
        if (bl) {
            if (g > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(g * 0.5F, (double) MathHelper.sin(this.yaw * 0.017453292F), (double) (-MathHelper.cos(this.yaw * 0.017453292F)));
                this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
            }

            this.dealDamage(this, target);
            this.onAttacking(target);
        }

        return bl;
    }

    @Nullable
    @Override
    public MeerkatEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.MEERKAT.get().create(world);
    }

    @Override
    public float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        if (pose == EntityPose.CROUCHING) {
            return 0.5F;
        }
        return super.getActiveEyeHeight(pose, dimensions);
    }

    private Box calcBox() {
        EntityDimensions entityDimensions = this.getDimensions(EntityPose.STANDING);
        boolean bl = this.getPose() == EntityPose.STANDING;
        double f = (double) entityDimensions.width / (bl ? 4.0F : 2.0F);
        double height = (double) entityDimensions.height / (bl ? 1.0F : 2.0F);
        Vec3d vec3d = new Vec3d(this.getX() - f, this.getY(), this.getZ() - f);
        Vec3d vec3d2 = new Vec3d(this.getX() + f, this.getY() + height, this.getZ() + f);
        return new Box(vec3d, vec3d2);
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.getController().getAnimationState().equals(AnimationState.Stopped) || (animtimer == 10 && !this.isInsideWaterOrBubbleColumn() && !event.isMoving())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("stand"));
            this.setPose(EntityPose.STANDING);
            this.setBoundingBox(calcBox());
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk"));
            this.setPose(EntityPose.CROUCHING);
            this.setBoundingBox(calcBox());
            animtimer = 0;
        }
        if (animtimer < 10) animtimer++;
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, "controller", 2, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
