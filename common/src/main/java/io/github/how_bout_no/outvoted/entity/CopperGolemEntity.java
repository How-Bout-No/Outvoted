package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.ModButtonBlock;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.util.ModUtil;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

public class CopperGolemEntity extends GolemEntity implements IAnimatable {
    protected static final TrackedData<Integer> OXIDIZATION_LEVEL;
    protected static final TrackedData<Boolean> WAXED;
    protected static final TrackedData<NbtCompound> ROTATIONS;
    protected boolean pushingState = false;

    public CopperGolemEntity(EntityType<? extends CopperGolemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new CopperGolemEntity.PushButtonsGoal(this, 0.8D, 10));
        this.goalSelector.add(2, new CopperGolemEntity.WanderGoal(this, 0.7D, 75));
        this.goalSelector.add(3, new CopperGolemEntity.LookEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(4, new CopperGolemEntity.LookGoal(this));
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.9D);
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, @Nullable EntityData spawnDataIn, @Nullable NbtCompound dataTag) {
        HealthUtil.setConfigHealth(this, Outvoted.commonConfig.entities.coppergolem.health);

        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    static {
        OXIDIZATION_LEVEL = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.INTEGER);
        WAXED = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        ROTATIONS = DataTracker.registerData(CopperGolemEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(OXIDIZATION_LEVEL, 0);
        this.dataTracker.startTracking(WAXED, false);
        this.dataTracker.startTracking(ROTATIONS, new NbtCompound());
    }

    public void setOxidizationLevel(int level) {
        this.dataTracker.set(OXIDIZATION_LEVEL, level);
    }

    public int getOxidizationLevel() {
        return this.dataTracker.get(OXIDIZATION_LEVEL);
    }

    public void setWaxed(boolean waxed) {
        this.dataTracker.set(WAXED, waxed);
    }

    public boolean isWaxed() {
        return this.dataTracker.get(WAXED);
    }

    public void setRotations(float[] inp) {
        float[] rot = inp.clone();
        for (int i=0; i < rot.length; i++) {
            rot[i] *= 10000;
        }
        setRotationsI(ModUtil.toIntArray(rot));
    }

    public void setRotationsI(int[] inp) {
        if (inp == null) inp = new int[7];
        NbtCompound compound = new NbtCompound();
        compound.putIntArray("Rot", inp);
        this.dataTracker.set(ROTATIONS, compound);
    }

    public float[] getRotations() {
        float[] rot = ModUtil.toFloatArray(getRotationsI().clone());
        for (int i=0; i < rot.length; i++) {
            rot[i] /= 10000;
        }
        return rot;
    }

    public int[] getRotationsI() {
        return this.dataTracker.get(ROTATIONS).getIntArray("Rot");
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, this.getStandingEyeHeight() - 0.1D, 0.0D);
    }

    public float getOxidizationMultiplier() {
        return switch (this.getOxidizationLevel()) {
            case 0 -> 1.0F;
            case 1 -> 0.75F;
            case 2 -> 0.5F;
            case 3 -> 0.0F;
            default -> throw new IllegalStateException("Unexpected value: " + this.getOxidizationLevel());
        };
    }

    public boolean isNotFrozen() {
        return this.getOxidizationLevel() < 3;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    protected float calculateNextStepSoundDistance() {
        return (float) ((int) this.distanceTraveled + 1);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose poseIn, EntityDimensions sizeIn) {
        return 0.7F;
    }

    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("Oxidization", this.getOxidizationLevel());
        compound.putBoolean("Waxed", this.isWaxed());

        compound.putIntArray("Rotations", this.getRotationsI().clone());
    }

    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setOxidizationLevel(compound.getInt("Oxidization"));
        this.setWaxed(compound.getBoolean("Waxed"));

        this.setRotationsI(compound.getIntArray("Rotations"));
    }

    public static boolean canSpawn(EntityType<CopperGolemEntity> entity, WorldAccess world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return canMobSpawn(entity, world, spawnReason, blockPos, random);
    }

    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        this.unFreeze();
        this.setOxidizationLevel(0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            if (!this.isWaxed() && this.age % 20 == 0 && this.random.nextFloat() < Outvoted.commonConfig.entities.coppergolem.oxidationRate) {
                this.oxidize();
            }
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getOxidizationLevel() < 3) {
            float limbSwing = this.limbAngle;
            float limbSwingAmount = this.limbDistance;
            float oxidizeMult = this.getOxidizationMultiplier();
            if (oxidizeMult < 1 && oxidizeMult > 0) oxidizeMult += 0.25F;
            float f = MathHelper.lerpAngleDegrees(0, this.prevBodyYaw, this.bodyYaw);
            float f1 = MathHelper.lerpAngleDegrees(0, this.prevHeadYaw, this.headYaw);
            float[] rot = new float[7];
            rot[0] = this.getPitch();
            rot[1] = f1 - f;
            rot[2] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount;
            rot[3] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount;
            rot[4] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount;
            rot[5] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount;
            rot[6] = this.getYaw();

            this.setRotations(rot);
        } else {
            this.setRotation(this.getRotations()[6], this.getPitch());
        }
    }

    @Override
    public float getMovementSpeed() {
        return super.getMovementSpeed() * this.getOxidizationMultiplier();
    }

    public void oxidize() {
        if (this.getOxidizationLevel() < 3) this.setOxidizationLevel(this.getOxidizationLevel() + 1);
        if (this.getOxidizationLevel() == 3) this.freeze();
    }

    public void deOxidize() {
        if (this.getOxidizationLevel() - 1 == 2) this.unFreeze();
        this.setOxidizationLevel(this.getOxidizationLevel() - 1);
    }

    public void freeze() {
        this.setOxidizationLevel(3);
        this.setAiDisabled(true);
        this.getNavigation().setSpeed(0);
        this.getNavigation().stop();
        this.getMoveControl().moveTo(this.getX(), this.getY(), this.getZ(), 0);
        this.getMoveControl().tick();
    }

    public void unFreeze() {
        this.setAiDisabled(false);
        this.getNavigation().setSpeed(1);
    }

    protected void pushAway(Entity entity) {
        if (this.isNotFrozen()) {
            super.pushAway(entity);
        }
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (this.isNotFrozen()) {
            super.pushAwayFrom(entity);
        }
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && this.isNotFrozen();
    }

    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean ci = itemStack.isOf(Items.COPPER_INGOT);
        boolean ai = itemStack.getItem() instanceof AxeItem;
        boolean hi = itemStack.isOf(Items.HONEYCOMB);
        if (!ci && !ai && !hi) {
            return ActionResult.PASS;
        } else {
            if (ci) {
                float f = this.getHealth();
                this.heal(6.25F);
                if (this.getHealth() == f) {
                    return ActionResult.PASS;
                } else {
                    float g = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                    this.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0F, g);
                }
            } else if (ai) {
                if (this.isWaxed()) {
                    this.setWaxed(false);
                    this.playSound(SoundEvents.ITEM_AXE_WAX_OFF, 1.0F, 1.0F);
                    this.world.syncWorldEvent(3004, this.getBlockPos(), 0);
                } else if (this.getOxidizationLevel() > 0) {
                    this.deOxidize();
                    this.playSound(SoundEvents.ITEM_AXE_SCRAPE, 1.0F, 1.0F);
                    this.world.syncWorldEvent(3005, this.getBlockPos(), 0);
                } else {
                    return ActionResult.PASS;
                }
            } else if (!this.isWaxed()) {
                this.setWaxed(true);
                this.playSound(SoundEvents.ITEM_HONEYCOMB_WAX_ON, 1.0F, 1.0F);
                this.world.syncWorldEvent(3003, this.getBlockPos(), 0);
            } else {
                return ActionResult.PASS;
            }
            this.emitGameEvent(GameEvent.MOB_INTERACT, this.getCameraBlockPos());
            if (!player.getAbilities().creativeMode) {
                if (ci || hi) itemStack.decrement(1);
                else itemStack.damage(1, this.random, (ServerPlayerEntity) player);
            }

            return ActionResult.success(this.world.isClient);
        }
    }

    public boolean canSpawn(WorldView world) {
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPos2 = blockPos.down();
        BlockState blockState = world.getBlockState(blockPos2);
        if (!blockState.hasSolidTopSurface(world, blockPos2, this)) {
            return false;
        } else {
            for (int i = 1; i < 2; ++i) {
                BlockPos blockPos3 = blockPos.up(i);
                BlockState blockState2 = world.getBlockState(blockPos3);
                if (!SpawnHelper.isClearForSpawn(world, blockPos3, blockState2, blockState2.getFluidState(), ModEntityTypes.COPPER_GOLEM.get())) {
                    return false;
                }
            }

            return SpawnHelper.isClearForSpawn(world, blockPos, world.getBlockState(blockPos), Fluids.EMPTY.getDefaultState(), ModEntityTypes.COPPER_GOLEM.get()) && world.intersectsEntities(this);
        }
    }

    static class PushButtonsGoal extends MoveToTargetPosGoal {
        public PushButtonsGoal(PathAwareEntity pathAwareEntity, double d, int i) {
            super(pathAwareEntity, d, i);
        }

        protected int getInterval(PathAwareEntity mob) {
            return 100 + mob.getRandom().nextInt(200);
        }

        public double getDesiredSquaredDistanceToTarget() {
            return 0.75D;
        }

        public boolean canStart() {
            return super.canStart() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean shouldContinue() {
            return super.shouldContinue() && this.cooldown == 0 && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public void tick() {
            super.tick();
            if (this.hasReached()) {
                ((CopperGolemEntity) this.mob).pushingState = true;
                this.cooldown = this.getInterval(this.mob);
                BlockState state = this.mob.world.getBlockState(this.targetPos);
                ((AbstractButtonBlock) state.getBlock()).onUse(state, this.mob.world, this.targetPos, null, null, null);
            }
        }

        @Override
        protected BlockPos getTargetPos() {
            return this.targetPos;
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockState blockState = world.getBlockState(pos);
            return blockState.getBlock() instanceof ModButtonBlock;
        }
    }

    static class WanderGoal extends WanderAroundGoal {
        public WanderGoal(PathAwareEntity pathAwareEntity, double d, int i) {
            super(pathAwareEntity, d, i);
        }

        public boolean canStart() {
            return super.canStart() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean shouldContinue() {
            return super.shouldContinue() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }
    }

    static class LookEntityGoal extends LookAtEntityGoal {
        public LookEntityGoal(MobEntity mobEntity, Class<? extends LivingEntity> class_, float f) {
            super(mobEntity, class_, f);
        }

        public boolean canStart() {
            return super.canStart() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean shouldContinue() {
            return super.shouldContinue() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }
    }

    static class LookGoal extends Goal {
        private final MobEntity mob;
        private double deltaX;
        private double deltaZ;
        private int lookTime;

        public LookGoal(MobEntity mobEntity) {
            this.mob = mobEntity;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            return this.mob.getRandom().nextFloat() < 0.02F && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean shouldContinue() {
            return this.lookTime >= 0 && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public void start() {
            double d = 6.283185307179586D * this.mob.getRandom().nextDouble();
            this.deltaX = Math.cos(d);
            this.deltaZ = Math.sin(d);
            this.lookTime = 20 + this.mob.getRandom().nextInt(20);
        }

        public void tick() {
            --this.lookTime;
            this.mob.getLookControl().lookAt(this.mob.getX() + this.deltaX, this.mob.getEyeY(), this.mob.getZ() + this.deltaZ);
        }
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
//        if (!this.isNotFrozen()) return PlayState.STOP;
        event.getController().setAnimation(new AnimationBuilder().addAnimation("headspin2", true));
//        if (this.random.nextInt(100) < 1) {
//            event.getController().setAnimation(new AnimationBuilder().addAnimation("headspinc"));
//        } else if (this.random.nextInt(100) < 1) {
//            event.getController().setAnimation(new AnimationBuilder().addAnimation("headspincc"));
//        }
//
//        if (this.pushingState) {
//            System.out.println("push");
//            event.getController().setAnimation(new AnimationBuilder().addAnimation("push"));
//            this.pushingState = false;
//        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<CopperGolemEntity> controller = new AnimationController<>(this, "controller", 2, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}

