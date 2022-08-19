package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModEntities;
import io.github.how_bout_no.outvoted.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class Ostrich extends Animal implements ContainerListener, PlayerRideableJumping, Saddleable, IAnimatable {
    private static final Ingredient TAMING_INGREDIENT;
    private static final EntityDataAccessor<Byte> OSTRICH_FLAGS;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID;
    protected SimpleContainer items;
    protected float jumpStrength;
    protected boolean inAir;
    public int eggLayTime;

    public Ostrich(EntityType<? extends Ostrich> type, Level worldIn) {
        super(type, worldIn);
        this.eggLayTime = this.random.nextInt(6000) + 6000;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.maxUpStep = 1.0F;
        this.onChestedStatusChanged();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D, Ostrich.class));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, TAMING_INGREDIENT, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.JUMP_STRENGTH, 0.75D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        HealthUtil.setConfigHealth(this, Config.ostrichHealth.get());

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    static {
        OSTRICH_FLAGS = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.BYTE);
        OWNER_UUID = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.OPTIONAL_UUID);
        TAMING_INGREDIENT = Ingredient.of(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OSTRICH_FLAGS, (byte) 0);
        this.entityData.define(OWNER_UUID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Bred", this.isBred());
        tag.putBoolean("Tame", this.isTame());
        if (this.getOwnerUuid() != null) {
            tag.putUUID("Owner", this.getOwnerUuid());
        }

        if (!this.items.getItem(0).isEmpty()) {
            tag.put("SaddleItem", this.items.getItem(0).save(new CompoundTag()));
        }
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBred(tag.getBoolean("Bred"));
        this.setTame(tag.getBoolean("Tame"));
        UUID uUID2;
        if (tag.hasUUID("Owner")) {
            uUID2 = tag.getUUID("Owner");
        } else {
            String string = tag.getString("Owner");
            uUID2 = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string);
        }

        if (uUID2 != null) {
            this.setOwnerUuid(uUID2);
        }

        if (tag.contains("SaddleItem", 10)) {
            ItemStack itemStack = ItemStack.of(tag.getCompound("SaddleItem"));
            if (itemStack.getItem() == Items.SADDLE) {
                this.items.setItem(0, itemStack);
            }
        }

        this.updateSaddle();
    }

    protected boolean getOstrichFlag(int bitmask) {
        return (this.entityData.get(OSTRICH_FLAGS) & bitmask) != 0;
    }

    protected void setOstrichFlag(int bitmask, boolean flag) {
        byte b = this.entityData.get(OSTRICH_FLAGS);
        if (flag) {
            this.entityData.set(OSTRICH_FLAGS, (byte) (b | bitmask));
        } else {
            this.entityData.set(OSTRICH_FLAGS, (byte) (b & ~bitmask));
        }
    }

    public boolean isTame() {
        return this.getOstrichFlag(2);
    }

    @Nullable
    public UUID getOwnerUuid() {
        return (UUID) ((Optional) this.entityData.get(OWNER_UUID)).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public boolean isInAir() {
        return this.inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public void setTame(boolean tame) {
        this.setOstrichFlag(2, tame);
    }

    public boolean isBred() {
        return this.getOstrichFlag(8);
    }

    public void setBred(boolean bred) {
        this.setOstrichFlag(8, bred);
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTame();
    }

    public void equipSaddle(@Nullable SoundSource sound) {
        this.items.setItem(0, new ItemStack(Items.SADDLE));
        if (sound != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, sound, 0.5F, 1.0F);
        }
    }

    public boolean isSaddled() {
        return this.getOstrichFlag(4);
    }

    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.OSTRICH_AMBIENT.get();
    }

//    @Override
//    protected SoundEvent getDeathSound() {
//        return ModSounds.OSTRICH_DEATH.get();
//    }
//
//    @Override
//    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
//        return ModSounds.OSTRICH_HURT.get();
//    }

    public boolean isFood(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    public static boolean canSpawn(EntityType<Ostrich> entity, LevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        return checkMobSpawnRules(entity, world, spawnReason, blockPos, random);
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        if (fallDistance > 1.0F) {
            this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        }

        int i = this.calculateFallDamage(fallDistance, damageMultiplier);
        if (i <= 0) {
            return false;
        } else {
            i /= 2;
            this.hurt(DamageSource.FALL, (float) i);
            if (this.isVehicle()) {
                for (Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(DamageSource.FALL, (float) i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.7F;
    }

    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return Mth.ceil((fallDistance * 0.5F - 3.0F) * damageMultiplier);
    }

    protected int getInventorySize() {
        return 2;
    }

    protected void onChestedStatusChanged() {
        SimpleContainer simpleInventory = this.items;
        this.items = new SimpleContainer(this.getInventorySize());
        if (simpleInventory != null) {
            simpleInventory.removeListener(this);
            int i = Math.min(simpleInventory.getContainerSize(), this.items.getContainerSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemStack = simpleInventory.getItem(j);
                if (!itemStack.isEmpty()) {
                    this.items.setItem(j, itemStack.copy());
                }
            }
        }

        this.items.addListener(this);
        this.updateSaddle();
    }

    protected void updateSaddle() {
        if (!this.level.isClientSide) {
            this.setOstrichFlag(4, !this.items.getItem(0).isEmpty());
        }
    }

    public void containerChanged(Container sender) {
        boolean bl = this.isSaddled();
        this.updateSaddle();
        if (this.tickCount > 20 && !bl && this.isSaddled()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
        }
    }

    public double getJumpStrength() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    public InteractionResult tryTame(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.isTame() && this.isFood(itemStack) && player.distanceToSqr(this) < 9.0D) {
            this.usePlayerItem(player, hand, itemStack);
            if (!this.level.isClientSide) {
                if (this.random.nextInt(3) == 0) {
                    this.setTame(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public void aiStep() {
        super.aiStep();
        Vec3 vec3d = this.getDeltaMovement();
        if (!this.onGround && vec3d.y < 0.0D) {
            this.setDeltaMovement(vec3d.multiply(1.0D, 0.9D, 1.0D));
        }

        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && !this.isVehicle() && --this.eggLayTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(Items.EGG);
            this.eggLayTime = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.isBaby()) {
            if (this.isVehicle()) {
                return super.mobInteract(player, hand);
            }
        }

        if (!itemStack.isEmpty()) {
            if (this.isFood(itemStack)) {
                return this.tryTame(player, hand);
            }

            InteractionResult actionResult = itemStack.interactLivingEntity(player, this, hand);
            if (actionResult.consumesAction()) {
                return actionResult;
            }

            if (!this.isTame()) {
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            boolean bl = !this.isBaby() && !this.isSaddled() && itemStack.getItem() == Items.SADDLE;
            if (bl) {
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }

        if (this.isBaby()) {
            return this.tryTame(player, hand);
        } else {
            this.putPlayerOnBack(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte status) {
        if (status == 7) {
            this.spawnPlayerReactionParticles(true);
        } else if (status == 6) {
            this.spawnPlayerReactionParticles(false);
        } else {
            super.handleEntityEvent(status);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnPlayerReactionParticles(boolean positive) {
        ParticleOptions particleEffect = positive ? ParticleTypes.HEART : ParticleTypes.SMOKE;

        for (int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02D;
            double e = this.random.nextGaussian() * 0.02D;
            double f = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(particleEffect, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d, e, f);
        }
    }

    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (passenger instanceof Mob) {
            Mob mobEntity = (Mob) passenger;
            this.yBodyRot = mobEntity.yBodyRot;
        }

        float f = Mth.sin(this.yBodyRot * 0.017453292F);
        float g = Mth.cos(this.yBodyRot * 0.017453292F);
        float h = 0.2F;
//        float i = 0.10F;
        float i = 0.0F;
        passenger.setPos(this.getX() + (double) (h * f), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset() + (double) i, this.getZ() - (double) (h * g));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).yBodyRot = this.yBodyRot;
        }
    }

    protected void putPlayerOnBack(Player player) {
        if (!this.level.isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled();
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.items != null) {
            for (int i = 0; i < this.items.getContainerSize(); ++i) {
                ItemStack itemStack = this.items.getItem(i);
                if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
                    this.spawnAtLocation(itemStack);
                }
            }
        }
    }

    public void travel(Vec3 movementInput) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()) {
                LivingEntity livingEntity = (LivingEntity) this.getControllingPassenger();
                this.setYRot(livingEntity.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(livingEntity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.setYBodyRot(this.getYRot());
                this.setYHeadRot(this.yBodyRot);
                float f = livingEntity.xxa * 0.5F;
                float g = livingEntity.zza;
                if (g <= 0.0F) {
                    g *= 0.25F;
                }

                if (this.jumpStrength > 0.0F && !this.isInAir() && this.onGround) {
                    double d = this.getJumpStrength() * (double) this.jumpStrength * (double) this.getBlockJumpFactor();
                    double h;
                    if (this.hasEffect(MobEffects.JUMP)) {
                        h = d + (double) ((float) (this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        h = d;
                    }

                    Vec3 vec3d = this.getDeltaMovement();
                    this.setDeltaMovement(vec3d.x, h, vec3d.z);
                    this.setInAir(true);
                    this.hasImpulse = true;
                    if (g > 0.0F) {
                        float i = Mth.sin(this.getYRot() * 0.017453292F);
                        float j = Mth.cos(this.getYRot() * 0.017453292F);
                        this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * i * this.jumpStrength, 0.0D, 0.4F * j * this.jumpStrength));
                    }

                    this.jumpStrength = 0.0F;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vec3(f, movementInput.y, g));
                } else if (livingEntity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                if (this.onGround) {
                    this.jumpStrength = 0.0F;
                    this.setInAir(false);
                }

                this.calculateEntityAnimation(this, false);
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(movementInput);
            }
        }
    }

    protected void playJumpSound() {
        this.playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
    }

    public boolean canMate(Animal other) {
        if (other == this) {
            return false;
        } else if (!(other instanceof Ostrich)) {
            return false;
        } else {
            return this.canBreed() && ((Ostrich) other).canBreed();
        }
    }

    public boolean canBreed() {
        return !this.isVehicle() && !this.isPassenger() && this.isTame() && !this.isBaby() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Nullable
    private Vec3 getDismountPos(Vec3 vec3d, LivingEntity livingEntity) {
        double d = this.getX() + vec3d.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + vec3d.z;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Pose entityPose : livingEntity.getDismountPoses()) {
            mutable.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75D;

            while (true) {
                double h = this.level.getBlockFloorHeight(mutable);
                if ((double) mutable.getY() + h > g) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(h)) {
                    AABB box = livingEntity.getLocalBoundsForPose(entityPose);
                    Vec3 vec3d2 = new Vec3(d, (double) mutable.getY() + h, f);
                    if (DismountHelper.canDismountTo(this.level, livingEntity, box.move(vec3d2))) {
                        livingEntity.setPose(entityPose);
                        return vec3d2;
                    }
                }

                mutable.move(Direction.UP);
                if (!((double) mutable.getY() < g)) {
                    break;
                }
            }
        }

        return null;
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 vec3d = getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 vec3d2 = this.getDismountPos(vec3d, passenger);
        if (vec3d2 != null) {
            return vec3d2;
        } else {
            Vec3 vec3d3 = getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
            Vec3 vec3d4 = this.getDismountPos(vec3d3, passenger);
            return vec3d4 != null ? vec3d4 : this.position();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        Ostrich ostrichEntity = ModEntities.OSTRICH.get().create(world);
        ostrichEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(ostrichEntity.blockPosition()), MobSpawnType.BREEDING, null, null);
        return ostrichEntity;
    }

    @OnlyIn(Dist.CLIENT)
    public void onPlayerJump(int strength) {
        if (this.isSaddled()) {
            if (strength < 0) {
                strength = 0;
            } else {
                this.jumping = true;
            }

            if (strength >= 90) {
                this.jumpStrength = 1.0F;
            } else {
                this.jumpStrength = 0.4F + 0.4F * (float) strength / 90.0F;
            }

        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void handleStartJump(int height) {
        this.jumping = true;
        this.playJumpSound();
    }

    @Override
    public void handleStopJump() {
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
