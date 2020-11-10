package com.hbn.outvoted.entities.kraken;

import com.hbn.outvoted.config.OutvotedConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.animation.controller.EntityAnimationController;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.event.AnimationTestEvent;
import software.bernie.geckolib.manager.EntityAnimationManager;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class KrakenEntity extends MonsterEntity implements IAnimatedEntity {
    private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(KrakenEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ATTACKING = EntityDataManager.createKey(KrakenEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.createKey(KrakenEntity.class, DataSerializers.VARINT);
    private LivingEntity targetedEntity;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected RandomWalkingGoal wander;

    public KrakenEntity(EntityType<? extends KrakenEntity> type, World worldIn) {
        super(type, worldIn);
        this.manager.addAnimationController(controller);
        this.experienceValue = 10;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.moveController = new KrakenEntity.MoveHelperController(this);
        //controller.registerSoundListener(this::soundListener);
    }

    EntityAnimationManager manager = new EntityAnimationManager();
    EntityAnimationController<KrakenEntity> controller = new EntityAnimationController<>(this, "controller", 5, this::animationPredicate);

    public <E extends Entity> boolean animationPredicate(AnimationTestEvent<E> event) {
        if (this.getAttackPhase() != 0) {
            if (this.getAttackPhase() == 1) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.kraken.attack").addAnimation("animation.kraken.reelin").addAnimation("animation.kraken.reelin2"));
            } else if (this.getAttackPhase() == 2) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.kraken.reelin2"));
            } else if (this.getAttackPhase() == 3) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.kraken.bite").addAnimation("animation.kraken.reelin2"));
            }
        } else {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.kraken.swim"));
        }

        return true;
    }

    /*private <E extends Entity> SoundEvent soundListener(SoundKeyframeEvent<E> event) {
        if (event.sound.equals("bite")) {
            damageEntity(this);
            System.out.println(this);
            return SoundEvents.ENTITY_HORSE_EAT;
        } else {
            return null;
        }
    }*/

    @Override
    public EntityAnimationManager getAnimationManager() {
        return manager;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.registerAttributes()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.1D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, OutvotedConfig.COMMON.healthkraken.get());
    }

    protected void registerGoals() {
        MoveTowardsRestrictionGoal movetowardsrestrictiongoal = new MoveTowardsRestrictionGoal(this, 1.0D);
        this.wander = new RandomWalkingGoal(this, 1.0D, 80);
        this.goalSelector.addGoal(3, new FollowBoatGoal(this));
        this.goalSelector.addGoal(4, new KrakenEntity.AttackGoal(this));
        this.goalSelector.addGoal(5, movetowardsrestrictiongoal);
        this.goalSelector.addGoal(7, this.wander);
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.wander.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        movetowardsrestrictiongoal.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigator createNavigator(World worldIn) {
        return new SwimmerPathNavigator(this, worldIn);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(MOVING, false);
        this.dataManager.register(TARGET_ENTITY, 0);
        this.dataManager.register(ATTACKING, 0);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    public boolean isMoving() {
        return this.dataManager.get(MOVING);
    }

    private void setMoving(boolean moving) {
        this.dataManager.set(MOVING, moving);
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
        return this.isInWaterOrBubbleColumn() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isInWaterOrBubbleColumn() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound() {
        return this.isInWaterOrBubbleColumn() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.5F;
    }

    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        return worldIn.getFluidState(pos).isTagged(FluidTags.WATER) ? 10.0F + worldIn.getBrightness(pos) - 0.5F : super.getBlockPathWeight(pos, worldIn);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick() {
        if (this.isAlive()) {
            if (this.world.isRemote) {
                if (!this.isInWater()) {
                    Vector3d vector3d = this.getMotion();
                    if (vector3d.y > 0.0D && this.clientSideTouchedGround && !this.isSilent()) {
                        this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), this.getFlopSound(), this.getSoundCategory(), 1.0F, 1.0F, false);
                    }

                    this.clientSideTouchedGround = vector3d.y < 0.0D && this.world.isTopSolid(this.getPosition().down(), this);
                }

                if (this.isMoving() && this.isInWater()) {
                    Vector3d vector3d1 = this.getLook(0.0F);

                    for (int i = 0; i < 2; ++i) {
                        this.world.addParticle(ParticleTypes.BUBBLE, this.getPosXRandom(0.5D) - vector3d1.x * 1.5D, this.getPosYRandom() - vector3d1.y * 1.5D, this.getPosZRandom(0.5D) - vector3d1.z * 1.5D, 0.0D, 0.0D, 0.0D);
                    }
                }
                if (this.hasTargetedEntity()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        ++this.clientSideAttackTime;
                    }
                }
            }
            if (this.hasTargetedEntity()) {
                LivingEntity livingentity = this.getTargetedEntity();
                if (livingentity != null) {
                    this.getLookController().setLookPositionWithEntity(livingentity, 90.0F, 90.0F);
                    this.getLookController().tick();
                    double d5 = (double) this.getAttackAnimationScale(0.0F);
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
                        //System.out.println(this.getDistance(livingentity));
                        d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
                        this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() + d0 * d4, this.getPosYEye() + d1 * d4, this.getPosZ() + d2 * d4, 0.0D, 0.0D, 0.0D);
                        livingentity.setLocationAndAngles(this.getPosX() + d0 * d3, this.getPosYEye() + d1, this.getPosZ() + d2 * d3, livingentity.rotationYaw, livingentity.rotationPitch);
                        livingentity.setSwimming(false);
                        livingentity.updateSwimming();
                        if (!this.world.isRemote) {
                            if (livingentity.isPassenger() && livingentity.getLowestRidingEntity() instanceof BoatEntity) {
                                BoatEntity boat = (BoatEntity) livingentity.getLowestRidingEntity();
                                boat.remove();
                                livingentity.stopRiding();
                                boat.entityDropItem(boat.getItemBoat());
                            }
                        }
                        if (this.controller.getCurrentAnimation() != null) {
                            if (this.controller.getCurrentAnimation().animationName.equals("animation.kraken.reelin")) {
                                livingentity.addVelocity(-d0 / 60, 0.0D, -d2 / 60);
                            } else if (!this.controller.getCurrentAnimation().animationName.equals("animation.kraken.swim")) {
                                livingentity.addVelocity(-d0 / 20, 0.0D, -d2 / 20);
                            }
                        }
                    }
                }
            }

            if (this.isInWaterOrBubbleColumn()) {
                this.setAir(300);
            } else if (this.onGround) {
                this.setMotion(this.getMotion().add((double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F), 0.5D, (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F)));
                this.rotationYaw = this.rand.nextFloat() * 360.0F;
                this.onGround = false;
                this.isAirBorne = true;
            }

            if (this.hasTargetedEntity()) {
                this.rotationYaw = this.rotationYawHead;
            }
        }

        super.livingTick();
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    public float getAttackAnimationScale(float p_175477_1_) {
        return ((float) this.clientSideAttackTime + p_175477_1_) / (float) this.getAttackDuration();
    }

    public boolean isNotColliding(IWorldReader worldIn) {
        return worldIn.checkNoEntityCollision(this);
    }

    public static boolean func_223329_b(EntityType<? extends KrakenEntity> p_223329_0_, IWorld p_223329_1_, SpawnReason reason, BlockPos p_223329_3_, Random p_223329_4_) {
        return (p_223329_4_.nextInt(20) == 0 || !p_223329_1_.canBlockSeeSky(p_223329_3_)) && p_223329_1_.getDifficulty() != Difficulty.PEACEFUL && (reason == SpawnReason.SPAWNER || p_223329_1_.getFluidState(p_223329_3_).isTagged(FluidTags.WATER));
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

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed() {
        return 180;
    }

    public void travel(Vector3d travelVector) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.9D));
            if (!this.isMoving() && this.getAttackTarget() == null) {
                this.setMotion(this.getMotion().add(0.0D, -0.001D, 0.0D));
            } else if (this.getAttackTarget() != null) {
                this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
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
            return livingentity != null && livingentity.isAlive() && (livingentity.isInWater() || (livingentity.getRidingEntity() != null && livingentity.getRidingEntity().isInWater())) && this.entity.getDistance(this.entity.getAttackTarget()) < 8.0D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && this.entity.getDistance(this.entity.getAttackTarget()) < 8.0D;
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
            this.entity.setTargetedEntity(0);
            this.entity.setAttackTarget((LivingEntity) null);
            this.entity.wander.makeUpdate();
            this.entity.setAttacking(0);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.entity.getAttackTarget();
            this.entity.getNavigator().clearPath();
            this.entity.getLookController().setLookPositionWithEntity(livingentity, 90.0F, 90.0F);
            if (!this.entity.canEntityBeSeen(livingentity)) {
                this.entity.setAttackTarget((LivingEntity) null);
            } else {
                ++this.tickCounter;
                if (this.tickCounter == 0) {
                    this.entity.setTargetedEntity(this.entity.getAttackTarget().getEntityId());
                    /*if (!this.entity.isSilent()) {
                        this.entity.world.setEntityState(this.entity, (byte) 21);
                    }*/
                } else if (this.tickCounter >= this.entity.getAttackDuration()) {
                    float f = 1.0F;
                    if (this.entity.world.getDifficulty() == Difficulty.HARD) {
                        f += 2.0F;
                    }

                    if (this.tickCounter % 50 == 0) {
                        livingentity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.entity, this.entity), f);
                        if (livingentity.getAir() - 50 > 0) {
                            livingentity.setAir(livingentity.getAir() - 50);
                        }
                    }/* else if (this.tickCounter % 25 == 0) {
                        this.entity.setAttacking(2);
                    }
                    this.entity.setAttackTarget((LivingEntity) null);
                    this.entity.setAttacking(0);*/
                } else {
                    this.entity.setAttacking(1);
                }

                super.tick();
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

                //this.entity.getLookController().setLookPosition(MathHelper.lerp(0.125D, d11, d8), MathHelper.lerp(0.125D, d12, d9), MathHelper.lerp(0.125D, d13, d10), 10.0F, 40.0F);
                double setY = MathHelper.lerp(0.125D, d12, d9);
                if (setY < 55) {
                    setY += 5;
                } else if (setY > 60) {
                    setY -= 5;
                }
                this.entity.getLookController().setLookPosition(MathHelper.lerp(0.125D, d11, d8), setY, MathHelper.lerp(0.125D, d13, d10), 10.0F, 40.0F);
                this.entity.setMoving(true);
            } else {
                this.entity.setAIMoveSpeed(0.0F);
                this.entity.setMoving(false);
            }
        }
    }
}
