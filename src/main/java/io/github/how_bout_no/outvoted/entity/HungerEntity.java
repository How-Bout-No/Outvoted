package io.github.how_bout_no.outvoted.entity;

import com.google.common.collect.ImmutableList;
import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModSounds;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import org.apache.commons.lang3.tuple.MutablePair;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HungerEntity extends CreatureEntity implements IAnimatable {
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENCHANTING = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(HungerEntity.class, DataSerializers.VARINT);
    private Map<Enchantment, Integer> storedEnchants = new HashMap<>();

    private AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animname = event.getController().getCurrentAnimation() != null ? event.getController().getCurrentAnimation().animationName : "";
        if (this.isBurrowed()) {
            if (this.isEnchanting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.bite2").addAnimation("animation.great_hunger.bite2loop", true));
            } else {
                if (event.getController().getCurrentAnimation() != null) {
                    if (animname.equals("animation.great_hunger.idle") || animname.equals("animation.great_hunger.attacking") || animname.equals("animation.great_hunger.bite") || animname.equals("animation.great_hunger.burrow")) {
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.burrow").addAnimation("animation.great_hunger.burrow2", true));
                    } else {
                        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.burrow2", true));
                    }
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.burrow").addAnimation("animation.great_hunger.burrow2", true));
                }
            }
        } else {
            if (event.getController().getCurrentAnimation() == null || animname.equals("animation.great_hunger.idle") || animname.equals("animation.great_hunger.attacking")) {
                if (this.isAttacking()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.attacking"));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.idle"));
                }
            } else {
                if (this.isAttacking()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.bite").addAnimation("animation.great_hunger.attacking"));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.bite").addAnimation("animation.great_hunger.idle"));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> void soundListener(SoundKeyframeEvent<E> event) {
        if (event.sound.equals("chomp")) {
            world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), ModSounds.HUNGER_BITE.get(), this.getSoundCategory(), 1.0F, 1.0F, false);
        } else if (event.sound.equals("dig")) {
            BlockState block = world.getBlockState(new BlockPos(this.getPosX(), this.getPosY() - 0.5D, this.getPosZ()));
            if (block.isIn(BlockTags.SAND)) {
                world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), ModSounds.HUNGER_DIG_SAND.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            } else {
                world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), ModSounds.HUNGER_DIG.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

    private <E extends IAnimatable> void particleListener(ParticleKeyFrameEvent<E> event) {
        if (event.effect.equals("dig")) {
            for (int i = 0; i < 2; ++i) {
                BlockPos blockpos = new BlockPos(this.getPosX(), this.getPosY() - 0.5D, this.getPosZ());
                BlockState blockstate = this.world.getBlockState(blockpos);
                this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate), this.getPosXRandom(0.5D), this.getPosYHeight(0), this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, "controller", 2, this::predicate);
        controller.registerSoundListener(this::soundListener);
        controller.registerParticleListener(this::particleListener);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public HungerEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 5;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return CreatureEntity.registerAttributes()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 19.0D)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK)
                .createMutableAttribute(Attributes.MAX_HEALTH, OutvotedConfig.COMMON.healthhunger.get())
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 15.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new HungerEntity.BiteGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new HungerEntity.BurrowGoal(this));
        this.goalSelector.addGoal(4, new HungerEntity.FindSpotGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new HungerEntity.WanderGoal(this));
        this.goalSelector.addGoal(6, new HungerEntity.LookGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
    }

    public static boolean canSpawn(EntityType<HungerEntity> entity, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return world.canBlockSeeSky(blockPos);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.HUNGER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.HUNGER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.HUNGER_DEATH.get();
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());

        ItemStack item = ItemStack.EMPTY; // Store enchantments in an empty ItemStack
        EnchantmentHelper.setEnchantments(storedEnchants, item);
        CompoundNBT compoundNBT = new CompoundNBT();
        item.write(compoundNBT);
        compound.put("Enchantments", compoundNBT);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));

        ItemStack item = ItemStack.read(compound.getCompound("Enchantments"));
        storedEnchants = EnchantmentHelper.deserializeEnchantments(item.getEnchantmentTagList());
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        int type;
        if (this.world.getBlockState(this.getPositionUnderneath()).getBlock().matchesBlock(Blocks.SAND)) {
            type = 0;
        } else if (this.world.getBlockState(this.getPositionUnderneath()).getBlock().matchesBlock(Blocks.RED_SAND)) {
            type = 1;
        } else {
            type = 2;
        }
        this.setVariant(type);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(BURROWED, Boolean.FALSE);
        this.dataManager.register(ATTACKING, Boolean.FALSE);
        this.dataManager.register(ENCHANTING, Boolean.FALSE);
        this.dataManager.register(VARIANT, 0);
    }

    public void setBurrowed(boolean burrowed) {
        this.dataManager.set(BURROWED, burrowed);
    }

    public boolean isBurrowed() {
        return this.dataManager.get(BURROWED);
    }

    public void setAttacking(boolean attacking) {
        this.dataManager.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.dataManager.get(ATTACKING);
    }

    public void setEnchanting(boolean enchanting) {
        this.dataManager.set(ENCHANTING, enchanting);
    }

    public boolean isEnchanting() {
        return this.dataManager.get(ENCHANTING);
    }

    public void setVariant(int type) {
        this.dataManager.set(VARIANT, type);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private static boolean hasEnchantibility(ItemStack itemStack) {
        return itemStack.isEnchantable() || itemStack.isEnchanted() || itemStack.getItem() instanceof EnchantedBookItem;
    }

    public MutablePair<Integer, ItemStack> modifyEnchantments(ItemStack stack, int damage, int count) {
        ItemStack itemstack = stack.copy();
        Map<Enchantment, Integer> cacheEnchants = new ConcurrentHashMap<>(storedEnchants);
        itemstack.removeChildTag("Enchantments");
        itemstack.removeChildTag("StoredEnchantments");
        if (damage > 0) {
            itemstack.setDamage(damage);
        } else {
            itemstack.removeChildTag("Damage");
        }

        if (itemstack.getItem() == Items.BOOK) {
            itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        /*
         * left: Status of item, 0 = accepted, 1 = rejected, 2 = enchanted
         * right: Item to return
         */
        MutablePair<Integer, ItemStack> pair = new MutablePair<>(0, itemstack);
        if (storedEnchants.size() <= OutvotedConfig.COMMON.max_enchants.get()) {
            itemstack.setCount(count);
            final boolean[] hasCurses = {false};
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((enchant) -> {
                if (enchant.getKey().isCurse()) hasCurses[0] = true;
                return !enchant.getKey().isCurse();
                //return true;
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (hasCurses[0]) {
                this.addPotionEffect(new EffectInstance(Effects.WITHER, 600, 1));
                pair.setLeft(1);
                return pair;
            } else if (itemstack.getTag() != null && itemstack.getTag().contains("Bitten")) {
                pair.setLeft(1);
                return pair;
            } else if (!hasEnchantibility(itemstack)) {
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
                        if (enchantment.getMaxLevel() != 1 && cacheEnchants.get(enchantment) != enchantment.getMaxLevel() + 1) {
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
                        for (Enchantment ench : storedEnchants.keySet()) {
                            if (enchantment instanceof ProtectionEnchantment && ench instanceof ProtectionEnchantment) {
                                if (((ProtectionEnchantment) enchantment).canApplyTogether(ench)) {
                                    cacheEnchants.put(enchantment, level);
                                } else {
                                    pair.setLeft(1);
                                    return pair;
                                }
                            } else if (enchantment instanceof InfinityEnchantment || enchantment instanceof MendingEnchantment) {
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
     *
     * @param source
     */
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) && !source.damageType.equals("wither") && !source.isMagicDamage() && !source.isExplosion();
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean exec = super.attackEntityAsMob(entityIn);
        if (exec && entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            List<NonNullList<ItemStack>> allInventories = ImmutableList.of(player.inventory.mainInventory, player.inventory.armorInventory, player.inventory.offHandInventory);
            List<ItemStack> enchantedItems = new ArrayList<>();
            for (NonNullList<ItemStack> inv : allInventories) {
                enchantedItems.addAll(inv.stream().filter((item) -> !EnchantmentHelper.getEnchantments(item).isEmpty()).collect(Collectors.toList()));
            }
            enchantedItems.removeIf((item) -> item.getItem() instanceof AirItem);
            if (!enchantedItems.isEmpty()) {
                ItemStack item = enchantedItems.get(this.rand.nextInt(enchantedItems.size()));
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
                item.removeChildTag("Enchantments");
                item.removeChildTag("StoredEnchantments");
                Object[] enchants = enchantments.keySet().toArray();
                Enchantment enchant = (Enchantment) enchants[this.rand.nextInt(enchants.length)];
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
    public void livingTick() {
        if (this.isAlive()) {
            this.setInvulnerable(this.isBurrowed());
        }
        super.livingTick();
    }

    public boolean canBePushed() {
        return !this.isInvulnerable() && super.canBePushed();
    }

    static class BiteGoal extends MeleeAttackGoal {
        private final HungerEntity hunger;

        public BiteGoal(HungerEntity entityIn, double speedIn, boolean useMemory) {
            super(entityIn, speedIn, useMemory);
            this.hunger = entityIn;
        }

        public boolean shouldExecute() {
            this.hunger.setAttacking(this.hunger.getAttackTarget() != null && !this.hunger.isBurrowed());
            return super.shouldExecute() && this.hunger.world.getDifficulty() != Difficulty.PEACEFUL && !this.hunger.isBurrowed();
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && this.hunger.world.getDifficulty() != Difficulty.PEACEFUL && !this.hunger.isBurrowed();
        }

        public void startExecuting() {
            super.startExecuting();
            this.hunger.setAttacking(true);
        }

        public void resetTask() {
            super.resetTask();
            this.hunger.setAttacking(this.attacker.getAttackTarget() != null);
        }
    }

    /* Determines whether a 3x3 section of ground below is suitable for burrowing */
    public boolean isSuitable(HungerEntity hungerIn, @Nullable BlockPos pos) {
        if (this.getActivePotionEffect(Effects.WITHER) != null) return false;
        World world = hungerIn.world;
        double posX;
        double posY;
        double posZ;
        if (pos == null) {
            posX = hungerIn.getPosX();
            posY = hungerIn.getPosY();
            posZ = hungerIn.getPosZ();
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
                    if (block.isIn(ModTags.HUNGER_CAN_BURROW) && !hungerIn.isInWater()) {
                        if (ret) {
                            ret = !hungerIn.getLeashed();
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
        protected final HungerEntity hunger;
        private double spotX;
        private double spotY;
        private double spotZ;
        private final double movementSpeed;

        public FindSpotGoal(HungerEntity theCreatureIn, double movementSpeedIn) {
            this.hunger = theCreatureIn;
            this.movementSpeed = movementSpeedIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return this.isPossibleSpot() && this.hunger.getAttackTarget() == null && !this.hunger.isBurrowed() && !this.hunger.isSuitable(this.hunger, null);
        }

        protected boolean isPossibleSpot() {
            Vector3d vector3d = this.findPossibleSpot();
            if (vector3d == null) {
                return false;
            } else {
                this.spotX = vector3d.x;
                this.spotY = vector3d.y;
                this.spotZ = vector3d.z;
                return true;
            }
        }

        public boolean shouldContinueExecuting() {
            return !this.hunger.getNavigator().noPath() && !this.hunger.isBurrowed();
        }

        public void startExecuting() {
            this.hunger.getNavigator().tryMoveToXYZ(this.spotX, this.spotY, this.spotZ, this.movementSpeed);
        }

        public void resetTask() {
            this.hunger.getNavigator().clearPath();
        }

        @Nullable
        protected Vector3d findPossibleSpot() {
            Random random = this.hunger.getRNG();
            BlockPos blockpos = this.hunger.getPosition();

            for (int i = 0; i < 10; ++i) {
                BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
                if (this.hunger.isSuitable(this.hunger, blockpos1)) {
                    return Vector3d.copyCenteredHorizontally(blockpos1);
                }
            }

            return null;
        }
    }

    static class WanderGoal extends WaterAvoidingRandomWalkingGoal {
        private final HungerEntity hunger;

        public WanderGoal(HungerEntity entityIn) {
            super(entityIn, 1.0D, 0.1F);
            this.hunger = entityIn;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && !this.hunger.isBurrowed() && !this.hunger.isSuitable(this.hunger, null);
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && !this.hunger.isBurrowed() && !this.hunger.isSuitable(this.hunger, null);
        }

        public void tick() {
            if (this.hunger.isBurrowed() || this.hunger.isSuitable(this.hunger, null))
                this.creature.getNavigator().clearPath();
        }
    }

    static class BurrowGoal extends Goal {
        private final HungerEntity hunger;
        private int tick = 0;
        private ItemStack cacheitem = ItemStack.EMPTY;

        public BurrowGoal(HungerEntity entityIn) {
            this.hunger = entityIn;
        }

        public boolean shouldExecute() {
            return !this.hunger.isAttacking() && this.hunger.isSuitable(this.hunger, null);
        }

        public boolean shouldContinueExecuting() {
            return !this.hunger.isAttacking() && this.hunger.isSuitable(this.hunger, null);
        }

        public void startExecuting() {
            this.hunger.setBurrowed(true);
        }

        public void resetTask() {
            this.tick = 0;
            this.hunger.setBurrowed(false);
        }

        public void tick() {
            List<Entity> entities = this.hunger.world.getEntitiesWithinAABBExcludingEntity(this.hunger, this.hunger.getBoundingBox().expand(1.0D, 0.0D, 1.0D).expand(-1.0D, 0.0D, -1.0D));
            if (!entities.isEmpty()) {
                if (!this.hunger.isAttacking() && !this.hunger.isEnchanting()) {
                    for (Entity entity : entities) {
                        double d0 = this.hunger.getDistanceSq(entity);
                        if (d0 < 1.1) {
                            if (entity instanceof ItemEntity) {
                                ItemStack item = ((ItemEntity) entity).getItem();
                                if (((ItemEntity) entity).getThrowerId() != this.hunger.getUniqueID()) {
                                    this.cacheitem = item.copy();
                                    this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), ModSounds.HUNGER_EAT.get(), this.hunger.getSoundCategory(), 0.8F, 0.9F);
                                    entity.remove();
                                    this.hunger.setEnchanting(true);
                                    break;
                                }
                            } else if (entity instanceof LivingEntity) {
                                if (entity.isAlive() && this.hunger.canAttack((LivingEntity) entity)) {
                                    this.hunger.setBurrowed(false);
                                    this.hunger.setAttacking(true);
                                    this.hunger.attackEntityAsMob(entity);
                                }
                            }
                        }
                    }
                }
            }
            if (this.hunger.isEnchanting()) {
                this.tick++;

                if (this.tick % 16 == 0) {
                    MutablePair<Integer, ItemStack> pair = this.hunger.modifyEnchantments(cacheitem, cacheitem.getDamage(), 1);
                    ItemStack item = pair.getRight();
                    if (cacheitem.getItem().equals(ModItems.VOID_HEART.get())) {
                        Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.hunger.world, this.hunger) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
                        this.hunger.world.createExplosion(this.hunger, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), 2.0F, explosion$mode);
                        this.hunger.remove();
                    }
                    if (pair.getLeft() == 0) {
                        if (item != ItemStack.EMPTY) {
                            this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, this.hunger.getSoundCategory(), 0.8F, 0.6F);
                        }
                    } else if (pair.getLeft() == 1) {
                        this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), ModSounds.HUNGER_SPIT.get(), this.hunger.getSoundCategory(), 0.8F, 0.8F);
                        ItemEntity newitem = new ItemEntity(this.hunger.world, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), cacheitem);
                        newitem.setThrowerId(this.hunger.getUniqueID());
                        this.hunger.world.addEntity(newitem);
                    } else {
                        this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, this.hunger.getSoundCategory(), 0.8F, 0.6F);
                        item.getOrCreateTag().putInt("Bitten", 1);
                        ItemEntity newitem = new ItemEntity(this.hunger.world, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), item);
                        newitem.setThrowerId(this.hunger.getUniqueID());
                        this.hunger.world.addEntity(newitem);
                    }
                    this.cacheitem = ItemStack.EMPTY;
                    this.tick = 0;
                    this.hunger.setEnchanting(false);
                }
            }
        }
    }

    static class LookGoal extends LookRandomlyGoal {
        private final HungerEntity hunger;

        public LookGoal(HungerEntity entitylivingIn) {
            super(entitylivingIn);
            this.hunger = entitylivingIn;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && !this.hunger.isBurrowed();
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && !this.hunger.isBurrowed();
        }
    }
}
