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
import net.minecraft.world.Difficulty;
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

import javax.annotation.Nullable;
import java.util.*;

public class KrakenEntity extends MonsterEntity implements IAnimatable {
    private static final DataParameter<Integer> ATTACKING = EntityDataManager.createKey(KrakenEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.createKey(KrakenEntity.class, DataSerializers.VARINT);
    private LivingEntity targetedEntity;
    private static Map<Integer, UUID> targetedEntities = new HashMap<>();
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected RandomWalkingGoal wander;

    public KrakenEntity(EntityType<? extends KrakenEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 10;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.moveController = new KrakenEntity.MoveHelperController(this);
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackPhase() != 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kraken.attack").addAnimation("animation.kraken.reelin").addAnimation("animation.kraken.reelin2"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kraken.swim"));
        }

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
        return MonsterEntity.registerAttributes()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.1D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 48.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, OutvotedConfig.COMMON.healthkraken.get());
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
        this.wander.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        movetowardsrestrictiongoal.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static boolean canSpawn(EntityType<KrakenEntity> entity, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return blockPos.getY() <= 45.0;
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigator createNavigator(World worldIn) {
        return new SwimmerPathNavigator(this, worldIn);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(TARGET_ENTITY, 0);
        this.dataManager.register(ATTACKING, 0);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    protected void updateAir(int air) {
        if (this.isAlive() && !this.isInWaterOrBubbleColumn()) {
            this.setAir(air - 1);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 5.0F);
            }
        } else {
            this.setAir(300);
        }

    }

    public int getAttackDuration() {
        return 80;
    }

    private void setTargetedEntity(int entityId) {
        this.dataManager.set(TARGET_ENTITY, entityId);
    }

    public boolean hasTargetedEntity() {
        return this.dataManager.get(TARGET_ENTITY) != 0;
    }

    private void setAttacking(int attacking) {
        this.dataManager.set(ATTACKING, attacking);
    }

    public int getAttackPhase() {
        return this.dataManager.get(ATTACKING);
    }

    @Nullable
    public LivingEntity getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.world.isRemote) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.world.getEntityByID(this.dataManager.get(TARGET_ENTITY));
                if (entity instanceof LivingEntity) {
                    this.targetedEntity = (LivingEntity) entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.getAttackTarget();
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }

    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval() {
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

    protected boolean canTriggerWalking() {
        return false;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.4F;
    }

    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        return worldIn.getFluidState(pos).isTagged(FluidTags.WATER) ? 10.0F + worldIn.getBrightness(pos) - 0.5F : super.getBlockPathWeight(pos, worldIn);
    }

    /**
     * Called when the mob's health reaches 0.
     *
     * @param cause
     */
    @Override
    public void onDeath(DamageSource cause) {
        targetedEntities.remove(this.dataManager.get(TARGET_ENTITY));
        super.onDeath(cause);
    }

    @Override
    public void applyKnockback(float strength, double ratioX, double ratioZ) {
        super.applyKnockback(strength / 4, ratioX, ratioZ);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick() {
        super.livingTick();
        if (this.isAlive()) {
            if (this.world.isRemote) {
                if (!this.isInWater()) {
                    Vector3d vector3d = this.getMotion();
                    if (vector3d.y > 0.0D && this.clientSideTouchedGround && !this.isSilent()) {
                        this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), this.getFlopSound(), this.getSoundCategory(), 1.0F, 1.0F, false);
                    }

                    this.clientSideTouchedGround = vector3d.y < 0.0D && this.world.isTopSolid(this.getPosition().down(), this);
                }
                if (this.hasTargetedEntity()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        ++this.clientSideAttackTime;
                    }
                }
            }
            if (this.hasTargetedEntity()) {
                this.rotationYaw = this.rotationYawHead;
                LivingEntity livingentity = this.getTargetedEntity();
                if (livingentity != null) {
                    this.getLookController().setLookPositionWithEntity(livingentity, 90.0F, 90.0F);
                    this.getLookController().tick();
                    double d5 = this.getAttackAnimationScale(0.0F);
                    double d0 = livingentity.getPosX() - this.getPosX();
                    double d1 = livingentity.getPosYHeight(0.5D) - this.getPosYEye();
                    double d2 = livingentity.getPosZ() - this.getPosZ();
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 = d0 / d3;
                    d1 = d1 / d3;
                    d2 = d2 / d3;
                    double d4 = this.rand.nextDouble();
                    if (this.getAttackPhase() == 0) {
                        this.setAttacking(1);
                    }

                    while (d4 < d3) {
                        d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
                        livingentity.setLocationAndAngles(this.getPosX() + d0 * d3, this.getPosYEye() + d1, this.getPosZ() + d2 * d3, livingentity.rotationYaw, livingentity.rotationPitch);
                        livingentity.setSwimming(false);
                        livingentity.updateSwimming();
                        if (!this.world.isRemote) {
                            if (livingentity.isPassenger() && livingentity.getLowestRidingEntity() instanceof BoatEntity) {
                                Entity boat = livingentity.getLowestRidingEntity();
                                livingentity.stopRiding();
                                boat.entityDropItem(((BoatEntity) boat).getItemBoat());
                                try {
                                    InventoryHelper.dropInventoryItems(boat.world, boat, (IInventory) boat);
                                } catch (Exception e) {
                                }
                                boat.remove();
                            }
                        }
                        if (this.getAttackPhase() != 0) {
                            livingentity.addVelocity(-d0 / 60, -d1 / 60, -d2 / 60);
                        }
                    }
                }
            }

            if (this.isInWaterOrBubbleColumn()) {
                this.setAir(300);
            } else if (this.onGround) {
                this.setMotion(this.getMotion().add((double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.1F), 0.5D, (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.1F)));
                this.rotationYaw = this.rand.nextFloat() * 360.0F;
                this.onGround = false;
                this.isAirBorne = true;
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

    public boolean isNotColliding(IWorldReader worldIn) {
        return worldIn.checkNoEntityCollision(this);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.wander != null) {
            this.wander.makeUpdate();
        }

        return super.attackEntityFrom(source, amount);
    }

    public void travel(Vector3d travelVector) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.9D));
            if (this.getAIMoveSpeed() == 0.0F && this.getAttackTarget() == null) {
                this.setMotion(this.getMotion().add(0.0D, -0.001D, 0.0D));
            } else if (this.getAttackTarget() != null) {
                this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }

    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (!this.detachHome()) {
            this.setHomePosAndDistance(this.getPosition(), 96);
        }
    }

    public boolean waterCheck(LivingEntity livingentity) {
        if (livingentity.getRidingEntity() != null) {
            return livingentity.getRidingEntity().isInWater();
        } else {
            return livingentity.isInWater() && livingentity.getActivePotionEffect(Effects.DOLPHINS_GRACE) == null && (targetedEntities.get(livingentity.getEntityId()) == null || targetedEntities.get(livingentity.getEntityId()) == this.getUniqueID());
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

        public void resetTask() {
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = this.entity.getAttackTarget();
            if (livingentity != null) {
                return super.shouldExecute() && this.entity.waterCheck(livingentity) && this.entity.isWithinHomeDistanceCurrentPosition();
            } else {
                return false;
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = this.entity.getAttackTarget();
            if (livingentity != null) {
                return super.shouldContinueExecuting() && this.entity.waterCheck(livingentity) && this.entity.isWithinHomeDistanceCurrentPosition();
            } else {
                return false;
            }
        }

        public void startExecuting() {
        }

        public void tick() {
            LivingEntity livingentity = this.entity.getAttackTarget();
            if (livingentity != null) {
                this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.speed);
            }
        }
    }

    static class AttackGoal extends Goal {
        private final KrakenEntity entity;
        private int tickCounter;

        public AttackGoal(KrakenEntity entity) {
            this.entity = entity;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = this.entity.getAttackTarget();
            return livingentity != null && livingentity.isAlive() && this.entity.waterCheck(livingentity) && this.entity.getDistanceSq(this.entity.getAttackTarget()) < 64.0D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            if (this.entity.getAttackTarget() != null) {
                return this.entity.getDistanceSq(this.entity.getAttackTarget()) < 90.5D && this.entity.getAttackTarget().getActivePotionEffect(Effects.DOLPHINS_GRACE) == null && this.entity.waterCheck(this.entity.getAttackTarget());
            } else {
                return false;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.tickCounter = -10;
            this.entity.getNavigator().clearPath();
            this.entity.getLookController().setLookPositionWithEntity(this.entity.getAttackTarget(), 90.0F, 90.0F);
            this.entity.isAirBorne = true;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            targetedEntities.remove(this.entity.dataManager.get(TARGET_ENTITY));
            this.entity.setTargetedEntity(0);
            this.entity.setAttackTarget(null);
            this.entity.wander.makeUpdate();
            this.entity.setAttacking(0);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.entity.getAttackTarget();
            if (livingentity != null) {
                this.entity.getNavigator().clearPath();
                this.entity.getLookController().setLookPositionWithEntity(livingentity, 90.0F, 90.0F);
                if (!this.entity.canEntityBeSeen(livingentity)) {
                    this.entity.setAttackTarget(null);
                } else {
                    ++this.tickCounter;
                    this.entity.setAttacking(1);
                    if (this.tickCounter == 0) {
                        this.entity.setTargetedEntity(this.entity.getAttackTarget().getEntityId());
                        targetedEntities.put(livingentity.getEntityId(), this.entity.getUniqueID());
                    } else if (this.tickCounter >= this.entity.getAttackDuration()) {
                        float f = 2.0F;
                        if (this.entity.world.getDifficulty() == Difficulty.HARD) {
                            f += 4.0F;
                        }

                        if (this.tickCounter % 20 == 0) {
                            //livingentity.attackEntityFrom(DamageSource.causeMobDamage(this.entity), f);
                            if (livingentity.getActivePotionEffect(Effects.WATER_BREATHING) != null) {
                                livingentity.attackEntityFrom(DamageSource.DROWN, f);
                            }
                            if (livingentity.getAir() - 45 >= 0) {
                                livingentity.setAir(livingentity.getAir() - 45);
                            }
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
            if (this.action == MovementController.Action.MOVE_TO && !this.entity.getNavigator().noPath()) {
                Vector3d vector3d = new Vector3d(this.posX - this.entity.getPosX(), this.posY - this.entity.getPosY(), this.posZ - this.entity.getPosZ());
                double d0 = vector3d.length();
                double d1 = vector3d.x / d0;
                double d2 = vector3d.y / d0;
                double d3 = vector3d.z / d0;
                float f = (float) (MathHelper.atan2(vector3d.z, vector3d.x) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 90.0F);
                this.entity.renderYawOffset = this.entity.rotationYaw;
                float f1 = (float) (this.speed * this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = MathHelper.lerp(0.125F, this.entity.getAIMoveSpeed(), f1);
                this.entity.setAIMoveSpeed(f2);
                double d4 = Math.sin((double) (this.entity.ticksExisted + this.entity.getEntityId()) * 0.5D) * 0.05D;
                double d5 = Math.cos((double) (this.entity.rotationYaw * ((float) Math.PI / 180F)));
                double d6 = Math.sin((double) (this.entity.rotationYaw * ((float) Math.PI / 180F)));
                double d7 = Math.sin((double) (this.entity.ticksExisted + this.entity.getEntityId()) * 0.75D) * 0.05D;
                this.entity.setMotion(this.entity.getMotion().add(d4 * d5, d7 * (d6 + d5) * 0.25D + (double) f2 * d2 * 0.1D, d4 * d6));
                LookController lookcontroller = this.entity.getLookController();
                double d8 = this.entity.getPosX() + d1 * 2.0D;
                double d9 = this.entity.getPosYEye() + d2 / d0;
                double d10 = this.entity.getPosZ() + d3 * 2.0D;
                double d11 = lookcontroller.getLookPosX();
                double d12 = lookcontroller.getLookPosY();
                double d13 = lookcontroller.getLookPosZ();
                if (!lookcontroller.getIsLooking()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }

                this.entity.getLookController().setLookPosition(MathHelper.lerp(0.125D, d11, d8), MathHelper.lerp(0.125D, d12, d9), MathHelper.lerp(0.125D, d13, d10), 10.0F, 40.0F);
            } else {
                this.entity.setAIMoveSpeed(0.0F);
            }
        }
    }
}