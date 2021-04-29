package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.util.EntityUtils;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class MeerkatEntity extends AnimalEntity implements IAnimatable {
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(Items.COD, Items.SALMON);
    private static final TrackedData<Boolean> TRUSTING;
    private static final TrackedData<Optional<UUID>> TRUSTED_UUID;
    private BlockPos structurepos = null;
    private int animtimer = 0;

    public MeerkatEntity(EntityType<? extends MeerkatEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F); // no like da water
        EntityUtils.setConfigHealth(this, Outvoted.config.common.entities.meerkat.health);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeerkatEntity.VentureGoal(this, 1.25D));
        this.goalSelector.add(4, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
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
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        this.structurepos = null;
        if (tag.contains("StructPos")) this.structurepos = NbtHelper.toBlockPos(tag.getCompound("StructPos"));

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

    private void setTrusted(LivingEntity entity) {
        setTrusted(entity.getUuid());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CAT_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
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
                System.out.println(serverWorld.getBiome(blockPos).getScale());
                BlockPos blockPos2 = serverWorld.locateStructure(structureFeature, blockPos, (int) Math.floor(10 * serverWorld.getBiome(blockPos).getScale()), false);
                return blockPos2;
            }
        }
        return null;
    }

    public static boolean canSpawn(EntityType<MeerkatEntity> entity, WorldAccess world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return world.getBaseLightLevel(blockPos, 0) > 8 && canMobSpawn(entity, world, spawnReason, blockPos, random) && world.getBlockState(blockPos.down()).isOf(Blocks.SAND);
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
                    setTrusted(player);
                    return ActionResult.CONSUME;
                }
            }
        }
        return super.interactMob(player, hand);
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
            this.mob.setTrusted(0);
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

    @Nullable
    @Override
    public MeerkatEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntityTypes.MEERKAT.get().create(world);
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.getController().getAnimationState().equals(AnimationState.Stopped) || (animtimer == 10 && !this.isInsideWaterOrBubbleColumn() && !event.isMoving())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("standing"));
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walking"));
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
