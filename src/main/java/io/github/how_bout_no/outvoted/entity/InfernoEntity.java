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
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
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

public class InfernoEntity extends MonsterEntity implements IAnimatable {
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private boolean shieldDisabled = false;
    private static final DataParameter<Boolean> SHIELDING = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> ON_FIRE = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(InfernoEntity.class, DataSerializers.VARINT);


    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animname = event.getController().getCurrentAnimation() != null ? event.getController().getCurrentAnimation().animationName : "";

        if (event.getController().getAnimationState().equals(AnimationState.Stopped)){
            if (this.getAttacking()){
                event.getController().transitionLengthTicks = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.attack"));
            }
            else if (this.getShielding()){
                event.getController().transitionLengthTicks = 5;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.shield").addAnimation("animation.inferno.shield2"));
            }
            else {
                event.getController().transitionLengthTicks = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.generaltran").addAnimation("animation.inferno.general"));
            }

        }
        else if (!animname.equals("animation.inferno.attack")){
            if (this.getAttacking()){
                event.getController().transitionLengthTicks = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.attack"));
            }
            else if (this.getShielding()){
                event.getController().transitionLengthTicks = 5;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.shield").addAnimation("animation.inferno.shield2"));
            }
            else {
                event.getController().transitionLengthTicks = 1;
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.inferno.generaltran").addAnimation("animation.inferno.general"));
            }
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
        return ModSounds.INFERNO_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.INFERNO_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.INFERNO_HURT.get();
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.8F;
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        int type;
        Block block = this.world.getBlockState(new BlockPos(this.getPositionVec().add(0D, -0.5D, 0D))).getBlock();
        if (block.matchesBlock(Blocks.SOUL_SAND) || block.matchesBlock(Blocks.SOUL_SOIL)) {
            type = 1;
        } else {
            type = 0;
        }
        this.setVariant(type);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SHIELDING, Boolean.FALSE);
        this.dataManager.register(ATTACKING, Boolean.FALSE);
        this.dataManager.register(ON_FIRE, (byte) 0);
        this.dataManager.register(VARIANT, 0);
    }

    public void setShielding(boolean shielding) {
        if (!this.shieldDisabled) {
            this.dataManager.set(SHIELDING, shielding);
        } else {
            this.dataManager.set(SHIELDING, false);
        }
    }

    public boolean getShielding() {
        return this.dataManager.get(SHIELDING) && !this.shieldDisabled;
    }

    public void setVariant(int type) {
        this.dataManager.set(VARIANT, type);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    public void setAttacking(boolean attacking) {
        this.dataManager.set(ATTACKING, attacking);
    }

    public boolean getAttacking() {
        return this.dataManager.get(ATTACKING);
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
                this.world.playSound(this.getPosX() + 0.5D, this.getPosY() + 0.5D, this.getPosZ() + 0.5D, ModSounds.INFERNO_BURN.get(), this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                //this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }

        }
        if (this.getShielding()) {
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
                    if (amount >= itemDamage + (itemDamage / 2)) { // Only disable shields on a critical axe hit
                        this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.3F, 1.5F);
                        this.shieldDisabled = true;
                        this.setShielding(false);
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
        private final InfernoEntity inferno;
        private int attackStep;
        private int attackTime;
        private int firedRecentlyTimer;

        public AttackGoal(InfernoEntity infernoIn) {
            this.inferno = infernoIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = this.inferno.getAttackTarget();
            return livingentity != null && livingentity.isAlive() && this.inferno.canAttack(livingentity);
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
            this.inferno.setOnFire(false);
            this.inferno.setShielding(false);
            this.inferno.setAttacking(false);
            this.firedRecentlyTimer = 0;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.attackTime;
            LivingEntity livingentity = this.inferno.getAttackTarget();
            this.inferno.setAttacking(false);
            if (livingentity != null) {
                boolean flag = this.inferno.getEntitySenses().canSee(livingentity);
                if (flag) {
                    this.firedRecentlyTimer = 0;
                } else {
                    ++this.firedRecentlyTimer;
                }

                double d0 = this.inferno.getDistanceSq(livingentity);
                if (d0 < 4.0D) {

                    this.inferno.setOnFire(true);

                    if (this.attackTime <= 0) {
                        this.inferno.setAttacking(true);
                        this.attackTime = 5;
                        this.inferno.attackEntityAsMob(livingentity);
                        livingentity.setFire(4);
                    }

                    this.inferno.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                    double d1 = livingentity.getPosX() - this.inferno.getPosX();
                    double d2 = livingentity.getPosYHeight(0.5D) - this.inferno.getPosYHeight(0.5D);
                    double d3 = livingentity.getPosZ() - this.inferno.getPosZ();

                    float health = (this.inferno.getMaxHealth() - this.inferno.getHealth()) / 2;
                    float healthPercent = this.inferno.getHealth() / this.inferno.getMaxHealth();

                    int maxAttackSteps = 3;

                    if (d0 < 36.0D) {
                        ++maxAttackSteps;
                    }
                    if (healthPercent < 0.6) {
                        ++maxAttackSteps;
                    }

                    if (this.attackTime <= 0) {
                        this.inferno.setShielding(false);
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = (int) (40 * healthPercent + 20);
                            this.inferno.setOnFire(true);
                        } else if (this.attackStep <= maxAttackSteps) {
                            this.attackTime = (int) (25 * healthPercent + 5);
                        } else {
                            this.attackTime = 200;
                            this.attackStep = 0;
                            this.inferno.setOnFire(false);
                            this.inferno.setAttacking(false);
                        }

                        if (this.attackStep > 1) {

                            this.inferno.setAttacking(true);

                            if (!this.inferno.isSilent()) {
                                this.inferno.world.playSound(null, this.inferno.getPosition(), ModSounds.INFERNO_SHOOT.get(), this.inferno.getSoundCategory(), 1.0F, 1.0F);
                            }

                            double fireballcount = OutvotedConfig.COMMON.fireballcount.get();
                            double offsetangle = toRadians(OutvotedConfig.COMMON.offsetangle.get());
                            double maxdepressangle = toRadians(OutvotedConfig.COMMON.maxdepressangle.get());

                            //update target pos
                            d1 = livingentity.getPosX() - this.inferno.getPosX();
                            d2 = livingentity.getPosYHeight(0.5D) - this.inferno.getPosYHeight(0.5D);
                            d3 = livingentity.getPosZ() - this.inferno.getPosZ();

                            //shoot fireballs
                            for (int i = 0; i <= (fireballcount - 1); ++i) {
                                SmallFireballEntity smallfireballentity;
                                double angle = (i - ((fireballcount - 1) / 2)) * offsetangle;
                                double x = d1 * cos(angle) + d3 * sin(angle);
                                double y = d2;
                                double z = -d1 * sin(angle) + d3 * cos(angle);
                                if (abs((atan2(d2, sqrt((d1 * d1) + (d3 * d3))))) > maxdepressangle) {
                                    y = -tan(maxdepressangle) * (sqrt((d1 * d1) + (d3 * d3)));
                                }
                                smallfireballentity = new SmallFireballEntity(this.inferno.world, this.inferno, x, y, z);
                                smallfireballentity.setPosition(smallfireballentity.getPosX(), this.inferno.getPosYHeight(0.5D), smallfireballentity.getPosZ());
                                this.inferno.world.addEntity(smallfireballentity);
                            }
                        }
                    } else if (this.attackTime < 160 + health && this.attackTime > 90 - health) {
                        this.inferno.setShielding(true);
                    } else if (this.attackTime >= 30 && this.attackTime >= 50) {
                        this.inferno.setShielding(false);
                        this.inferno.shieldDisabled = false;
                    }

                    this.inferno.setInvulnerable(this.inferno.getShielding());

                    this.inferno.getLookController().setLookPositionWithEntity(livingentity, 10.0F, 10.0F);
                } else if (this.firedRecentlyTimer < 5) {
                    this.inferno.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.inferno.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}
