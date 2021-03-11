package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.resource.GeckoLibCache;

import javax.annotation.Nullable;
import java.util.*;

public class KrakenEntity extends MonsterEntity implements IAnimatable {
    private static final DataParameter<Integer> ATTACKING = EntityDataManager.defineId(KrakenEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.defineId(KrakenEntity.class, DataSerializers.INT);
    private LivingEntity targetedEntity;
    private static Map<Integer, UUID> targetedEntities = new HashMap<>();
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected RandomWalkingGoal wander;

    public KrakenEntity(EntityType<? extends KrakenEntity> type, World worldIn) {
        super(type, worldIn);
        this.xpReward = 10;
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.moveControl = new KrakenEntity.MoveHelperController(this);
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackPhase() != 0) {
            if (this.hasTargetedEntity()) {
                GeckoLibCache.getInstance().parser.setValue("distance", this.distanceToSqr(this.getTargetedEntity()) + 15);
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

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MAX_HEALTH, OutvotedConfig.COMMON.healthkraken.get());
    }

    protected void registerGoals() {
        MoveTowardsRestrictionGoal movetowardsrestrictiongoal = new MoveTowardsRestrictionGoal(this, 1.0D);
        this.wander = new RandomWalkingGoal(this, 1.0D, 80);
        this.goalSelector.addGoal(3, new KrakenEntity.AttackGoal(this));
        this.goalSelector.addGoal(4, new KrakenEntity.ChaseGoal(this, 6.0D, 48.0F));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, KrakenEntity.class, 72.0F, 4.0D, 4.0D));
        this.goalSelector.addGoal(6, movetowardsrestrictiongoal);
        this.goalSelector.addGoal(7, this.wander);
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.wander.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        movetowardsrestrictiongoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static boolean canSpawn(EntityType<KrakenEntity> entity, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return blockPos.getY() <= 45.0;
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigator createNavigation(World worldIn) {
        return new SwimmerPathNavigator(this, worldIn);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_ENTITY, 0);
        this.entityData.define(ATTACKING, 0);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.WATER;
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

    public void onSyncedDataUpdated(DataParameter<?> key) {
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
        return ModSounds.KRAKEN_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.KRAKEN_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.KRAKEN_DEATH.get();
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.4F;
    }

    public float getWalkTargetValue(BlockPos pos, IWorldReader worldIn) {
        return worldIn.getFluidState(pos).is(FluidTags.WATER) ? 10.0F + worldIn.getBrightness(pos) - 0.5F : super.getWalkTargetValue(pos, worldIn);
    }

    /**
     * Called when the mob's health reaches 0.
     *
     * @param cause
     */
    @Override
    public void die(DamageSource cause) {
        targetedEntities.remove(this.entityData.get(TARGET_ENTITY));
        super.die(cause);
    }

    @Override
    public void knockback(float strength, double ratioX, double ratioZ) {
        super.knockback(strength / 4, ratioX, ratioZ);
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
                    Vector3d vector3d = this.getDeltaMovement();
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
                this.yRot = this.yHeadRot;
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
                        livingentity.moveTo(this.getX() + d0 * d3, this.getEyeY() + d1, this.getZ() + d2 * d3, livingentity.yRot, livingentity.xRot);
                        livingentity.setSwimming(false);
                        livingentity.updateSwimming();
                        if (!this.level.isClientSide) {
                            if (livingentity.isPassenger() && livingentity.getRootVehicle() instanceof BoatEntity) {
                                Entity boat = livingentity.getRootVehicle();
                                livingentity.stopRiding();
                                boat.spawnAtLocation(((BoatEntity) boat).getDropItem());
                                try {
                                    InventoryHelper.dropContents(boat.level, boat, (IInventory) boat);
                                } catch (Exception ignored) {
                                }
                                boat.remove();
                            }
                        }
                        if (this.getAttackPhase() != 0) {
                            livingentity.push(-d0 / 50, -d1 / 50, -d2 / 50);
                        }
                    }
                }
            }

            if (this.isInWaterOrBubble()) {
                this.setAirSupply(300);
            } else if (this.onGround) {
                this.setDeltaMovement(this.getDeltaMovement().add((double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.1F), 0.5D, (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.1F)));
                this.yRot = this.random.nextFloat() * 360.0F;
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
        return ModSounds.KRAKEN_FLOP.get();
    }

    public float getAttackAnimationScale(float f) {
        return ((float) this.clientSideAttackTime + f) / (float) this.getAttackDuration();
    }

    public boolean checkSpawnObstruction(IWorldReader worldIn) {
        return worldIn.isUnobstructed(this);
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

    public void travel(Vector3d travelVector) {
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
            return livingentity.isInWater() && livingentity.getEffect(Effects.DOLPHINS_GRACE) == null && (targetedEntities.get(livingentity.getId()) == null || targetedEntities.get(livingentity.getId()) == this.getUUID());
        }
    }

    static class ChaseGoal extends MoveTowardsTargetGoal {
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
        public boolean canUse() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                return super.canUse() && this.entity.waterCheck(livingentity) && this.entity.isWithinRestriction();
            } else {
                return false;
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                return super.canContinueToUse() && this.entity.waterCheck(livingentity) && this.entity.isWithinRestriction();
            } else {
                return false;
            }
        }

        public void start() {
        }

        public void tick() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                this.entity.getNavigation().moveTo(livingentity, this.speed);
            }
        }
    }

    static class AttackGoal extends Goal {
        private final KrakenEntity entity;
        private int tickCounter;

        public AttackGoal(KrakenEntity entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.entity.getTarget();
            return livingentity != null && livingentity.isAlive() && this.entity.waterCheck(livingentity) && this.entity.distanceToSqr(this.entity.getTarget()) < 64.0D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.entity.getTarget() != null) {
                return this.entity.distanceToSqr(this.entity.getTarget()) < 90.5D && this.entity.getTarget().getEffect(Effects.DOLPHINS_GRACE) == null && this.entity.waterCheck(this.entity.getTarget());
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
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 90.0F, 90.0F);
            this.entity.hasImpulse = true;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            targetedEntities.remove(this.entity.entityData.get(TARGET_ENTITY));
            this.entity.setTargetedEntity(0);
            this.entity.setTarget(null);
            this.entity.wander.trigger();
            this.entity.setAttacking(0);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.entity.getTarget();
            if (livingentity != null) {
                this.entity.getNavigation().stop();
                this.entity.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                if (!this.entity.canSee(livingentity)) {
                    this.entity.setTarget(null);
                } else {
                    ++this.tickCounter;
                    this.entity.setAttacking(1);
                    if (this.tickCounter == 0) {
                        this.entity.setTargetedEntity(this.entity.getTarget().getId());
                        targetedEntities.put(livingentity.getId(), this.entity.getUUID());
                    } else if (this.tickCounter >= this.entity.getAttackDuration()) {
                        if (this.tickCounter % 20 == 0) {
                            //livingentity.attackEntityFrom(DamageSource.causeMobDamage(this.entity), f);
                            if (livingentity.getEffect(Effects.WATER_BREATHING) != null && livingentity.getAirSupply() == 0) {
                                livingentity.hurt(DamageSource.DROWN, 2.0F);
                            }
                            livingentity.setAirSupply(Math.max(livingentity.getAirSupply() - 45, 0));
                        }
                    }
                }
            }
        }
    }

    static class MoveHelperController extends MovementController {
        private final KrakenEntity entity;

        public MoveHelperController(KrakenEntity entity) {
            super(entity);
            this.entity = entity;
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO && !this.entity.getNavigation().isDone()) {
                Vector3d vector3d = new Vector3d(this.wantedX - this.entity.getX(), this.wantedY - this.entity.getY(), this.wantedZ - this.entity.getZ());
                double d0 = vector3d.length();
                double d1 = vector3d.x / d0;
                double d2 = vector3d.y / d0;
                double d3 = vector3d.z / d0;
                float f = (float) (MathHelper.atan2(vector3d.z, vector3d.x) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.entity.yRot = this.rotlerp(this.entity.yRot, f, 90.0F);
                this.entity.yBodyRot = this.entity.yRot;
                float f1 = (float) (this.speedModifier * this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = MathHelper.lerp(0.125F, this.entity.getSpeed(), f1);
                this.entity.setSpeed(f2);
                double d4 = Math.sin((double) (this.entity.tickCount + this.entity.getId()) * 0.5D) * 0.05D;
                double d5 = Math.cos((double) (this.entity.yRot * ((float) Math.PI / 180F)));
                double d6 = Math.sin((double) (this.entity.yRot * ((float) Math.PI / 180F)));
                double d7 = Math.sin((double) (this.entity.tickCount + this.entity.getId()) * 0.75D) * 0.05D;
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(d4 * d5, d7 * (d6 + d5) * 0.25D + (double) f2 * d2 * 0.1D, d4 * d6));
                LookController lookcontroller = this.entity.getLookControl();
                double d8 = this.entity.getX() + d1 * 2.0D;
                double d9 = this.entity.getEyeY() + d2 / d0;
                double d10 = this.entity.getZ() + d3 * 2.0D;
                double d11 = lookcontroller.getWantedX();
                double d12 = lookcontroller.getWantedY();
                double d13 = lookcontroller.getWantedZ();
                if (!lookcontroller.isHasWanted()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }

                this.entity.getLookControl().setLookAt(MathHelper.lerp(0.125D, d11, d8), MathHelper.lerp(0.125D, d12, d9), MathHelper.lerp(0.125D, d13, d10), 10.0F, 40.0F);
            } else {
                this.entity.setSpeed(0.0F);
            }
        }
    }
}