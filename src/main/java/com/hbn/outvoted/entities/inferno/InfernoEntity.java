package com.hbn.outvoted.entities.inferno;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.animation.controller.EntityAnimationController;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.event.AnimationTestEvent;
import software.bernie.geckolib.manager.EntityAnimationManager;

import java.util.EnumSet;

public class InfernoEntity extends MonsterEntity implements IAnimatedEntity {
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private static final DataParameter<Boolean> SHIELDING = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.BOOLEAN);


    EntityAnimationManager manager = new EntityAnimationManager();
    EntityAnimationController controller = new EntityAnimationController(this, "controller", 5, this::animationPredicate);

    public <E extends Entity> boolean animationPredicate(AnimationTestEvent<E> event) {
        if (this.shielding()) {
            controller.transitionLengthTicks = 5;
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.inferno.shield").addAnimation("animation.inferno.shield2"));
        } else {
            controller.transitionLengthTicks = 1;
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.inferno.generaltran").addAnimation("animation.inferno.general"));

        }

        return true;
    }

    public InfernoEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        manager.addAnimationController(controller);
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
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
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
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SHIELDING, Boolean.FALSE);
    }

    public void shielding(boolean shielding) {
        this.dataManager.set(SHIELDING, shielding);
    }

    public boolean shielding() {
        return this.dataManager.get(SHIELDING);
    }

    /*@Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Shielded", this.shielding());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.shielding(compound.getBoolean("Shielded"));
    }*/


    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return manager;
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
                this.world.addParticle(ParticleTypes.SMOKE, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }
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
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
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

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            //this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.5F);
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.3F, 0.5F);
            return false;
        }
        return super.attackEntityFrom(source, amount);

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
                if (d0 < 7.0D) {
                    /*if (!flag) {
                        return;
                    }*/

                    if (this.attackTime <= 0) {
                        this.attackTime = 2;
                        this.blaze.attackEntityAsMob(livingentity);
                    }

                    this.blaze.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                    //double d1 = livingentity.getPosX() - this.blaze.getPosX();
                    double d2 = livingentity.getPosYHeight(0.5D) - this.blaze.getPosYHeight(0.5D);
                    //double d3 = livingentity.getPosZ() - this.blaze.getPosZ();

                    float health = (this.blaze.getMaxHealth() - this.blaze.getHealth()) / 2;
                    if (this.attackTime <= 0) {
                        this.blaze.shielding(false);
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = 60;
                        } else if (this.attackStep <= 3) {
                            this.attackTime = 30;
                        } else {
                            this.attackTime = 175;
                            this.attackStep = 0;
                        }

                        if (this.attackStep > 1) {
                            float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                            if (!this.blaze.isSilent()) {
                                this.blaze.world.playEvent((PlayerEntity) null, 1018, this.blaze.getPosition(), 0);
                            }

                            for (int i = 0; i < 10; ++i) {
                                for (int j = 0; j < 4; ++j) {
                                    SmallFireballEntity smallfireballentity;
                                    if (j == 0) {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, (i * 36), d2, 360 - (i * 36));
                                    } else if (j == 1) {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, -(i * 36), d2, 360 - (i * 36));
                                    } else if (j == 2) {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, (i * 36), d2, -360 + (i * 36));
                                    } else {
                                        smallfireballentity = new SmallFireballEntity(this.blaze.world, this.blaze, -(i * 36), d2, -360 + (i * 36));
                                    }
                                    smallfireballentity.setPosition(smallfireballentity.getPosX(), this.blaze.getPosYHeight(0.5D), smallfireballentity.getPosZ());
                                    this.blaze.world.addEntity(smallfireballentity);

                                }
                            }
                        }
                    } else if (this.attackTime < 150 + health && this.attackTime > 100 - health) {
                        this.blaze.shielding(true);
                    } else if (this.attackTime >= 30 && this.attackTime >= 50) {
                        this.blaze.shielding(false);
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
