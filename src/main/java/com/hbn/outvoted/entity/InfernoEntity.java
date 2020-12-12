package com.hbn.outvoted.entity;

import com.hbn.outvoted.config.OutvotedConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;

public class InfernoEntity extends MonsterEntity implements IAnimatable {
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private boolean shieldDisabled = false;
    private static final DataParameter<Boolean> SHIELDING = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> ON_FIRE = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.VARINT);


    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.shielding()) {
            event.getController().transitionLengthTicks = 5;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.shield").addAnimation("animation.inferno.shield2"));
        } else {
            event.getController().transitionLengthTicks = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.generaltran").addAnimation("animation.inferno.general"));
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

    public InfernoEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.experienceValue = 20;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.registerAttributes()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 4.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, OutvotedConfig.COMMON.healthinferno.get())
                .createMutableAttribute(Attributes.ARMOR, 10.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new InfernoEntity.AttackGoal(this));
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BLAZE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.8F;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SHIELDING, Boolean.FALSE);
        this.dataManager.register(ON_FIRE, (byte) 0);
        this.dataManager.register(VARIANT, 2);
    }

    public void shielding(boolean shielding) {
        if (!this.shieldDisabled) {
            this.dataManager.set(SHIELDING, shielding);
        } else {
            this.dataManager.set(SHIELDING, false);
        }
    }

    public boolean shielding() {
        return this.dataManager.get(SHIELDING) && !this.shieldDisabled;
    }

    public int variant() {
        if (this.dataManager.get(VARIANT) == 2) {
            Block block = this.world.getBlockState(new BlockPos(this.getPositionVec().add(0D, -0.1D, 0D))).getBlock();
            if (block.matchesBlock(Blocks.SOUL_SAND) || block.matchesBlock(Blocks.SOUL_SOIL)) {
                this.dataManager.set(VARIANT, 1);
            } else {
                this.dataManager.set(VARIANT, 0);
            }
        }
        return this.dataManager.get(VARIANT);
    }

    public float getBrightness() {
        return 1.0F;
    }

    public void livingTick() {
        if (!this.onGround && this.getMotion().y < 0.0D) {
            this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }

        if (this.world.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.getPosX() + 0.5D, this.getPosY() + 0.5D, this.getPosZ() + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                //this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }

        }
        if (this.shielding()) {
            this.world.addParticle(ParticleTypes.LAVA, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
        }

        super.livingTick();
    }

    public boolean isWaterSensitive() {
        return true;
    }

    protected void updateAITasks() {
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * (3 / ((this.getHealth() / 25) + 1));
        }

        LivingEntity livingentity = this.getAttackTarget();
        if (livingentity != null && livingentity.getPosYEye() > this.getPosYEye() + (double) this.heightOffset && this.canAttack(livingentity)) {
            Vector3d vector3d = this.getMotion();
            this.setMotion(this.getMotion().add(0.0D, ((double) 0.3F - vector3d.y) * (double) 0.3F, 0.0D));
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     * Copied from BlazeEntity.java
     */
    public boolean isBurning() {
        return this.isCharged();
    }

    private boolean isCharged() {
        return (this.dataManager.get(ON_FIRE) & 1) != 0;
    }

    private void setOnFire(boolean onFire) {
        byte b0 = this.dataManager.get(ON_FIRE);
        if (onFire) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(ON_FIRE, b0);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.world.isRemote) {
            if (source.getImmediateSource() instanceof LivingEntity && this.isInvulnerable()) {
                LivingEntity entity = (LivingEntity) source.getImmediateSource();
                if (entity.getHeldItemMainhand().getItem() instanceof AxeItem) {
                    double itemDamage = ((AxeItem) entity.getHeldItemMainhand().getItem()).getAttackDamage() + 1;
                    if (amount == itemDamage + (itemDamage / 2)) { // Only disable shields on a critical axe hit
                        this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.3F, 1.5F);
                        this.shieldDisabled = true;
                        this.shielding(false);
                        this.setInvulnerable(false);
                        return false;
                    }
                }
            }
            if (this.isInvulnerableTo(source)) {
                this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.3F, 0.5F);

                if (source.isProjectile()) {
                    source.getImmediateSource().setFire(12);
                } else if (source.getImmediateSource() != null) {
                    source.getImmediateSource().setFire(8);
                }

                return false;
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source == DamageSource.DROWN) {
            return false;
        }
        return super.isInvulnerableTo(source);
    }

    static class AttackGoal extends Goal {
        private final InfernoEntity blaze;
        private int attackStep;
        private int attackTime;
        private int firedRecentlyTimer;

        public AttackGoal(InfernoEntity blazeIn) {
            this.blaze = blazeIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = this.blaze.getAttackTarget();
            return livingentity != null && livingentity.isAlive() && this.blaze.canAttack(livingentity);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.attackStep = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.blaze.setOnFire(false);
            this.firedRecentlyTimer = 0;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.attackTime;
            LivingEntity livingentity = this.blaze.getAttackTarget();
            if (livingentity != null) {
                boolean flag = this.blaze.getEntitySenses().canSee(livingentity);
                if (flag) {
                    this.firedRecentlyTimer = 0;
                } else {
                    ++this.firedRecentlyTimer;
                }

                double d0 = this.blaze.getDistanceSq(livingentity);
                if (d0 < 4.0D) {

                    this.blaze.setOnFire(true);

                    if (this.attackTime <= 0) {
                        this.attackTime = 5;
                        this.blaze.attackEntityAsMob(livingentity);
                        livingentity.setFire(4);
                    }

                    this.blaze.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                    double d2 = livingentity.getPosYHeight(0.5D) - this.blaze.getPosYHeight(0.5D);

                    float health = (this.blaze.getMaxHealth() - this.blaze.getHealth()) / 2;
                    float healthPercent = this.blaze.getHealth() / this.blaze.getMaxHealth();

                    int maxAttackSteps = 3;

                    if (d0 < 36.0D) {
                        ++maxAttackSteps;
                    }
                    if (healthPercent < 0.6) {
                        ++maxAttackSteps;
                    }

                    if (this.attackTime <= 0) {
                        this.blaze.shielding(false);
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            //this.attackTime = 60;
                            this.attackTime = (int) (40 * healthPercent + 20);
                            this.blaze.setOnFire(true);
                        } else if (this.attackStep <= maxAttackSteps) {
                            //this.attackTime = 30;
                            this.attackTime = (int) (25 * healthPercent + 5);
                        } else {
                            this.attackTime = 200;
                            this.attackStep = 0;
                            this.blaze.setOnFire(false);
                        }

                        if (this.attackStep > 1) {
                            float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                            if (!this.blaze.isSilent()) {
                                this.blaze.world.playEvent((PlayerEntity) null, 1018, this.blaze.getPosition(), 0);
                            }

                            int offset = ((36 / (maxAttackSteps - 1)) * (attackStep - 2));

                            //shoot fireballs in circle
                            for (int i = 0; i < 10; ++i) {
                                for (int j = 0; j < 4; ++j) {
                                    SmallFireballEntity smallfireballentity;
                                    if (j == 0) {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, (i * 36 + offset), d2, 360 - (i * 36 + offset));
                                    } else if (j == 1) {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, -(i * 36 + offset), d2, 360 - (i * 36 + offset));
                                    } else if (j == 2) {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, (i * 36 + offset), d2, -360 + (i * 36 + offset));
                                    } else {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, -(i * 36 + offset), d2, -360 + (i * 36 + offset));
                                    }
                                    smallfireballentity.setPosition(smallfireballentity.getPosX(), this.blaze.getPosYHeight(0.5D), smallfireballentity.getPosZ());
                                    this.blaze.world.addEntity(smallfireballentity);

                                }
                            }
                        }
                    } else if (this.attackTime < 160 + health && this.attackTime > 90 - health) {
                        this.blaze.shielding(true);
                    } else if (this.attackTime >= 30 && this.attackTime >= 50) {
                        this.blaze.shielding(false);
                        this.blaze.shieldDisabled = false;
                    }

                    this.blaze.setInvulnerable(this.blaze.shielding());

                    this.blaze.getLookController().setLookPositionWithEntity(livingentity, 10.0F, 10.0F);
                } else if (this.firedRecentlyTimer < 5) {
                    this.blaze.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}
