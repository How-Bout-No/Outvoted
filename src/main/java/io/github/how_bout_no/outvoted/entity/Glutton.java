package io.github.how_bout_no.outvoted.entity;

import com.google.common.collect.ImmutableList;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModSounds;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction8;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Glutton extends PathfinderMob implements IAnimatable {
    private static final EntityDataAccessor<Boolean> BURROWED;
    private static final EntityDataAccessor<Boolean> ATTACKING;
    private static final EntityDataAccessor<Boolean> ENCHANTING;
    private static final EntityDataAccessor<Integer> VARIANT;
    protected Map<Enchantment, Integer> storedEnchants = new HashMap<>();

    public Glutton(EntityType<? extends Glutton> type, Level worldIn) {
        super(type, worldIn);
        this.xpReward = 5;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BiteGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new BurrowGoal(this));
        this.goalSelector.addGoal(4, new FindSpotGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new WanderGoal(this));
        this.goalSelector.addGoal(6, new LookGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Glutton.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                LivingEntity.class, 10, true, false, (entity) -> this.distanceToSqr(entity) < 2 && !(entity instanceof Creeper)));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        HealthUtil.setConfigHealth(this, Config.gluttonHealth.get());

        Block block = worldIn.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock();
        this.setVariant(block == Blocks.SAND ? 0 : block == Blocks.RED_SAND ? 1 : 2);

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return 0.0F;
    }

    public static boolean canSpawn(EntityType<Glutton> entity, LevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        return world.getRawBrightness(blockPos, 0) > 8 && checkMobSpawnRules(entity, world, spawnReason, blockPos, random) && world.getBlockState(blockPos.below()).is(ModTags.GLUTTON_CAN_BURROW);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.GLUTTON_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.GLUTTON_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.GLUTTON_DEATH.get();
    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected float getSoundVolume() {
        return this.isBurrowed() ? 0.25F : super.getSoundVolume();
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());

        ItemStack item = ItemStack.EMPTY; // Store enchantments in an empty ItemStack
        EnchantmentHelper.setEnchantments(storedEnchants, item);
        CompoundTag compoundNBT = new CompoundTag();
        item.save(compoundNBT);
        compound.put("Enchantments", compoundNBT);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));

        ItemStack item = ItemStack.of(compound.getCompound("Enchantments"));
        storedEnchants = EnchantmentHelper.deserializeEnchantments(item.getEnchantmentTags());
    }

    @Override
    protected Component getTypeName() {
        return switch (getVariant()) {
            case 1 -> new TranslatableComponent("entity.outvoted.glutton_r");
            case 2 -> new TranslatableComponent("entity.outvoted.glutton_s");
            default -> super.getTypeName();
        };
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    static {
        BURROWED = SynchedEntityData.defineId(Glutton.class, EntityDataSerializers.BOOLEAN);
        ATTACKING = SynchedEntityData.defineId(Glutton.class, EntityDataSerializers.BOOLEAN);
        ENCHANTING = SynchedEntityData.defineId(Glutton.class, EntityDataSerializers.BOOLEAN);
        VARIANT = SynchedEntityData.defineId(Glutton.class, EntityDataSerializers.INT);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BURROWED, Boolean.FALSE);
        this.entityData.define(ATTACKING, Boolean.FALSE);
        this.entityData.define(ENCHANTING, Boolean.FALSE);
        this.entityData.define(VARIANT, 0);
    }

    public void setBurrowed(boolean burrowed) {
        this.entityData.set(BURROWED, burrowed);
    }

    public boolean isBurrowed() {
        return this.entityData.get(BURROWED);
    }

    public void setAggressive(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAggressive() {
        return this.entityData.get(ATTACKING);
    }

    public void setEnchanting(boolean enchanting) {
        this.entityData.set(ENCHANTING, enchanting);
    }

    public boolean isEnchanting() {
        return this.entityData.get(ENCHANTING);
    }

    public void setVariant(int type) {
        this.entityData.set(VARIANT, type);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public MutablePair<Integer, ItemStack> modifyEnchantments(ItemStack stack, int damage, int count) {
        ItemStack itemstack = stack.copy();
        Map<Enchantment, Integer> cacheEnchants = new ConcurrentHashMap<>(storedEnchants);
        itemstack.removeTagKey("Enchantments");
        itemstack.removeTagKey("StoredEnchantments");
        if (damage > 0) {
            itemstack.setDamageValue(damage);
        } else {
            itemstack.removeTagKey("Damage");
        }

        if (itemstack.getItem() == Items.BOOK) {
            itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        /*
         * left: Status of item, 0 = accepted, 1 = rejected, 2 = enchanted
         * right: Item to return
         */
        MutablePair<Integer, ItemStack> pair = new MutablePair<>(0, itemstack);
        if (storedEnchants.size() <= Config.gluttonMaxEnchants.get()) {
            itemstack.setCount(count);
            final boolean[] hasCurses = {false};
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((enchant) -> {
                if (enchant.getKey().isCurse()) hasCurses[0] = true;
                return !enchant.getKey().isCurse();
                //return true;
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (hasCurses[0]) {
                this.addEffect(new MobEffectInstance(MobEffects.WITHER, 600, 1));
                pair.setLeft(1);
                return pair;
            } else if (itemstack.getTag() != null && itemstack.getTag().getBoolean("Bitten") && Config.gluttonCapEnchants.get()) {
                pair.setLeft(1);
                return pair;
            } else if (!(itemstack.isEnchantable() || itemstack.isEnchanted() || itemstack.getItem() instanceof EnchantedBookItem)) {
                pair.setRight(ItemStack.EMPTY);
                return pair;
            } else if (itemstack.getItem().equals(ModItems.VOID_HEART.get())) {
                return pair;
            }

            if (!map.isEmpty()) {
                for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    Integer level = entry.getValue();
                    if (cacheEnchants.containsKey(enchantment)) {
                        if (enchantment.getMaxLevel() != 1 && (!Config.gluttonCapEnchants.get() || cacheEnchants.get(enchantment) < enchantment.getMaxLevel() + 1)) {
                            if (level == enchantment.getMaxLevel() && cacheEnchants.get(enchantment) == enchantment.getMaxLevel()) {
                                for (Enchantment ench : cacheEnchants.keySet()) {
                                    if (cacheEnchants.get(ench) == ench.getMaxLevel() + 1) {
                                        pair.setLeft(1);
                                        return pair;
                                    }
                                }
                                cacheEnchants.put(enchantment, enchantment.getMaxLevel() + 1);
                            } else if (level.equals(cacheEnchants.get(enchantment))) {
                                cacheEnchants.put(enchantment, level + 1);
                            } else if (level > cacheEnchants.get(enchantment)) {
                                cacheEnchants.put(enchantment, level);
                            }
                        } else {
                            pair.setLeft(1);
                            return pair;
                        }
                    } else if (!storedEnchants.isEmpty()) {
                        if (storedEnchants.size() < Config.gluttonMaxEnchants.get()) {
                            for (Enchantment ench : storedEnchants.keySet()) {
                                if (enchantment instanceof ProtectionEnchantment && ench instanceof ProtectionEnchantment) {
                                    if (((ProtectionEnchantment) enchantment).checkCompatibility(ench)) {
                                        cacheEnchants.put(enchantment, level);
                                    } else {
                                        pair.setLeft(1);
                                        return pair;
                                    }
                                } else if (enchantment instanceof ArrowInfiniteEnchantment || enchantment instanceof MendingEnchantment) {
                                    cacheEnchants.put(enchantment, level);
                                } else if (enchantment instanceof DamageEnchantment && ench instanceof DamageEnchantment) {
                                    cacheEnchants.put(enchantment, level);
                                } else if (enchantment.isCompatibleWith(ench)) {
                                    cacheEnchants.put(enchantment, level);
                                } else {
                                    pair.setLeft(1);
                                    return pair;
                                }
                            }
                        } else {
                            pair.setLeft(1);
                            return pair;
                        }
                    } else {
                        cacheEnchants.put(enchantment, level);
                    }
                }
            } else if (!storedEnchants.isEmpty()) {
                pair.setLeft(2);
                map = storedEnchants;
                storedEnchants = new HashMap<>();
                EnchantmentHelper.setEnchantments(map, itemstack);
                return pair;
            } else {
                pair.setRight(ItemStack.EMPTY);
            }
            storedEnchants = cacheEnchants;
        } else {
            pair.setLeft(1);
        }
        return pair;
    }

    /**
     * Returns whether this Entity is invulnerable to the given DamageSource.
     */
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) && !source.msgId.equals("wither") && !source.isMagic() && !source.isExplosion();
    }

    @Override
    public void knockback(double strength, double ratioX, double ratioZ) {
        super.knockback(this.isBurrowed() ? 0 : strength, ratioX, ratioZ);
    }

    @Override
    public boolean isPushable() {
        return !this.isBurrowed() && super.isPushable();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean exec = super.doHurtTarget(entityIn);
        if (exec && entityIn instanceof Player player && Config.gluttonStealEnchants.get()) {
            List<NonNullList<ItemStack>> allInventories = ImmutableList.of(player.getInventory().items, player.getInventory().armor, player.getInventory().offhand);
            List<ItemStack> enchantedItems = new ArrayList<>();
            for (NonNullList<ItemStack> inv : allInventories) {
                enchantedItems.addAll(inv.stream().filter((item) -> !EnchantmentHelper.getEnchantments(item).isEmpty()).collect(Collectors.toList()));
            }
            enchantedItems.removeIf((item) -> item.getItem() instanceof AirItem);
            if (!enchantedItems.isEmpty()) {
                ItemStack item = enchantedItems.get(this.random.nextInt(enchantedItems.size()));
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
                item.removeTagKey("Enchantments");
                item.removeTagKey("StoredEnchantments");
                Object[] enchants = enchantments.keySet().toArray();
                Enchantment enchant = (Enchantment) enchants[this.random.nextInt(enchants.length)];
                if (enchantments.get(enchant) > 1) {
                    enchantments.put(enchant, enchantments.get(enchant) - 1);
                } else {
                    enchantments.remove(enchant);
                }
                if (enchantments.isEmpty() && item.getItem() instanceof EnchantedBookItem) {
                    for (NonNullList<ItemStack> inv : allInventories) {
                        if (inv.contains(item)) {
                            inv.set(inv.indexOf(item), new ItemStack(Items.BOOK));
                        }
                    }
                } else {
                    EnchantmentHelper.setEnchantments(enchantments, item);
                }
            }
        }
        return exec;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void aiStep() {
        if (this.isAlive()) {
            this.setInvulnerable(this.isBurrowed());
        }
        super.aiStep();
    }

    static class BiteGoal extends MeleeAttackGoal {
        private final Glutton mob;

        public BiteGoal(Glutton entityIn, double speedIn, boolean useMemory) {
            super(entityIn, speedIn, useMemory);
            this.mob = entityIn;
        }

        public boolean canUse() {
            this.mob.setAggressive(this.mob.getTarget() != null && !this.mob.isBurrowed());
            return super.canUse() && this.mob.level.getDifficulty() != Difficulty.PEACEFUL && !this.mob.isBurrowed();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.mob.level.getDifficulty() != Difficulty.PEACEFUL && !this.mob.isBurrowed();
        }

        public void start() {
            super.start();
            this.mob.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.mob.setAggressive(this.mob.getTarget() != null);
        }
    }

    /* Determines whether a 3x3 section of ground below is suitable for burrowing */
    public boolean isSuitable(Glutton gluttonIn, @Nullable BlockPos pos) {
        if (this.getEffect(MobEffects.WITHER) != null) return false;
        Level world = gluttonIn.level;
        double posX;
        double posY;
        double posZ;
        if (pos == null) {
            posX = gluttonIn.getX();
            posY = gluttonIn.getY();
            posZ = gluttonIn.getZ();
        } else {
            posX = pos.getX();
            posY = pos.getY();
            posZ = pos.getZ();
        }
        boolean ret = true;
        for (double k = posX - 1; k <= posX + 1; ++k) {
            if (ret) {
                for (double l = posZ - 1; l <= posZ + 1; ++l) {
                    BlockState block = world.getBlockState(new BlockPos(k, posY - 1, l));
                    if (block.is(ModTags.GLUTTON_CAN_BURROW) && !gluttonIn.isInWater()) {
                        if (ret) {
                            ret = !gluttonIn.isLeashed();
                        }
                    } else {
                        ret = false;
                        break;
                    }
                }
            } else {
                break;
            }
        }
        return ret;
    }

    static class FindSpotGoal extends Goal {
        protected final Glutton mob;
        private double spotX;
        private double spotY;
        private double spotZ;
        private final double movementSpeed;

        public FindSpotGoal(Glutton theCreatureIn, double movementSpeedIn) {
            this.mob = theCreatureIn;
            this.movementSpeed = movementSpeedIn;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return this.isPossibleSpot() && this.mob.getTarget() == null && !this.mob.isBurrowed() && !this.mob.isSuitable(this.mob, null);
        }

        protected boolean isPossibleSpot() {
            net.minecraft.world.phys.Vec3 vector3d = this.findPossibleSpot();
            if (vector3d == null) {
                return false;
            } else {
                this.spotX = vector3d.x;
                this.spotY = vector3d.y;
                this.spotZ = vector3d.z;
                return true;
            }
        }

        public boolean canContinueToUse() {
            return !this.mob.getNavigation().isDone() && !this.mob.isBurrowed();
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.spotX, this.spotY, this.spotZ, this.movementSpeed);
        }

        public void stop() {
            this.mob.getNavigation().stop();
        }

        @Nullable
        protected net.minecraft.world.phys.Vec3 findPossibleSpot() {
            Random random = this.mob.getRandom();
            BlockPos blockpos = this.mob.blockPosition();

            for (int i = 0; i < 10; ++i) {
                BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(3) - 1, random.nextInt(20) - 10);
                if (this.mob.isSuitable(this.mob, blockpos1) && this.mob.getWalkTargetValue(blockpos1) < 0.0F) {
                    return net.minecraft.world.phys.Vec3.atBottomCenterOf(blockpos1);
                }
            }

            return null;
        }
    }

    static class WanderGoal extends WaterAvoidingRandomStrollGoal {
        private final Glutton mob;

        public WanderGoal(Glutton entityIn) {
            super(entityIn, 1.0D, 0.1F);
            this.mob = entityIn;
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isBurrowed() && !this.mob.isSuitable(this.mob, null);
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.mob.isBurrowed() && !this.mob.isSuitable(this.mob, null);
        }

        public void tick() {
            if (this.mob.isBurrowed() || this.mob.isSuitable(this.mob, null))
                this.mob.getNavigation().stop();
        }
    }

    /**
     * Creates a vector based on caclulated direction of one of the 8 cardinal directions the entity is facing
     */
    private net.minecraft.world.phys.Vec3 directionVector() {
        net.minecraft.world.phys.Vec3 vec3d = net.minecraft.world.phys.Vec3.ZERO;
        double rotation = this.getYRot() - 180;
        if (rotation < 0) rotation += 360;
        int ordinal = Mth.floor(rotation / 45.0D + 0.5D) & 7;
        for (Direction direction : Direction8.values()[ordinal].getDirections()) {
            vec3d = vec3d.add(direction.getStepX(), direction.getStepY(), direction.getStepZ());
        }
        return vec3d;
    }

    static class BurrowGoal extends Goal {
        private final Glutton mob;
        private int tick = 0;
        private ItemStack cacheitem = ItemStack.EMPTY;

        public BurrowGoal(Glutton entityIn) {
            this.mob = entityIn;
        }

        public boolean canUse() {
            return !this.mob.isAggressive() && this.mob.isSuitable(this.mob, null);
        }

        public boolean canContinueToUse() {
            return !this.mob.isAggressive() && this.mob.isSuitable(this.mob, null);
        }

        public void start() {
            this.mob.setBurrowed(true);
        }

        public void stop() {
            this.tick = 0;
            this.mob.setBurrowed(false);
        }

        public void tick() {
            net.minecraft.world.phys.Vec3 vec3d = this.mob.directionVector().scale(0.6D);
            AABB boundingBox = this.mob.getBoundingBox().expandTowards(vec3d).expandTowards(vec3d.scale(-1.0D));
            List<Entity> entities = this.mob.level.getEntities(this.mob, boundingBox);
            if (!entities.isEmpty()) {
                if (!this.mob.isAggressive() && !this.mob.isEnchanting()) {
                    for (Entity entity : entities) {
                        if (boundingBox.contains(entity.position())) {
                            if (entity instanceof ItemEntity) {
                                ItemStack item = ((ItemEntity) entity).getItem();
                                if (((ItemEntity) entity).getThrower() != this.mob.getUUID()) {
                                    this.cacheitem = item.copy();
                                    this.mob.level.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), ModSounds.GLUTTON_EAT.get(), this.mob.getSoundSource(), 0.8F, 0.9F);
                                    entity.discard();
                                    this.mob.setEnchanting(true);
                                    break;
                                }
                            } else if (entity instanceof LivingEntity) {
                                if (entity.isAlive() && this.mob.canAttack((LivingEntity) entity) && !this.mob.isAggressive()) {
                                    this.mob.setBurrowed(false);
                                    this.mob.setAggressive(true);
                                    this.mob.doHurtTarget(entity);
                                }
                            }
                        }
                    }
                }
            }
            if (this.mob.isEnchanting()) {
                this.tick++;

                if (this.tick % 16 == 0) {
                    MutablePair<Integer, ItemStack> pair = this.mob.modifyEnchantments(cacheitem, cacheitem.getDamageValue(), 1);
                    ItemStack item = pair.getRight();
                    if (cacheitem.getItem().equals(ModItems.VOID_HEART.get())) {
                        Explosion.BlockInteraction explosion$mode = this.mob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
                        this.mob.level.explode(this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 2.0F, explosion$mode);
                        this.mob.discard();
                    }
                    if (pair.getLeft() == 0) {
                        if (item != ItemStack.EMPTY) {
                            this.mob.level.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, this.mob.getSoundSource(), 0.8F, 0.6F);
                        }
                    } else if (pair.getLeft() == 1) {
                        this.mob.level.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), ModSounds.GLUTTON_SPIT.get(), this.mob.getSoundSource(), 0.8F, 0.8F);
                        ItemEntity newitem = new ItemEntity(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ(), cacheitem);
                        newitem.setThrower(this.mob.getUUID());
                        this.mob.level.addFreshEntity(newitem);
                    } else {
                        this.mob.level.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.PLAYER_LEVELUP, this.mob.getSoundSource(), 0.8F, 0.6F);
                        item.getOrCreateTag().putInt("Bitten", 1);
                        ItemEntity newitem = new ItemEntity(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ(), item);
                        newitem.setThrower(this.mob.getUUID());
                        this.mob.level.addFreshEntity(newitem);
                    }
                    this.cacheitem = ItemStack.EMPTY;
                    this.tick = 0;
                    this.mob.setEnchanting(false);
                }
            }
        }
    }

    static class LookGoal extends RandomLookAroundGoal {
        private final Glutton mob;

        public LookGoal(Glutton entitylivingIn) {
            super(entitylivingIn);
            this.mob = entitylivingIn;
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isBurrowed();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.mob.isBurrowed();
        }
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animname = event.getController().getCurrentAnimation() != null ? event.getController().getCurrentAnimation().animationName : "";
        if (this.isBurrowed()) {
            if (this.isEnchanting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("bite").addAnimation("biteloop", true));
            } else {
                if (event.getController().getCurrentAnimation() != null) {
                    if (animname.equals("idle") || animname.equals("attacking") || animname.equals("chomp") || animname.equals("burrow")) {
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("burrow").addAnimation("burrowed", true));
                    } else {
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("burrowed", true));
                    }
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("burrow").addAnimation("burrowed", true));
                }
            }
        } else {
            if (event.getController().getCurrentAnimation() == null || animname.equals("idle") || animname.equals("attacking")) {
                if (this.isAggressive()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("attacking"));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
                }
            } else {
                if (this.isAggressive()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp").addAnimation("attacking"));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp").addAnimation("idle"));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> void soundListener(SoundKeyframeEvent<E> event) {
        if (event.sound.equals("chomp")) {
            level.playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.GLUTTON_BITE.get(), this.getSoundSource(), 1.0F, 1.0F, false);
        } else if (event.sound.equals("dig")) {
            BlockState block = level.getBlockState(new BlockPos(this.getX(), this.getY() - 0.5D, this.getZ()));
            if (block.is(BlockTags.SAND)) {
                level.playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.GLUTTON_DIG_SAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            } else {
                level.playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.GLUTTON_DIG.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

    private <E extends IAnimatable> void particleListener(ParticleKeyFrameEvent<E> event) {
        if (event.effect.equals("dig")) {
            for (int i = 0; i < 2; ++i) {
                BlockPos blockpos = new BlockPos(this.getX(), this.getY() - 0.5D, this.getZ());
                BlockState blockstate = this.level.getBlockState(blockpos);
                this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), this.getRandomX(0.5D), this.getY(0), this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<Glutton> controller = new AnimationController<>(this, "controller", 2, this::predicate);
        controller.registerSoundListener(this::soundListener);
        controller.registerParticleListener(this::particleListener);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
