package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModSounds;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.util.IMixinBlaze;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

import static java.lang.Math.*;

public class Wildfire extends Monster implements IAnimatable {
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private boolean shieldDisabled = false;
    private static final EntityDataAccessor<Boolean> SHIELDING;
    private static final EntityDataAccessor<Byte> ON_FIRE;
    private static final EntityDataAccessor<Boolean> ATTACKING;
    private static final EntityDataAccessor<Integer> VARIANT;

    public Wildfire(EntityType<? extends Wildfire> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.xpReward = 20;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AttackGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.ATTACK_KNOCKBACK, 4)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.115)
                .add(Attributes.FOLLOW_RANGE, 48);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        HealthUtil.setConfigHealth(this, Config.wildfireHealth.get());

        int type = 0;
        if (reason != MobSpawnType.SPAWN_EGG && reason != MobSpawnType.DISPENSER) {
            if (worldIn.getBiome(this.blockPosition()).isBound()) {
                if (worldIn.getBiome(this.blockPosition()).is(Biomes.SOUL_SAND_VALLEY)) {
                    type = 1;
                }
            }
        } else {
            Block block = worldIn.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock();
            if (block == Blocks.SOUL_SAND || block == Blocks.SOUL_SOIL) {
                type = 1;
            }
        }
        this.setVariant(type);

        if (reason == MobSpawnType.NATURAL) {
            ServerLevel serverWorld = worldIn.getLevel();
            int max = switch (difficultyIn.getDifficulty()) {
                case NORMAL -> 3;
                case HARD -> 4;
                default -> 2;
            };
            int min = max - 1;
            int rand = new Random().nextInt(max - min) + min;
            for (int i = 1; i <= rand; i++) {
                Blaze blaze = EntityType.BLAZE.create(serverWorld);
                blaze.absMoveTo(this.getRandomX(3.0D), this.getY(), this.getRandomZ(3.0D), this.getYRot(), this.getXRot());
                while (!serverWorld.isEmptyBlock(blaze.blockPosition())) { // Should prevent spawning inside of blocks
                    blaze.absMoveTo(this.getRandomX(3.0D), this.getY(), this.getRandomZ(3.0D), this.getYRot(), this.getXRot());
                }
                ((IMixinBlaze) blaze).initialize(worldIn, difficultyIn, reason, null, null);
                serverWorld.addFreshEntity(blaze);
            }
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    protected Component getTypeName() {
        return getVariant() == 0 ? super.getTypeName() : new TranslatableComponent("entity.outvoted.wildfire_s");
    }

    public int getMaxSpawnClusterSize() {
        return this.level.getDifficulty() == Difficulty.HARD ? 2 : 1;
    }

    static {
        SHIELDING = SynchedEntityData.defineId(Wildfire.class, EntityDataSerializers.BOOLEAN);
        ON_FIRE = SynchedEntityData.defineId(Wildfire.class, EntityDataSerializers.BYTE);
        ATTACKING = SynchedEntityData.defineId(Wildfire.class, EntityDataSerializers.BOOLEAN);
        VARIANT = SynchedEntityData.defineId(Wildfire.class, EntityDataSerializers.INT);
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

    public void setAggressive(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean getIsAttacking() {
        return this.entityData.get(ATTACKING);
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
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.8F;
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
        if (this.getIsAttacking()) {
            for (int particlei = 0; particlei < 16; ++particlei) {
                this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(0.75D), this.getRandomY(), this.getRandomZ(0.75D), 0.0D, 0.0D, 0.0D);
            }
        }

        super.aiStep();
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    @Nullable
    public ItemEntity spawnAtLocation(ItemStack stack, float yOffset) {
        if (stack.getItem() instanceof WildfireHelmetItem) {
            if (!level.isClientSide() && this.getVariant() == 1) {
                stack.getOrCreateTag().putFloat("SoulTexture", 1.0F);
            }
        }

        return super.spawnAtLocation(stack, yOffset);
    }

    protected void customServerAiStep() {
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.random.nextGaussian() * (3 / ((this.getHealth() / 25) + 1));
        }

        LivingEntity livingentity = this.getTarget();
        if (livingentity != null && livingentity.getEyeY() > this.getEyeY() + (double) this.heightOffset && this.canAttack(livingentity)) {
            Vec3 vector3d = this.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, ((double) 0.3F - vector3d.y) * (double) 0.3F, 0.0D));
            this.hasImpulse = true;
        }

        super.customServerAiStep();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
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

    @Override
    public void setSharedFlagOnFire(boolean onFire) {
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
            if (source.getDirectEntity() instanceof LivingEntity entity && this.isInvulnerable()) {
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
                if (source.getDirectEntity() != null)
                    if (source.isProjectile()) source.getDirectEntity().setSecondsOnFire(12);
                    else source.getDirectEntity().setSecondsOnFire(8);

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
        private final Wildfire mob;
        private int attackStep;
        private int attackTime;
        private int firedRecentlyTimer;

        public AttackGoal(Wildfire wildfireIn) {
            this.mob = wildfireIn;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            return livingentity != null && livingentity.isAlive() && this.mob.canAttack(livingentity);
        }

        public void start() {
            this.attackStep = 0;
        }

        public void stop() {
            this.mob.setSharedFlagOnFire(false);
            this.mob.setShielding(false);
            this.mob.setAggressive(false);
            this.firedRecentlyTimer = 0;
        }

        public void tick() {
            --this.attackTime;
            LivingEntity livingentity = this.mob.getTarget();
            this.mob.setAggressive(false);
            if (livingentity != null) {
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                if (flag) {
                    this.firedRecentlyTimer = 0;
                } else {
                    ++this.firedRecentlyTimer;
                }

                double d0 = this.mob.distanceToSqr(livingentity);
                if (d0 < 4.0D) {
                    this.mob.setSharedFlagOnFire(true);

                    if (this.attackTime <= 0) {
                        this.mob.setAggressive(true);
                        this.attackTime = 5;
                        this.mob.doHurtTarget(livingentity);
                        livingentity.setSecondsOnFire(4);
                    }

                    this.mob.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                    float health = (this.mob.getMaxHealth() - this.mob.getHealth()) / 2;
                    float healthPercent = this.mob.getHealth() / this.mob.getMaxHealth();

                    int maxAttackSteps = 3;

                    if (d0 < 36.0D) {
                        ++maxAttackSteps;
                    }
                    if (healthPercent < 0.6) {
                        ++maxAttackSteps;
                    }

                    if (this.attackTime <= 0) {
                        this.mob.setShielding(false);
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = (int) (40 * healthPercent + 20);
                            this.mob.setSharedFlagOnFire(true);
                        } else if (this.attackStep <= maxAttackSteps) {
                            this.attackTime = (int) (25 * healthPercent + 5);
                        } else {
                            this.attackTime = 200;
                            this.attackStep = 0;
                            this.mob.setSharedFlagOnFire(false);
                            this.mob.setAggressive(false);
                        }

                        if (this.attackStep > 1) {
                            this.mob.setAggressive(true);

                            if (!this.mob.isSilent()) {
                                this.mob.level.playSound(null, this.mob.blockPosition(), ModSounds.WILDFIRE_SHOOT.get(), this.mob.getSoundSource(), 1.0F, 1.0F);
                            }

                            double fireballcount = Config.wildfireFireballCount.get();
                            double offsetangle = toRadians(Config.wildfireOffsetAngle.get());
                            double maxdepressangle = toRadians(Config.wildfireMaxDepressAngle.get());

                            //update target pos
                            double d1 = livingentity.getX() - this.mob.getX();
                            double d2 = livingentity.getY(0.5D) - this.mob.getY(0.5D);
                            double d3 = livingentity.getZ() - this.mob.getZ();

                            //shoot fireballs
                            for (int i = 0; i <= (fireballcount - 1); ++i) {
                                WildfireFireball wildfirefireballentity;
                                double angle = (i - ((fireballcount - 1) / 2)) * offsetangle;
                                double x = d1 * cos(angle) + d3 * sin(angle);
                                double y = d2;
                                double z = -d1 * sin(angle) + d3 * cos(angle);
                                double a = sqrt((d1 * d1) + (d3 * d3));
                                if (abs((atan2(d2, a))) > maxdepressangle) {
                                    y = -tan(maxdepressangle) * a;
                                }
                                wildfirefireballentity = new WildfireFireball(this.mob.level, this.mob, x, y, z);
                                wildfirefireballentity.setPos(wildfirefireballentity.getX(), this.mob.getY(0.5D), wildfirefireballentity.getZ());
                                this.mob.level.addFreshEntity(wildfirefireballentity);
                            }
                        }
                    } else if (this.attackTime < 160 + health && this.attackTime > 90 - health) {
                        this.mob.setShielding(true);
                    } else if (this.attackTime >= 30 && this.attackTime >= 50) {
                        this.mob.setShielding(false);
                        this.mob.shieldDisabled = false;
                    }

                    this.mob.setInvulnerable(this.mob.getShielding());

                    this.mob.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
                } else if (this.firedRecentlyTimer < 5) {
                    this.mob.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animname = event.getController().getCurrentAnimation() != null ? event.getController().getCurrentAnimation().animationName : "";

        if (event.getController().getAnimationState().equals(AnimationState.Stopped) || !animname.equals("attack")) {
            if (this.getIsAttacking()) {
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
        AnimationController<Wildfire> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
