package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.util.EntityUtils;
import io.github.how_bout_no.outvoted.init.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
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

public class KrakenEntity extends HostileEntity implements IAnimatable {
    private static final TrackedData<Integer> ATTACKING = DataTracker.registerData(KrakenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TARGET_ENTITY = DataTracker.registerData(KrakenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private LivingEntity targetedEntity;
    private static Map<Integer, UUID> targetedEntities = new HashMap<>();
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected net.minecraft.entity.ai.goal.WanderAroundGoal wander;
    private boolean initAttack = false;

    public KrakenEntity(EntityType<? extends KrakenEntity> type, World worldIn) {
        super(type, worldIn);
        this.experiencePoints = 10;
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.moveControl = new KrakenEntity.MoveHelperController(this);
        EntityUtils.setConfigHealth(this, Outvoted.config.get().entities.kraken.health);
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackPhase() != 0) {
            if (this.hasTargetedEntity()) {
                GeckoLibCache.getInstance().parser.setValue("distance", this.squaredDistanceTo(this.getTargetedEntity()) + 15);
            }
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack").addAnimation("reelin"));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("swim"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, "controller", 5, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return HostileEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    protected void initGoals() {
        GoToWalkTargetGoal movetowardsrestrictiongoal = new GoToWalkTargetGoal(this, 1.0D);
        this.wander = new net.minecraft.entity.ai.goal.WanderAroundGoal(this, 1.0D, 80);
        this.goalSelector.add(3, new KrakenEntity.AttackGoal(this));
        this.goalSelector.add(4, new KrakenEntity.ChaseGoal(this, 6.0D, 48.0F));
        this.goalSelector.add(5, new FleeEntityGoal<>(this, KrakenEntity.class, 72.0F, 4.0D, 4.0D));
        this.goalSelector.add(6, movetowardsrestrictiongoal);
        this.goalSelector.add(7, this.wander);
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.wander.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        movetowardsrestrictiongoal.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static boolean canSpawn(EntityType<KrakenEntity> entity, WorldAccess world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return blockPos.getY() <= 45.0;
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected EntityNavigation createNavigation(World worldIn) {
        return new SwimNavigation(this, worldIn);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TARGET_ENTITY, 0);
        this.dataTracker.startTracking(ATTACKING, 0);
    }

    public boolean canBreatheInWater() {
        return true;
    }

    public net.minecraft.entity.EntityGroup getGroup() {
        return net.minecraft.entity.EntityGroup.AQUATIC;
    }

    protected void updateAir(int air) {
        if (this.isAlive() && !this.isInsideWaterOrBubbleColumn()) {
            this.setAir(air - 1);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.damage(DamageSource.DROWN, 5.0F);
            }
        } else {
            this.setAir(300);
        }

    }

    public int getAttackDuration() {
        return 80;
    }

    private void setTargetedEntity(int entityId) {
        this.dataTracker.set(TARGET_ENTITY, entityId);
    }

    public boolean hasTargetedEntity() {
        return this.dataTracker.get(TARGET_ENTITY) != 0;
    }

    private void setAttacking(int attacking) {
        this.dataTracker.set(ATTACKING, attacking);
    }

    public int getAttackPhase() {
        return this.dataTracker.get(ATTACKING);
    }

    @Nullable
    public LivingEntity getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.world.isClient) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.world.getEntityById(this.dataTracker.get(TARGET_ENTITY));
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

    public void onTrackedDataSet(TrackedData<?> key) {
        super.onTrackedDataSet(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }

    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getMinAmbientSoundDelay() {
        return 160;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.KRAKEN_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.KRAKEN_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.KRAKEN_DEATH.get();
    }

    protected boolean canClimb() {
        return false;
    }

    protected float getActiveEyeHeight(EntityPose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.4F;
    }

    public float getPathfindingFavor(BlockPos pos, WorldView worldIn) {
        return worldIn.getFluidState(pos).isIn(FluidTags.WATER) ? 10.0F + worldIn.getBrightness(pos) - 0.5F : super.getPathfindingFavor(pos, worldIn);
    }

    /**
     * Called when the mob's health reaches 0.
     *
     * @param cause
     */
    @Override
    public void onDeath(DamageSource cause) {
        targetedEntities.remove(this.dataTracker.get(TARGET_ENTITY));
        super.onDeath(cause);
    }

    @Override
    public void takeKnockback(float strength, double ratioX, double ratioZ) {
        super.takeKnockback(strength / 4, ratioX, ratioZ);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void tickMovement() {
        super.tickMovement();
        if (this.isAlive()) {
            if (this.world.isClient) {
                if (!this.isTouchingWater()) {
                    Vec3d vector3d = this.getVelocity();
                    if (vector3d.y > 0.0D && this.clientSideTouchedGround && !this.isSilent()) {
                        this.world.playSound(this.getX(), this.getY(), this.getZ(), this.getFlopSound(), this.getSoundCategory(), 1.0F, 1.0F, false);
                    }

                    this.clientSideTouchedGround = vector3d.y < 0.0D && this.world.isTopSolid(this.getBlockPos().down(), this);
                }
                if (this.hasTargetedEntity()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        ++this.clientSideAttackTime;
                    }
                }
            }
            if (this.hasTargetedEntity()) {
                this.yaw = this.headYaw;
                LivingEntity livingentity = this.getTargetedEntity();
                if (livingentity != null) {
                    this.getLookControl().lookAt(livingentity, 90.0F, 90.0F);
                    this.getLookControl().tick();
                    double d5 = this.getAttackAnimationScale(0.0F);
                    double d0 = livingentity.getX() - this.getX();
                    double d1 = livingentity.getBodyY(0.5D) - this.getEyeY();
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
                        livingentity.refreshPositionAndAngles(this.getX() + d0 * d3, this.getEyeY() + d1, this.getZ() + d2 * d3, livingentity.yaw, livingentity.pitch);
                        livingentity.setSwimming(false);
                        livingentity.updateSwimming();
                        if (!this.world.isClient) {
                            if (livingentity.hasVehicle() && livingentity.getRootVehicle() instanceof BoatEntity) {
                                Entity boat = livingentity.getRootVehicle();
                                livingentity.stopRiding();
                                boat.dropItem(((BoatEntity) boat).asItem());
                                try {
                                    ItemScatterer.spawn(boat.world, boat, (Inventory) boat);
                                } catch (Exception ignored) {
                                }
                                boat.remove();
                            }
                        }
                        if (this.getAttackPhase() != 0) {
                            if (!initAttack) {
                                livingentity.damage(DamageSource.DROWN, 0.1F);
                                initAttack = true;
                            }
                            livingentity.addVelocity(-d0 / 50, -d1 / 50, -d2 / 50);
                        } else if (initAttack) {
                            initAttack = false;
                        }
                    }
                }
            }

            if (this.isInsideWaterOrBubbleColumn()) {
                this.setAir(300);
            } else if (this.onGround) {
                this.setVelocity(this.getVelocity().add((double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.1F), 0.5D, (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.1F)));
                this.yaw = this.random.nextFloat() * 360.0F;
                this.onGround = false;
                this.velocityDirty = true;
            }
        }
    }

    public void baseTick() {
        int i = this.getAir();
        super.baseTick();
        this.updateAir(i);
    }

    protected SoundEvent getFlopSound() {
        return ModSounds.KRAKEN_FLOP.get();
    }

    public float getAttackAnimationScale(float f) {
        return ((float) this.clientSideAttackTime + f) / (float) this.getAttackDuration();
    }

    public boolean canSpawn(WorldView worldIn) {
        return worldIn.intersectsEntities(this);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean damage(DamageSource source, float amount) {
        if (this.wander != null) {
            this.wander.ignoreChanceOnce();
        }

        return super.damage(source, amount);
    }

    public void travel(Vec3d travelVector) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, travelVector);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9D));
            if (this.getMovementSpeed() == 0.0F && this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.001D, 0.0D));
            } else if (this.getTarget() != null) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }

    }

    protected void mobTick() {
        super.mobTick();
        if (!this.hasPositionTarget()) {
            this.setPositionTarget(this.getBlockPos(), 96);
        }
    }

    public boolean waterCheck(LivingEntity livingentity) {
        if (livingentity.getVehicle() != null) {
            return livingentity.getVehicle().isTouchingWater();
        } else {
            return livingentity.isTouchingWater() && livingentity.getStatusEffect(StatusEffects.DOLPHINS_GRACE) == null && (targetedEntities.get(livingentity.getEntityId()) == null || targetedEntities.get(livingentity.getEntityId()) == this.getUuid());
        }
    }

    static class ChaseGoal extends WanderNearTargetGoal {
        private final KrakenEntity entity;
        private final double speed;

        public ChaseGoal(KrakenEntity kraken, double speedIn, float maxDistanceIn) {
            super(kraken, speedIn, maxDistanceIn);
            this.entity = kraken;
            this.speed = speedIn;
        }

        public void stop() {
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public boolean canStart() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                return super.canStart() && this.entity.waterCheck(livingentity) && this.entity.isInWalkTargetRange();
            } else {
                return false;
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public boolean shouldContinue() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                return super.shouldContinue() && this.entity.waterCheck(livingentity) && this.entity.isInWalkTargetRange();
            } else {
                return false;
            }
        }

        public void start() {
        }

        public void tick() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                this.entity.getNavigation().startMovingTo(livingentity, this.speed);
            }
        }
    }

    static class AttackGoal extends Goal {
        private final KrakenEntity entity;
        private int tickCounter;

        public AttackGoal(KrakenEntity entity) {
            this.entity = entity;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canStart() {
            LivingEntity livingentity = this.entity.getTarget();
            return livingentity != null && livingentity.isAlive() && this.entity.waterCheck(livingentity) && this.entity.squaredDistanceTo(this.entity.getTarget()) < 64.0D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinue() {
            if (this.entity.getTarget() != null) {
                return this.entity.squaredDistanceTo(this.entity.getTarget()) < 90.5D && this.entity.getTarget().getStatusEffect(StatusEffects.DOLPHINS_GRACE) == null && this.entity.waterCheck(this.entity.getTarget());
            } else {
                return false;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.tickCounter = -10;
            this.entity.getNavigation().stop();
            this.entity.getLookControl().lookAt(this.entity.getTarget(), 90.0F, 90.0F);
            this.entity.velocityDirty = true;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            targetedEntities.remove(this.entity.dataTracker.get(TARGET_ENTITY));
            this.entity.setTargetedEntity(0);
            this.entity.setTarget(null);
            this.entity.wander.ignoreChanceOnce();
            this.entity.setAttacking(0);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                this.entity.getNavigation().stop();
                this.entity.getLookControl().lookAt(livingentity, 90.0F, 90.0F);
                if (!this.entity.canSee(livingentity)) {
                    this.entity.setTarget(null);
                } else {
                    ++this.tickCounter;
                    this.entity.setAttacking(1);
                    if (this.tickCounter == 0) {
                        this.entity.setTargetedEntity(this.entity.getTarget().getEntityId());
                        targetedEntities.put(livingentity.getEntityId(), this.entity.getUuid());
                    } else if (this.tickCounter >= this.entity.getAttackDuration()) {
                        if (this.tickCounter % 20 == 0) {
                            //livingentity.attackEntityFrom(DamageSource.causeMobDamage(this.entity), f);
                            if (livingentity.getStatusEffect(StatusEffects.WATER_BREATHING) != null && livingentity.getAir() == 0) {
                                livingentity.damage(DamageSource.DROWN, 2.0F);
                            }
                            livingentity.setAir(Math.max(livingentity.getAir() - 45, 0));
                        }
                    }
                }
            }
        }
    }

    static class MoveHelperController extends MoveControl {
        private final KrakenEntity entity;

        public MoveHelperController(KrakenEntity entity) {
            super(entity);
            this.entity = entity;
        }

        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO && !this.entity.getNavigation().isIdle()) {
                Vec3d vector3d = new Vec3d(this.targetX - this.entity.getX(), this.targetY - this.entity.getY(), this.targetZ - this.entity.getZ());
                double d0 = vector3d.length();
                double d1 = vector3d.x / d0;
                double d2 = vector3d.y / d0;
                double d3 = vector3d.z / d0;
                float f = (float) (MathHelper.atan2(vector3d.z, vector3d.x) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.entity.yaw = this.changeAngle(this.entity.yaw, f, 90.0F);
                this.entity.bodyYaw = this.entity.yaw;
                float f1 = (float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float f2 = MathHelper.lerp(0.125F, this.entity.getMovementSpeed(), f1);
                this.entity.setMovementSpeed(f2);
                double d4 = Math.sin((double) (this.entity.age + this.entity.getEntityId()) * 0.5D) * 0.05D;
                double d5 = Math.cos((double) (this.entity.yaw * ((float) Math.PI / 180F)));
                double d6 = Math.sin((double) (this.entity.yaw * ((float) Math.PI / 180F)));
                double d7 = Math.sin((double) (this.entity.age + this.entity.getEntityId()) * 0.75D) * 0.05D;
                this.entity.setVelocity(this.entity.getVelocity().add(d4 * d5, d7 * (d6 + d5) * 0.25D + (double) f2 * d2 * 0.1D, d4 * d6));
                LookControl lookcontroller = this.entity.getLookControl();
                double d8 = this.entity.getX() + d1 * 2.0D;
                double d9 = this.entity.getEyeY() + d2 / d0;
                double d10 = this.entity.getZ() + d3 * 2.0D;
                double d11 = lookcontroller.getLookX();
                double d12 = lookcontroller.getLookY();
                double d13 = lookcontroller.getLookZ();
                if (!lookcontroller.isActive()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }

                this.entity.getLookControl().lookAt(MathHelper.lerp(0.125D, d11, d8), MathHelper.lerp(0.125D, d12, d9), MathHelper.lerp(0.125D, d13, d10), 10.0F, 40.0F);
            } else {
                this.entity.setMovementSpeed(0.0F);
            }
        }
    }
}