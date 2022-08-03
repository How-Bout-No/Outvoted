package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.block.BaseCopperButtonBlock;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;

public class CopperGolemEntity extends AbstractGolem implements IAnimatable {
    protected static final EntityDataAccessor<Integer> OXIDIZATION_LEVEL;
    protected static final EntityDataAccessor<Boolean> WAXED;
    protected static final EntityDataAccessor<CompoundTag> ROTATIONS;
    protected boolean pushingState = false;

    public CopperGolemEntity(EntityType<? extends CopperGolemEntity> type, Level worldIn) {
        super(type, worldIn);
        this.lookControl = new CGLookControl(this);
    }

    class CGLookControl extends LookControl {
        public CGLookControl(Mob mobEntity) {
            super(mobEntity);
        }

        public void tick() {
            if (CopperGolemEntity.this.isNotFrozen()) super.tick();
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PushButtonsGoal(this, 0.8D, 10));
        this.goalSelector.addGoal(2, new WanderGoal(this, 0.7D, 75));
        this.goalSelector.addGoal(3, new LookEntityGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new LookGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        HealthUtil.setConfigHealth(this, Config.copperGolemHealth.get());

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    static {
        OXIDIZATION_LEVEL = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.INT);
        WAXED = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.BOOLEAN);
        ROTATIONS = SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.COMPOUND_TAG);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OXIDIZATION_LEVEL, 0);
        this.entityData.define(WAXED, false);
        this.entityData.define(ROTATIONS, new CompoundTag());
    }

    public void setOxidizationLevel(int level) {
        this.entityData.set(OXIDIZATION_LEVEL, level);
    }

    public int getOxidizationLevel() {
        return this.entityData.get(OXIDIZATION_LEVEL);
    }

    public void setWaxed(boolean waxed) {
        this.entityData.set(WAXED, waxed);
    }

    public boolean isWaxed() {
        return this.entityData.get(WAXED);
    }

    public void setRotations(float[] inp) {
        float[] rot = inp.clone();
        for (int i = 0; i < rot.length; i++) {
            rot[i] *= 10000;
        }
        setRotationsI(ModUtil.toIntArray(rot));
    }

    public void setRotationsI(int[] inp) {
        if (inp == null) inp = new int[7];
        CompoundTag compound = new CompoundTag();
        compound.putIntArray("Rot", inp);
        this.entityData.set(ROTATIONS, compound);
    }

    public float[] getRotations() {
        float[] rot = ModUtil.toFloatArray(getRotationsI().clone());
        for (int i = 0; i < rot.length; i++) {
            rot[i] /= 10000;
        }
        return rot;
    }

    public int[] getRotationsI() {
        int[] rots = this.entityData.get(ROTATIONS).getIntArray("Rot");
        if (rots.length < 7) rots = new int[7];
        return rots;
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, this.getEyeHeight() - 0.1D, 0.0D);
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
        return SoundEvents.IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    protected float nextStep() {
        return (float) ((int) this.moveDist + 1);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.7F;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Oxidization", this.getOxidizationLevel());
        compound.putBoolean("Waxed", this.isWaxed());

        compound.putIntArray("Rotations", this.getRotationsI().clone());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOxidizationLevel(compound.getInt("Oxidization"));
        this.setWaxed(compound.getBoolean("Waxed"));

        this.setRotationsI(compound.getIntArray("Rotations"));
    }

    public static boolean canSpawn(EntityType<CopperGolemEntity> entity, LevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        return checkMobSpawnRules(entity, world, spawnReason, blockPos, random);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public void thunderHit(ServerLevel world, LightningBolt lightning) {
        this.unFreeze();
        this.setOxidizationLevel(0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (!this.isWaxed() && this.tickCount % (int) Math.max((35 * Math.sqrt(this.level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING))) / 3, 1) == 0 && this.random.nextFloat() < Config.copperGolemOxidation.get()) {
                this.oxidize();
            }
        }
    }

    public float[] createRot() {
        float limbSwing = this.animationPosition;
        float limbSwingAmount = this.animationSpeed;
        float oxidizeMult = this.getOxidizationMultiplier();
        if (oxidizeMult < 1 && oxidizeMult > 0) oxidizeMult += 0.25F;
        int partialTicks = 0;
        float f = Mth.rotLerp(partialTicks, this.yBodyRotO, this.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, this.yHeadRotO, this.yHeadRot);
        float[] rot = new float[7];
        rot[0] = Mth.lerp(partialTicks, this.xRotO, this.getXRot());
        rot[1] = f1 - f;
        rot[2] = Mth.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount;
        rot[3] = Mth.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount;
        rot[4] = Mth.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount;
        rot[5] = Mth.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount;
        rot[6] = this.getYRot();

        return rot;
    }

    @Override
    public void setRot(float yaw, float pitch) {
        super.setRot(yaw, pitch);
        if (!this.isNotFrozen()) {
            this.yHeadRot = yaw;
            this.yBodyRot = yaw;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getOxidizationLevel() == 2) {
            this.setRotations(this.createRot());
        } else if (!this.isNotFrozen()) {
            this.setRot(this.getRotations()[6], this.getXRot());
        }
    }

    @Override
    public float getSpeed() {
        return super.getSpeed() * this.getOxidizationMultiplier();
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
        this.setNoAi(true);
        this.getNavigation().setSpeedModifier(0);
        this.getNavigation().stop();
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0);
        this.getMoveControl().tick();
    }

    public void unFreeze() {
        this.setNoAi(false);
        this.getNavigation().setSpeedModifier(1);
    }

    protected void doPush(Entity entity) {
        if (this.isNotFrozen()) {
            super.doPush(entity);
        }
    }

    @Override
    public void push(Entity entity) {
        if (this.isNotFrozen()) {
            super.push(entity);
        }
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && this.isNotFrozen();
    }

    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            boolean ci = itemStack.is(Items.COPPER_INGOT);
            boolean ai = itemStack.getItem() instanceof AxeItem;
            boolean hi = itemStack.is(Items.HONEYCOMB);
            if (!ci && !ai && !hi) {
                return InteractionResult.PASS;
            } else {
                if (ci) {
                    float f = this.getHealth();
                    this.heal(this.getMaxHealth() / 4);
                    if (this.getHealth() == f) {
                        return InteractionResult.PASS;
                    } else {
                        float g = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                        this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, g);
                    }
                } else if (ai) {
                    if (this.isWaxed()) {
                        this.setWaxed(false);
                        this.playSound(SoundEvents.AXE_WAX_OFF, 1.0F, 1.0F);
                        this.level.levelEvent(LevelEvent.PARTICLES_WAX_OFF, this.blockPosition(), 0);
                    } else if (this.getOxidizationLevel() > 0) {
                        this.deOxidize();
                        this.playSound(SoundEvents.AXE_SCRAPE, 1.0F, 1.0F);
                        this.level.levelEvent(LevelEvent.PARTICLES_SCRAPE, this.blockPosition(), 0);
                    } else {
                        return InteractionResult.PASS;
                    }
                } else if (!this.isWaxed()) {
                    this.setWaxed(true);
                    this.playSound(SoundEvents.HONEYCOMB_WAX_ON, 1.0F, 1.0F);
                    this.level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_WAX_ON, this.blockPosition(), 0);
                } else {
                    return InteractionResult.PASS;
                }
                this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                if (!player.getAbilities().instabuild) {
                    if (ci || hi) itemStack.shrink(1);
                    else if (!this.level.isClientSide) itemStack.hurt(1, this.random, (ServerPlayer) player);
                }

                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        } else {
            if (!this.level.isClientSide) {
                float playerRot = getYRotD(player).get();
                float rotDiff = playerRot - this.getYRot();
                this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());

                if (rotDiff > 0) {
                    if (this.hasItemInSlot(EquipmentSlot.MAINHAND))
                    if (!player.getAbilities().instabuild || this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
                        dropItem(this.getMainHandItem());
                    this.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                } else {
                    if (!player.getAbilities().instabuild || this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
                        dropItem(this.getOffhandItem());
                    this.setItemInHand(InteractionHand.OFF_HAND, itemStack);
                }
            }
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
    }

    protected Optional<Float> getYRotD(Player player) {
        double d = player.getX() - this.getX();
        double e = player.getZ() - this.getZ();
        return !(Math.abs(e) > 9.999999747378752E-6) && !(Math.abs(d) > 9.999999747378752E-6) ? Optional.empty() : Optional.of((float) (Mth.atan2(d, e) * 57.2957763671875) - 180F);
    }

    private void dropItem(ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
        this.level.addFreshEntity(itemEntity);
    }

    public boolean checkSpawnObstruction(LevelReader world) {
        BlockPos blockPos = this.blockPosition();
        BlockPos blockPos2 = blockPos.below();
        BlockState blockState = world.getBlockState(blockPos2);
        if (!blockState.entityCanStandOn(world, blockPos2, this)) {
            return false;
        } else {
            for (int i = 1; i < 2; ++i) {
                BlockPos blockPos3 = blockPos.above(i);
                BlockState blockState2 = world.getBlockState(blockPos3);
                if (!NaturalSpawner.isValidEmptySpawnBlock(world, blockPos3, blockState2, blockState2.getFluidState(), ModEntityTypes.COPPER_GOLEM.get())) {
                    return false;
                }
            }

            return NaturalSpawner.isValidEmptySpawnBlock(world, blockPos, world.getBlockState(blockPos), Fluids.EMPTY.defaultFluidState(), ModEntityTypes.COPPER_GOLEM.get()) && !world.getEntityCollisions(this, new AABB(this.getOnPos())).isEmpty();
        }
    }

    static class PushButtonsGoal extends MoveToBlockGoal {
        public PushButtonsGoal(PathfinderMob pathAwareEntity, double d, int i) {
            super(pathAwareEntity, d, i);
        }

        protected int nextStartTick(PathfinderMob mob) {
            return 100 + mob.getRandom().nextInt(200);
        }

        public double getDesiredSquaredDistanceToTarget() {
            return 0.75D;
        }

        public boolean canUse() {
            return super.canUse() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.nextStartTick == 0 && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public void tick() {
            super.tick();
            if (this.isReachedTarget()) {
                ((CopperGolemEntity) this.mob).pushingState = true;
                this.nextStartTick = this.nextStartTick(this.mob);
                BlockState state = this.mob.level.getBlockState(this.blockPos);
                ((ButtonBlock) state.getBlock()).use(state, this.mob.level, this.blockPos, null, null, null);
            }
        }

        @Override
        protected BlockPos getMoveToTarget() {
            return this.blockPos;
        }

        @Override
        protected boolean isValidTarget(LevelReader world, BlockPos pos) {
            BlockState blockState = world.getBlockState(pos);
            return blockState.getBlock() instanceof BaseCopperButtonBlock;
        }
    }

    static class WanderGoal extends RandomStrollGoal {
        public WanderGoal(PathfinderMob pathAwareEntity, double d, int i) {
            super(pathAwareEntity, d, i);
        }

        public boolean canUse() {
            return super.canUse() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }
    }

    static class LookEntityGoal extends LookAtPlayerGoal {
        public LookEntityGoal(Mob mobEntity, Class<? extends LivingEntity> class_, float f) {
            super(mobEntity, class_, f);
        }

        public boolean canUse() {
            return super.canUse() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && ((CopperGolemEntity) this.mob).isNotFrozen();
        }
    }

    static class LookGoal extends Goal {
        private final Mob mob;
        private double deltaX;
        private double deltaZ;
        private int lookTime;

        public LookGoal(Mob mobEntity) {
            this.mob = mobEntity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return this.mob.getRandom().nextFloat() < 0.02F && ((CopperGolemEntity) this.mob).isNotFrozen();
        }

        public boolean canContinueToUse() {
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
            this.mob.getLookControl().setLookAt(this.mob.getX() + this.deltaX, this.mob.getEyeY(), this.mob.getZ() + this.deltaZ);
        }
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
//        event.getController().setAnimation(new AnimationBuilder().addAnimation("push", true));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<CopperGolemEntity> controller = new AnimationController<>(this, "controller", 1, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}

