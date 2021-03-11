package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static java.lang.Math.*;

public class WildfireEntity extends MonsterEntity implements IAnimatable {
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private boolean shieldDisabled = false;
    private static final DataParameter<Boolean> SHIELDING = EntityDataManager.defineId(WildfireEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> ON_FIRE = EntityDataManager.defineId(WildfireEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.defineId(WildfireEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(WildfireEntity.class, DataSerializers.INT);


    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animname = event.getController().getCurrentAnimation() != null ? event.getController().getCurrentAnimation().animationName : "";

        if (event.getController().getAnimationState().equals(AnimationState.Stopped) || !animname.equals("attack")) {
            if (this.getAttacking()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack"));
            } else if (this.getShielding()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("shieldtransition").addAnimation("shielding"));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("generaltransition").addAnimation("general"));
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public WildfireEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.LAVA, 8.0F);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.xpReward = 20;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 4.0D)
                .add(Attributes.MAX_HEALTH, OutvotedConfig.COMMON.healthwildfire.get())
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new WildfireEntity.AttackGoal(this));
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.WILDFIRE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.WILDFIRE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.WILDFIRE_HURT.get();
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.8F;
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        int type;
        Block block = worldIn.getBlockState(new BlockPos(this.position().add(0D, -0.5D, 0D))).getBlock();
        if (block.is(Blocks.SOUL_SAND) || block.is(Blocks.SOUL_SOIL)) {
            type = 1;
        } else {
            type = 0;
        }
        this.setVariant(type);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHIELDING, Boolean.FALSE);
        this.entityData.define(ATTACKING, Boolean.FALSE);
        this.entityData.define(ON_FIRE, (byte) 0);
        this.entityData.define(VARIANT, 0);
    }

    public void setShielding(boolean shielding) {
        if (!this.shieldDisabled) {
            this.entityData.set(SHIELDING, shielding);
        } else {
            this.entityData.set(SHIELDING, false);
        }
    }

    public boolean getShielding() {
        return this.entityData.get(SHIELDING) && !this.shieldDisabled;
    }

    public void setVariant(int type) {
        this.entityData.set(VARIANT, type);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public float getBrightness() {
        return 1.0F;
    }

    public void aiStep() {
        if (!this.onGround && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

        if (this.level.isClientSide) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.level.playLocalSound(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D, ModSounds.WILDFIRE_BURN.get(), this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                //this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }

        }
        if (this.getShielding()) {
            this.level.addParticle(ParticleTypes.LAVA, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
        }
        if (this.getAttacking()) {
            for (int particlei = 0; particlei < 16; ++particlei) {
                this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(0.75D), this.getRandomY(), this.getRandomZ(0.75D), 0.0D, 0.0D, 0.0D);
            }
        }

        super.aiStep();
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    protected void customServerAiStep() {
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.random.nextGaussian() * (3 / ((this.getHealth() / 25) + 1));
        }

        LivingEntity livingentity = this.getTarget();
        if (livingentity != null && livingentity.getEyeY() > this.getEyeY() + (double) this.heightOffset && this.canAttack(livingentity)) {
            Vector3d vector3d = this.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, ((double) 0.3F - vector3d.y) * (double) 0.3F, 0.0D));
            this.hasImpulse = true;
        }

        super.customServerAiStep();
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     * Copied from BlazeEntity.java
     */
    public boolean isOnFire() {
        return this.isCharged();
    }

    private boolean isCharged() {
        return (this.entityData.get(ON_FIRE) & 1) != 0;
    }

    private void setOnFire(boolean onFire) {
        byte b0 = this.entityData.get(ON_FIRE);
        if (onFire) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(ON_FIRE, b0);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide) {
            if (source.getDirectEntity() instanceof LivingEntity && this.isInvulnerable()) {
                LivingEntity entity = (LivingEntity) source.getDirectEntity();
                // Shield disabling on critical axe hit
                if (entity.getMainHandItem().getItem() instanceof AxeItem) {
                    double itemDamage = ((AxeItem) entity.getMainHandItem().getItem()).getAttackDamage() + 1;
                    if (amount >= itemDamage + (itemDamage / 2)) { // Only disable shields on a critical axe hit
                        this.playSound(SoundEvents.ANVIL_PLACE, 0.3F, 1.5F);
                        this.shieldDisabled = true;
                        this.setShielding(false);
                        this.setInvulnerable(false);
                        return false;
                    }
                }
            }
            if (this.isInvulnerableTo(source)) {
                this.playSound(SoundEvents.ANVIL_PLACE, 0.3F, 0.5F);
                if (source.isProjectile()) {
                    source.getDirectEntity().setSecondsOnFire(12);
                } else if (source.getDirectEntity() != null) {
                    source.getDirectEntity().setSecondsOnFire(8);
                }

                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if ((source == DamageSource.GENERIC || source instanceof EntityDamageSource) && !source.isCreativePlayer()) {
            return this.isInvulnerable();
        } else {
            return false;
        }
    }

    static class AttackGoal extends Goal {
        private final WildfireEntity wildfire;
        private int attackStep;
        private int attackTime;
        private int firedRecentlyTimer;

        public AttackGoal(WildfireEntity wildfireIn) {
            this.wildfire = wildfireIn;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.wildfire.getTarget();
            return livingentity != null && livingentity.isAlive() && this.wildfire.canAttack(livingentity);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.attackStep = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.wildfire.setOnFire(false);
            this.wildfire.setShielding(false);
            this.wildfire.setAttacking(false);
            this.firedRecentlyTimer = 0;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.attackTime;
            LivingEntity livingentity = this.wildfire.getTarget();
            this.wildfire.setAttacking(false);
            if (livingentity != null) {
                boolean flag = this.wildfire.getSensing().canSee(livingentity);
                if (flag) {
                    this.firedRecentlyTimer = 0;
                } else {
                    ++this.firedRecentlyTimer;
                }

                double d0 = this.wildfire.distanceToSqr(livingentity);
                if (d0 < 4.0D) {

                    this.wildfire.setOnFire(true);

                    if (this.attackTime <= 0) {
                        this.wildfire.setAttacking(true);
                        this.attackTime = 5;
                        this.wildfire.doHurtTarget(livingentity);
                        livingentity.setSecondsOnFire(4);
                    }

                    this.wildfire.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                    double d1 = livingentity.getX() - this.wildfire.getX();
                    double d2 = livingentity.getY(0.5D) - this.wildfire.getY(0.5D);
                    double d3 = livingentity.getZ() - this.wildfire.getZ();

                    float health = (this.wildfire.getMaxHealth() - this.wildfire.getHealth()) / 2;
                    float healthPercent = this.wildfire.getHealth() / this.wildfire.getMaxHealth();

                    int maxAttackSteps = 3;

                    if (d0 < 36.0D) {
                        ++maxAttackSteps;
                    }
                    if (healthPercent < 0.6) {
                        ++maxAttackSteps;
                    }

                    if (this.attackTime <= 0) {
                        this.wildfire.setShielding(false);
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = (int) (40 * healthPercent + 20);
                            this.wildfire.setOnFire(true);
                        } else if (this.attackStep <= maxAttackSteps) {
                            this.attackTime = (int) (25 * healthPercent + 5);
                        } else {
                            this.attackTime = 200;
                            this.attackStep = 0;
                            this.wildfire.setOnFire(false);
                            this.wildfire.setAttacking(false);
                        }

                        if (this.attackStep > 1) {

                            this.wildfire.setAttacking(true);

                            if (!this.wildfire.isSilent()) {
                                this.wildfire.level.playSound(null, this.wildfire.blockPosition(), ModSounds.WILDFIRE_SHOOT.get(), this.wildfire.getSoundSource(), 1.0F, 1.0F);
                            }

                            double fireballcount = OutvotedConfig.COMMON.fireballcount.get();
                            double offsetangle = toRadians(OutvotedConfig.COMMON.offsetangle.get());
                            double maxdepressangle = toRadians(OutvotedConfig.COMMON.maxdepressangle.get());

                            //update target pos
                            d1 = livingentity.getX() - this.wildfire.getX();
                            d2 = livingentity.getY(0.5D) - this.wildfire.getY(0.5D);
                            d3 = livingentity.getZ() - this.wildfire.getZ();

                            //shoot fireballs
                            for (int i = 0; i <= (fireballcount - 1); ++i) {
                                WildfireFireballEntity wildfirefireballentity;
                                double angle = (i - ((fireballcount - 1) / 2)) * offsetangle;
                                double x = d1 * cos(angle) + d3 * sin(angle);
                                double y = d2;
                                double z = -d1 * sin(angle) + d3 * cos(angle);
                                if (abs((atan2(d2, sqrt((d1 * d1) + (d3 * d3))))) > maxdepressangle) {
                                    y = -tan(maxdepressangle) * (sqrt((d1 * d1) + (d3 * d3)));
                                }
                                wildfirefireballentity = new WildfireFireballEntity(this.wildfire.level, this.wildfire, x, y, z);
                                wildfirefireballentity.setPos(wildfirefireballentity.getX(), this.wildfire.getY(0.5D), wildfirefireballentity.getZ());
                                this.wildfire.level.addFreshEntity(wildfirefireballentity);
                            }
                        }
                    } else if (this.attackTime < 160 + health && this.attackTime > 90 - health) {
                        this.wildfire.setShielding(true);
                    } else if (this.attackTime >= 30 && this.attackTime >= 50) {
                        this.wildfire.setShielding(false);
                        this.wildfire.shieldDisabled = false;
                    }

                    this.wildfire.setInvulnerable(this.wildfire.getShielding());

                    this.wildfire.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
                } else if (this.firedRecentlyTimer < 5) {
                    this.wildfire.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.wildfire.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}
