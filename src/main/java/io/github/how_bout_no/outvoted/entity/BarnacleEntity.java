package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.*;

public class BarnacleEntity extends Monster implements IAnimatable {
    private static final EntityDataAccessor<Integer> ATTACKING;
    private static final EntityDataAccessor<Integer> TARGET_ENTITY;
    private LivingEntity targetedEntity;
    private static final Map<Integer, UUID> targetedEntities = new HashMap<>();
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected RandomStrollGoal wander;
    private boolean initAttack = false;
    private int attackCounter = 0;

    public BarnacleEntity(EntityType<? extends BarnacleEntity> type, Level worldIn) {
        super(type, worldIn);
        this.xpReward = 10;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new MoveHelperController(this);
    }

    protected void registerGoals() {
        MoveTowardsRestrictionGoal movetowardsrestrictiongoal = new MoveTowardsRestrictionGoal(this, 1.0D);
        this.wander = new RandomStrollGoal(this, 1.0D, 80);
        this.goalSelector.addGoal(3, new AttackGoal(this));
        this.goalSelector.addGoal(4, new ChaseGoal(this, 6.0D, 48.0F));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, BarnacleEntity.class, 72.0F, 4.0D, 4.0D));
        this.goalSelector.addGoal(6, movetowardsrestrictiongoal);
        this.goalSelector.addGoal(7, this.wander);
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.wander.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        movetowardsrestrictiongoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Dolphin.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        HealthUtil.setConfigHealth(this, Config.barnacleHealth.get());

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public static boolean canSpawn(EntityType<BarnacleEntity> entity, LevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        return blockPos.getY() >= world.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ()) && world.getDifficulty() != Difficulty.PEACEFUL && blockPos.getY() <= 45.0 && (spawnReason == MobSpawnType.SPAWNER || world.getFluidState(blockPos).is(FluidTags.WATER));
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    static {
        ATTACKING = SynchedEntityData.defineId(BarnacleEntity.class, EntityDataSerializers.INT);
        TARGET_ENTITY = SynchedEntityData.defineId(BarnacleEntity.class, EntityDataSerializers.INT);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_ENTITY, 0);
        this.entityData.define(ATTACKING, 0);
    }

    public int getAttackDuration() {
        return 80;
    }

    private void setTargetedEntity(int entityId) {
        this.entityData.set(TARGET_ENTITY, entityId);
    }

    public boolean hasTargetedEntity() {
        return this.entityData.get(TARGET_ENTITY) != 0;
    }

    private void setAttacking(int attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public int getAttackPhase() {
        return this.entityData.get(ATTACKING);
    }

    @Nullable
    public LivingEntity getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.level.isClientSide) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.level.getEntity(this.entityData.get(TARGET_ENTITY));
                if (entity instanceof LivingEntity) {
                    this.targetedEntity = (LivingEntity) entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    protected void updateAir(int air) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(air - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(DamageSource.DROWN, 5.0F);
            }
        } else {
            this.setAirSupply(300);
        }

    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }

    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getAmbientSoundInterval() {
        return 160;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.BARNACLE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.BARNACLE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.BARNACLE_DEATH.get();
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.4F;
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.getFluidState(pos).is(FluidTags.WATER) ? 10.0F + worldIn.getBrightness(pos) - 0.5F : super.getWalkTargetValue(pos, worldIn);
    }

    @Override
    public void die(DamageSource cause) {
        targetedEntities.remove(this.entityData.get(TARGET_ENTITY));
        super.die(cause);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(strength / 4, x, z);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (this.level.isClientSide) {
                if (!this.isInWater()) {
                    Vec3 vector3d = this.getDeltaMovement();
                    if (vector3d.y > 0.0D && this.clientSideTouchedGround && !this.isSilent()) {
                        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), this.getFlopSound(), this.getSoundSource(), 1.0F, 1.0F, false);
                    }

                    this.clientSideTouchedGround = vector3d.y < 0.0D && this.level.loadedAndEntityCanStandOn(this.blockPosition().below(), this);
                }
                if (this.hasTargetedEntity()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        ++this.clientSideAttackTime;
                    }
                }
            }
            if (this.hasTargetedEntity()) {
                this.setYRot(this.yHeadRot);
                LivingEntity livingentity = this.getTargetedEntity();
                if (livingentity != null) {
                    this.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                    this.getLookControl().tick();
                    double d5 = this.getAttackAnimationScale(0.0F);
                    double d0 = livingentity.getX() - this.getX();
                    double d1 = livingentity.getY(0.5D) - this.getEyeY();
                    double d2 = livingentity.getZ() - this.getZ();
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 = d0 / d3;
                    d1 = d1 / d3;
                    d2 = d2 / d3;
                    double d4 = this.random.nextDouble();
                    if (this.getAttackPhase() == 0) {
                        this.setAttacking(1);
                    }

                    while (d4 < d3) {
                        d4 += 1.8D - d5 + this.random.nextDouble() * (1.7D - d5);
                        livingentity.moveTo(this.getX() + d0 * d3, this.getEyeY() + d1, this.getZ() + d2 * d3, livingentity.getYRot(), livingentity.getXRot());
                        livingentity.setSwimming(false);
                        livingentity.updateSwimming();
                        if (!this.level.isClientSide) {
                            if (livingentity.isPassenger() && livingentity.getRootVehicle() instanceof Boat) {
                                Entity boat = livingentity.getRootVehicle();
                                livingentity.stopRiding();
                                boat.spawnAtLocation(((Boat) boat).getDropItem());
                                try {
                                    Containers.dropContents(boat.level, boat, (Container) boat);
                                } catch (Exception ignored) {
                                }
                                boat.discard();
                            }
                        }
                        if (this.getAttackPhase() != 0) {
                            if (attackCounter > 10) {
                                if (!initAttack) {
                                    livingentity.hurt(DamageSource.mobAttack(this), 0.1F);
                                    initAttack = true;
                                }
                            } else {
                                attackCounter++;
                            }
                            livingentity.push(-d0 / 50, -d1 / 50, -d2 / 50);
                        } else if (initAttack) {
                            initAttack = false;
                        } else {
                            attackCounter = 0;
                        }
                    }
                }
            }

            if (this.isInWaterOrBubble()) {
                this.setAirSupply(300);
            } else if (this.onGround) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.1F, 0.5D, (this.random.nextFloat() * 2.0F - 1.0F) * 0.1F));
                this.setYRot(this.random.nextFloat() * 360.0F);
                this.onGround = false;
                this.hasImpulse = true;
            }
        }
    }

    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.updateAir(i);
    }

    protected SoundEvent getFlopSound() {
        return ModSounds.BARNACLE_FLOP.get();
    }

    public float getAttackAnimationScale(float f) {
        return ((float) this.clientSideAttackTime + f) / (float) this.getAttackDuration();
    }

    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return !worldIn.getEntityCollisions(this, new AABB(this.getOnPos())).isEmpty();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource source, float amount) {
        if (this.wander != null) {
            this.wander.trigger();
        }

        return super.hurt(source, amount);
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getSpeed() == 0.0F && this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.001D, 0.0D));
            } else if (this.getTarget() != null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }

    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!this.hasRestriction()) {
            this.restrictTo(this.blockPosition(), 96);
        }
    }

    public boolean waterCheck(LivingEntity livingentity) {
        if (livingentity.getVehicle() != null) {
            return livingentity.getVehicle().isInWater();
        } else {
            return livingentity.isInWater() && (targetedEntities.get(livingentity.getId()) == null || targetedEntities.get(livingentity.getId()) == this.getUUID());
        }
    }

    static class ChaseGoal extends MoveTowardsTargetGoal {
        private final BarnacleEntity mob;
        private final double speed;

        public ChaseGoal(BarnacleEntity barnacle, double speedIn, float maxDistanceIn) {
            super(barnacle, speedIn, maxDistanceIn);
            this.mob = barnacle;
            this.speed = speedIn;
        }

        public void stop() {
        }

        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                return super.canUse() && this.mob.waterCheck(livingentity) && this.mob.isWithinRestriction();
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                return super.canContinueToUse() && this.mob.waterCheck(livingentity) && this.mob.isWithinRestriction();
            } else {
                return false;
            }
        }

        public void start() {
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                this.mob.getNavigation().moveTo(livingentity, this.speed);
            }
        }
    }

    static class AttackGoal extends Goal {
        private final BarnacleEntity mob;
        private int tickCounter;
        private boolean hasAttacked;

        public AttackGoal(BarnacleEntity entity) {
            this.mob = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            return livingentity != null && livingentity.isAlive() && this.mob.waterCheck(livingentity) && this.mob.distanceToSqr(this.mob.getTarget()) < 64.0D;
        }

        public boolean canContinueToUse() {
            if (this.mob.getTarget() != null) {
                return this.mob.distanceToSqr(this.mob.getTarget()) < 90.5D && this.mob.waterCheck(this.mob.getTarget());
            } else {
                return false;
            }
        }

        public void start() {
            this.tickCounter = -1;
            this.mob.getNavigation().stop();
            this.mob.getLookControl().setLookAt(this.mob.getTarget(), 90.0F, 90.0F);
            this.mob.hasImpulse = true;
            this.hasAttacked = false;
        }

        public void stop() {
            targetedEntities.remove(this.mob.entityData.get(TARGET_ENTITY));
            this.mob.setTargetedEntity(0);
            this.mob.setTarget(null);
            this.mob.wander.trigger();
            this.mob.setAttacking(0);
            this.hasAttacked = false;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                this.mob.getNavigation().stop();
                this.mob.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                if (!this.mob.hasLineOfSight(livingentity)) {
                    this.mob.setTarget(null);
                } else {
                    ++this.tickCounter;
                    if (this.mob.getAttackPhase() < 1) this.mob.setAttacking(1);
                    if (this.tickCounter == 0) {
                        this.mob.setTargetedEntity(this.mob.getTarget().getId());
                        targetedEntities.put(livingentity.getId(), this.mob.getUUID());
                    } else if (this.tickCounter >= this.mob.getAttackDuration()) {
                        if (this.tickCounter >= 600) {
                            this.mob.setAttacking(2);
                            if (this.tickCounter % 5 == 0) livingentity.hurt(DamageSource.mobAttack(this.mob), 2.0F);
                        } else if (this.tickCounter % 40 == 0 && this.mob.getAttackPhase() == 1) {
                            this.mob.setAttacking(3);
                        } else if (this.mob.getAttackPhase() == 3 && this.tickCounter % 6 == 0) {
                            if (!this.hasAttacked) {
                                livingentity.hurt(DamageSource.mobAttack(this.mob), 2.0F);
                            } else {
                                this.mob.setAttacking(1);
                            }
                            this.hasAttacked = !this.hasAttacked;
                        }
                    }
                }
            }
        }
    }

    static class MoveHelperController extends MoveControl {
        private final BarnacleEntity mob;

        public MoveHelperController(BarnacleEntity entity) {
            super(entity);
            this.mob = entity;
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                Vec3 vector3d = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
                double d0 = vector3d.length();
                double d1 = vector3d.x / d0;
                double d2 = vector3d.y / d0;
                double d3 = vector3d.z / d0;
                float f = (float) (Mth.atan2(vector3d.z, vector3d.x) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
                this.mob.yBodyRot = this.mob.getYRot();
                float f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = Mth.lerp(0.125F, this.mob.getSpeed(), f1);
                this.mob.setSpeed(f2);
                double d4 = Math.sin((double) (this.mob.tickCount + this.mob.getId()) * 0.5D) * 0.05D;
                double d5 = Math.cos(this.mob.getYRot() * ((float) Math.PI / 180F));
                double d6 = Math.sin(this.mob.getYRot() * ((float) Math.PI / 180F));
                double d7 = Math.sin((double) (this.mob.tickCount + this.mob.getId()) * 0.75D) * 0.05D;
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(d4 * d5, d7 * (d6 + d5) * 0.25D + (double) f2 * d2 * 0.1D, d4 * d6));
                LookControl lookcontroller = this.mob.getLookControl();
                double d8 = this.mob.getX() + d1 * 2.0D;
                double d9 = this.mob.getEyeY() + d2 / d0;
                double d10 = this.mob.getZ() + d3 * 2.0D;
                double d11 = lookcontroller.getWantedX();
                double d12 = lookcontroller.getWantedY();
                double d13 = lookcontroller.getWantedZ();
                if (!lookcontroller.isLookingAtTarget()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }

                this.mob.getLookControl().setLookAt(Mth.lerp(0.125D, d11, d8), Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), 10.0F, 40.0F);
            } else {
                this.mob.setSpeed(0.0F);
            }
        }
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        int phase = this.getAttackPhase();
        LivingEntity livingEntity = this.getTargetedEntity();
        if (this.hasTargetedEntity() && phase > 0 && livingEntity != null && livingEntity.position() != null) {
            GeckoLibCache.getInstance().parser.setValue("distance", this.distanceToSqr(livingEntity) + 15);
        }
        if (event.getController().getCurrentAnimation() == null || event.getController().getCurrentAnimation().animationName == null) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim"));
            return PlayState.CONTINUE;
        }
        switch (phase) {
            case 1 -> {
                if (event.getController().getCurrentAnimation().animationName.equals("bite")) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("reelin"));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("attack").addAnimation("reelin"));
                }
                return PlayState.CONTINUE;
            }
            case 2 -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("clamp"));
                return PlayState.CONTINUE;
            }
            case 3 -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("bite"));
                return PlayState.CONTINUE;
            }
            default -> {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("swim"));
                return PlayState.CONTINUE;
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<BarnacleEntity> controller = new AnimationController<>(this, "controller", 3, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}