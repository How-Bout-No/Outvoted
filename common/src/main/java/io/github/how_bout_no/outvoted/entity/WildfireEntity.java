package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModSounds;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.util.IMixinBlazeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
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

public class WildfireEntity extends HostileEntity implements IAnimatable {
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private boolean shieldDisabled = false;
    private static final TrackedData<Boolean> SHIELDING;
    private static final TrackedData<Byte> ON_FIRE;
    private static final TrackedData<Boolean> ATTACKING;
    private static final TrackedData<Integer> VARIANT;

    public WildfireEntity(EntityType<? extends WildfireEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.experiencePoints = 20;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AttackGoal(this));
        this.goalSelector.add(2, new GoToWalkTargetGoal(this, 1.0D));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D, 0.0F));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return HostileEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 4.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.115D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, @Nullable EntityData spawnDataIn, @Nullable NbtCompound dataTag) {
        HealthUtil.setConfigHealth(this, Outvoted.commonConfig.entities.wildfire.health);

        int type = 0;
        if (reason != SpawnReason.SPAWN_EGG && reason != SpawnReason.DISPENSER) {
            if (worldIn.getBiomeKey(this.getBlockPos()).isPresent()) {
                if (worldIn.getBiomeKey(this.getBlockPos()).get() == BiomeKeys.SOUL_SAND_VALLEY) {
                    type = 1;
                }
            }
        } else {
            Block block = worldIn.getBlockState(this.getVelocityAffectingPos()).getBlock();
            if (block.is(Blocks.SOUL_SAND) || block.is(Blocks.SOUL_SOIL)) {
                type = 1;
            }
        }
        this.setVariant(type);

        if (reason == SpawnReason.NATURAL) {
            ServerWorld serverWorld = worldIn.toServerWorld();
            int max = 2;
            switch (difficultyIn.getGlobalDifficulty()) {
                case NORMAL:
                    max = 3;
                    break;
                case HARD:
                    max = 4;
                    break;
            }
            int min = max - 1;
            int rand = new Random().nextInt(max - min) + min;
            for (int i = 1; i <= rand; i++) {
                BlazeEntity blaze = EntityType.BLAZE.create(serverWorld);
                blaze.updatePositionAndAngles(this.getParticleX(3.0D), this.getY(), this.getParticleZ(3.0D), this.yaw, this.pitch);
                while (!serverWorld.isAir(blaze.getBlockPos())) { // Should prevent spawning inside of blocks
                    blaze.updatePositionAndAngles(this.getParticleX(3.0D), this.getY(), this.getParticleZ(3.0D), this.yaw, this.pitch);
                }
                ((IMixinBlazeEntity) blaze).initialize(worldIn, difficultyIn, reason, null, null);
                serverWorld.spawnEntity(blaze);
            }
        }

        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    public int getLimitPerChunk() {
        return this.world.getDifficulty() == Difficulty.HARD ? 2 : 1;
    }

    @Override
    protected Text getDefaultName() {
        return getVariant() == 0 ? super.getDefaultName() : new TranslatableText("entity.outvoted.wildfire_s");
    }

    static {
        SHIELDING = DataTracker.registerData(WildfireEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        ON_FIRE = DataTracker.registerData(WildfireEntity.class, TrackedDataHandlerRegistry.BYTE);
        ATTACKING = DataTracker.registerData(WildfireEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        VARIANT = DataTracker.registerData(WildfireEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SHIELDING, Boolean.FALSE);
        this.dataTracker.startTracking(ATTACKING, Boolean.FALSE);
        this.dataTracker.startTracking(ON_FIRE, (byte) 0);
        this.dataTracker.startTracking(VARIANT, 0);
    }

    public void setShielding(boolean shielding) {
        if (!this.shieldDisabled) {
            this.dataTracker.set(SHIELDING, shielding);
        } else {
            this.dataTracker.set(SHIELDING, false);
        }
    }

    public boolean getShielding() {
        return this.dataTracker.get(SHIELDING) && !this.shieldDisabled;
    }

    public void setVariant(int type) {
        this.dataTracker.set(VARIANT, type);
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setAttacking(boolean attacking) {
        this.dataTracker.set(ATTACKING, attacking);
    }

    public boolean getIsAttacking() {
        return this.dataTracker.get(ATTACKING);
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
    protected float getActiveEyeHeight(EntityPose poseIn, EntityDimensions sizeIn) {
        return 1.8F;
    }

    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    public void tickMovement() {
        if (!this.onGround && this.getVelocity().y < 0.0D) {
            this.setVelocity(this.getVelocity().multiply(1.0D, 0.6D, 1.0D));
        }

        if (this.world.isClient) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D, ModSounds.WILDFIRE_BURN.get(), this.getSoundCategory(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
                //this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }

        }
        if (this.getShielding()) {
            this.world.addParticle(ParticleTypes.LAVA, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
        }
        if (this.getIsAttacking()) {
            for (int particlei = 0; particlei < 16; ++particlei) {
                this.world.addParticle(ParticleTypes.FLAME, this.getParticleX(0.75D), this.getRandomBodyY(), this.getParticleZ(0.75D), 0.0D, 0.0D, 0.0D);
            }
        }

        super.tickMovement();
    }

    public boolean hurtByWater() {
        return true;
    }

    @Override
    @Nullable
    public ItemEntity dropStack(ItemStack stack, float yOffset) {
        if (stack.getItem() instanceof WildfireHelmetItem) {
            if (Outvoted.clientConfig.wildfireVariants && this.getVariant() == 1) {
                stack.getOrCreateTag().putFloat("SoulTexture", 1.0F);
            }
        }

        return super.dropStack(stack, yOffset);
    }

    protected void mobTick() {
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.random.nextGaussian() * (3 / ((this.getHealth() / 25) + 1));
        }

        LivingEntity livingentity = this.getTarget();
        if (livingentity != null && livingentity.getEyeY() > this.getEyeY() + (double) this.heightOffset && this.canTarget(livingentity)) {
            Vec3d vector3d = this.getVelocity();
            this.setVelocity(this.getVelocity().add(0.0D, ((double) 0.3F - vector3d.y) * (double) 0.3F, 0.0D));
            this.velocityDirty = true;
        }

        super.mobTick();
    }

    public boolean handleFallDamage(float distance, float damageMultiplier) {
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
        return (this.dataTracker.get(ON_FIRE) & 1) != 0;
    }

    private void setOnFire(boolean onFire) {
        byte b0 = this.dataTracker.get(ON_FIRE);
        if (onFire) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataTracker.set(ON_FIRE, b0);
    }

    public boolean damage(DamageSource source, float amount) {
        if (!this.world.isClient) {
            if (source.getSource() instanceof LivingEntity && this.isInvulnerable()) {
                LivingEntity entity = (LivingEntity) source.getSource();
                // Shield disabling on critical axe hit
                if (entity.getMainHandStack().getItem() instanceof AxeItem) {
                    double itemDamage = ((AxeItem) entity.getMainHandStack().getItem()).getAttackDamage() + 1;
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
                    source.getSource().setOnFireFor(12);
                } else if (source.getSource() != null) {
                    source.getSource().setOnFireFor(8);
                }

                return false;
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if ((source == DamageSource.GENERIC || source instanceof EntityDamageSource) && !source.isSourceCreativePlayer()) {
            return this.isInvulnerable();
        } else {
            return false;
        }
    }

    static class AttackGoal extends Goal {
        private final WildfireEntity mob;
        private int attackStep;
        private int attackTime;
        private int firedRecentlyTimer;

        public AttackGoal(WildfireEntity wildfireIn) {
            this.mob = wildfireIn;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            LivingEntity livingentity = this.mob.getTarget();
            return livingentity != null && livingentity.isAlive() && this.mob.canTarget(livingentity);
        }

        public void start() {
            this.attackStep = 0;
        }

        public void stop() {
            this.mob.setOnFire(false);
            this.mob.setShielding(false);
            this.mob.setAttacking(false);
            this.firedRecentlyTimer = 0;
        }

        public void tick() {
            --this.attackTime;
            LivingEntity livingentity = this.mob.getTarget();
            this.mob.setAttacking(false);
            if (livingentity != null) {
                boolean flag = this.mob.getVisibilityCache().canSee(livingentity);
                if (flag) {
                    this.firedRecentlyTimer = 0;
                } else {
                    ++this.firedRecentlyTimer;
                }

                double d0 = this.mob.squaredDistanceTo(livingentity);
                if (d0 < 4.0D) {
                    this.mob.setOnFire(true);

                    if (this.attackTime <= 0) {
                        this.mob.setAttacking(true);
                        this.attackTime = 5;
                        this.mob.tryAttack(livingentity);
                        livingentity.setOnFireFor(4);
                    }

                    this.mob.getMoveControl().moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
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
                            this.mob.setOnFire(true);
                        } else if (this.attackStep <= maxAttackSteps) {
                            this.attackTime = (int) (25 * healthPercent + 5);
                        } else {
                            this.attackTime = 200;
                            this.attackStep = 0;
                            this.mob.setOnFire(false);
                            this.mob.setAttacking(false);
                        }

                        if (this.attackStep > 1) {
                            this.mob.setAttacking(true);

                            if (!this.mob.isSilent()) {
                                this.mob.world.playSound(null, this.mob.getBlockPos(), ModSounds.WILDFIRE_SHOOT.get(), this.mob.getSoundCategory(), 1.0F, 1.0F);
                            }

                            double fireballcount = Outvoted.commonConfig.entities.wildfire.attacking.fireballCount;
                            double offsetangle = toRadians(Outvoted.commonConfig.entities.wildfire.attacking.offsetAngle);
                            double maxdepressangle = toRadians(Outvoted.commonConfig.entities.wildfire.attacking.maxDepressAngle);

                            //update target pos
                            double d1 = livingentity.getX() - this.mob.getX();
                            double d2 = livingentity.getBodyY(0.5D) - this.mob.getBodyY(0.5D);
                            double d3 = livingentity.getZ() - this.mob.getZ();

                            //shoot fireballs
                            for (int i = 0; i <= (fireballcount - 1); ++i) {
                                WildfireFireballEntity wildfirefireballentity;
                                double angle = (i - ((fireballcount - 1) / 2)) * offsetangle;
                                double x = d1 * cos(angle) + d3 * sin(angle);
                                double y = d2;
                                double z = -d1 * sin(angle) + d3 * cos(angle);
                                double a = sqrt((d1 * d1) + (d3 * d3));
                                if (abs((atan2(d2, a))) > maxdepressangle) {
                                    y = -tan(maxdepressangle) * (a);
                                }
                                wildfirefireballentity = new WildfireFireballEntity(this.mob.world, this.mob, x, y, z);
                                wildfirefireballentity.setPosition(wildfirefireballentity.getX(), this.mob.getBodyY(0.5D), wildfirefireballentity.getZ());
                                this.mob.world.spawnEntity(wildfirefireballentity);
                            }
                        }
                    } else if (this.attackTime < 160 + health && this.attackTime > 90 - health) {
                        this.mob.setShielding(true);
                    } else if (this.attackTime >= 30 && this.attackTime >= 50) {
                        this.mob.setShielding(false);
                        this.mob.shieldDisabled = false;
                    }

                    this.mob.setInvulnerable(this.mob.getShielding());

                    this.mob.getLookControl().lookAt(livingentity, 10.0F, 10.0F);
                } else if (this.firedRecentlyTimer < 5) {
                    this.mob.getMoveControl().moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.mob.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
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
        AnimationController<WildfireEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
