package com.hbn.outvoted.entity;

import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.init.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BookItem;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HungerEntity extends CreatureEntity implements IAnimatable {
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENCHANTING = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(HungerEntity.class, DataSerializers.VARINT);
    private Map<Enchantment, Integer> storedEnchants = new ConcurrentHashMap<Enchantment, Integer>();

    private AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animname = event.getController().getCurrentAnimation() != null ? event.getController().getCurrentAnimation().animationName : "";
        if (this.getBurrowed()) {
            if (this.getEnchanting()) {
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
                if (this.getAttacking()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.attacking"));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.idle"));
                }
            } else {
                if (this.getAttacking()) {
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
            world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
        } else if (event.sound.equals("dig")) {
            BlockState block = world.getBlockState(new BlockPos(this.getPosX(), this.getPosY() - 1, this.getPosZ()));
            if (block.isIn(BlockTags.SAND)) {
                world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            } else {
                world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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
        this.goalSelector.addGoal(4, new HungerEntity.FindSpotGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
    }

    public static boolean canSpawn(EntityType<HungerEntity> entity, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return world.canBlockSeeSky(blockPos);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.HUNGER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.HUNGER_HIT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.HUNGER_DEATH.get();
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
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
        if (this.world.getBlockState(this.getPosition().down()).getBlock().matchesBlock(Blocks.SAND)) {
            type = 0;
        } else if (this.world.getBlockState(this.getPosition().down()).getBlock().matchesBlock(Blocks.RED_SAND)) {
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

    public boolean getBurrowed() {
        return this.dataManager.get(BURROWED);
    }

    public void setAttacking(boolean attacking) {
        this.dataManager.set(ATTACKING, attacking);
    }

    public boolean getAttacking() {
        return this.dataManager.get(ATTACKING);
    }

    public void setEnchanting(boolean enchanting) {
        this.dataManager.set(ENCHANTING, enchanting);
    }

    public boolean getEnchanting() {
        return this.dataManager.get(ENCHANTING);
    }

    public void setVariant(int type) {
        this.dataManager.set(VARIANT, type);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    public ItemStack modifyEnchantments(ItemStack stack, int damage, int count) {
        // TODO: Rewrite this awful crap
        ItemStack itemstack = stack.copy();
        itemstack.removeChildTag("Enchantments");
        itemstack.removeChildTag("StoredEnchantments");
        if (damage > 0) {
            itemstack.setDamage(damage);
        } else {
            itemstack.removeChildTag("Damage");
        }
        if (storedEnchants.size() <= OutvotedConfig.COMMON.max_enchants.get() && hasEnchantibility(stack)) {
            itemstack.setCount(count);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((enchant) -> {
                return !enchant.getKey().isCurse();
                //return true;
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Map<Enchantment, Integer> curses = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((enchant) -> {
                return enchant.getKey().isCurse();
                //return true;
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (!curses.isEmpty()) this.addPotionEffect(new EffectInstance(Effects.WITHER, 600));

            if (!map.isEmpty()) {
                for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                    Enchantment key = entry.getKey();
                    Integer value = entry.getValue();
                    if (storedEnchants.containsKey(key)) {
                        if (value == key.getMaxLevel() && storedEnchants.get(key) == key.getMaxLevel()) {
                            boolean isMax = false;
                            for (Map.Entry<Enchantment, Integer> stentry : storedEnchants.entrySet()) {
                                if (stentry.getValue() == (stentry.getKey().getMaxLevel())) {
                                    isMax = true;
                                }
                            }
                            if (isMax) {
                                storedEnchants.put(key, key.getMaxLevel() + 1);
                            } else {
                                return null;
                            }
                        } else if (value == storedEnchants.get(key)) {
                            storedEnchants.put(key, value + 1);
                        } else if (value > storedEnchants.get(key)) {
                            storedEnchants.put(key, value);
                        } else {
                            return null;
                        }
                        itemstack = ItemStack.EMPTY;
                    } else {
                        if (storedEnchants.size() > 0) {
                            for (Enchantment ench : storedEnchants.keySet()) {
                                if (ench instanceof ProtectionEnchantment) {
                                    if (key instanceof ProtectionEnchantment) {
                                        if (!storedEnchants.isEmpty()) {
                                            if (key.isCompatibleWith(ench)) {
                                                storedEnchants.put(key, value);
                                            } else if (key != ench) {
                                                return null;
                                            }
                                        } else {
                                            storedEnchants.put(key, value);
                                        }
                                    } else {
                                        storedEnchants.put(key, value);
                                    }
                                } else if (key.isCompatibleWith(ench)) {
                                    storedEnchants.put(key, value);
                                } else if (key instanceof InfinityEnchantment || key instanceof MendingEnchantment) {
                                    storedEnchants.put(key, value);
                                } else if (key != ench) {
                                    return null;
                                } else {
                                    itemstack = ItemStack.EMPTY;
                                }
                            }
                        } else {
                            storedEnchants.put(key, value);
                            itemstack = ItemStack.EMPTY;
                        }
                    }
                    //map = new HashMap<Enchantment, Integer>();
                }
            } else {
                map = storedEnchants;
                storedEnchants = new ConcurrentHashMap<Enchantment, Integer>();
            }

            if (itemstack.getItem() == Items.ENCHANTED_BOOK || map.size() == 0) {
                itemstack = ItemStack.EMPTY;
            } else if (itemstack.getItem() == Items.BOOK) {
                itemstack = new ItemStack(Items.ENCHANTED_BOOK);
            }

            if (itemstack != ItemStack.EMPTY) {
                EnchantmentHelper.setEnchantments(map, itemstack);
                itemstack.setRepairCost(0);
            }


        /*for (int i = 0; i < map.size(); ++i) {
            itemstack.setRepairCost(RepairContainer.getNewRepairCost(itemstack.getRepairCost()));
        }*/
        } else {
            itemstack = ItemStack.EMPTY;
        }

        return itemstack;
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

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick() {
        if (this.isAlive()) {
            this.setInvulnerable(this.getBurrowed());
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
            boolean exec = super.shouldExecute() && this.hunger.world.getDifficulty() != Difficulty.PEACEFUL && !this.hunger.getBurrowed();
            this.hunger.setAttacking(exec);
            return exec;
        }
    }

    public boolean isSuitable(HungerEntity entityIn) {
        if (this.getActivePotionEffect(Effects.WITHER) != null) return false;
        World world = entityIn.world;
        double posX = entityIn.getPosX();
        double posY = entityIn.getPosY();
        double posZ = entityIn.getPosZ();
        boolean ret = true;
        for (double k = posX - 1; k <= posX + 1; ++k) {
            if (ret) {
                for (double l = posZ - 1; l <= posZ + 1; ++l) {
                    BlockState block = world.getBlockState(new BlockPos(k, posY - 1, l));
                    if (block.isIn(BlockTags.BAMBOO_PLANTABLE_ON) && !entityIn.isInWater()) {
                        if (ret) {
                            ret = !entityIn.getLeashed();
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

    static class FindSpotGoal extends WaterAvoidingRandomWalkingGoal {
        private final HungerEntity hunger;

        public FindSpotGoal(HungerEntity entityIn) {
            super(entityIn, 1.0D, 0.1F);
            this.hunger = entityIn;
        }

        public boolean shouldExecute() {
            return super.shouldExecute() && !this.hunger.getBurrowed() && !this.hunger.isSuitable(this.hunger);
        }

        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && !this.hunger.getBurrowed() && !this.hunger.isSuitable(this.hunger);
        }

        public void tick() {
            if (this.hunger.getBurrowed() || this.hunger.isSuitable(this.hunger))
                this.creature.getNavigator().clearPath();
        }
    }

    private static boolean hasEnchantibility(ItemStack itemStack) {
        return itemStack.isEnchantable() || itemStack.isEnchanted();
    }

    static class BurrowGoal extends Goal {
        private final HungerEntity hunger;
        private int tick = 0;
        private ItemStack cacheitem = ItemStack.EMPTY;
        private Entity cacheentity = null;

        public BurrowGoal(HungerEntity entityIn) {
            this.hunger = entityIn;
        }

        public void resetTask() {
            this.tick = 0;
            this.hunger.setBurrowed(false);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return !this.hunger.getAttacking() && this.hunger.isSuitable(this.hunger);
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return !this.hunger.getAttacking() && this.hunger.isSuitable(this.hunger);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */

        public void tick() {
            boolean suitable = this.hunger.isSuitable(this.hunger);
            List<Entity> entities = this.hunger.world.getEntitiesWithinAABBExcludingEntity(this.hunger, this.hunger.getBoundingBox().expand(1.0D, 0.0D, 1.0D).expand(-1.0D, 0.0D, -1.D));
            if (!entities.isEmpty()) {
                for (Entity entity : entities) {
                    double d0 = this.hunger.getDistanceSq(entity);
                    if (d0 < 1.1) {
                        if (entity instanceof ItemEntity) {
                            ItemStack item = ((ItemEntity) entity).getItem();
                            if (item.getTag() != null) {
                                if (!this.hunger.getEnchanting()) {
                                    if (!item.getTag().contains("Bitten")) {
                                        this.cacheitem = item.copy();
                                        this.cacheentity = entity;
                                        this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_STRIDER_EAT, this.hunger.getSoundCategory(), 0.8F, 0.9F);
                                        entity.remove();
                                        this.hunger.setEnchanting(true);
                                    }
                                    this.hunger.setBurrowed(suitable);
                                }
                            } else if (item.getItem() instanceof BookItem) {
                                if (!this.hunger.getEnchanting()) {
                                    this.cacheitem = item.copy();
                                    this.cacheentity = entity;
                                    this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_STRIDER_EAT, this.hunger.getSoundCategory(), 0.8F, 0.9F);
                                    entity.remove();
                                    this.hunger.setEnchanting(true);
                                    this.hunger.setBurrowed(suitable);
                                }
                            } else {
                                if (!this.hunger.getEnchanting()) {
                                    this.cacheitem = item.copy();
                                    this.cacheentity = entity;
                                    this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_STRIDER_EAT, this.hunger.getSoundCategory(), 0.8F, 0.9F);
                                    entity.remove();
                                    this.hunger.setEnchanting(true);
                                }
                                this.hunger.setBurrowed(suitable);
                            }
                        } else if (entity instanceof LivingEntity && !this.hunger.getEnchanting()) {
                            if (entity.isAlive() && this.hunger.canAttack((LivingEntity) entity)) {
                                if (!this.hunger.getEnchanting()) {
                                    this.hunger.setBurrowed(false);
                                    this.hunger.setAttacking(true);
                                    this.hunger.setEnchanting(false);
                                }
                            }
                        } else {
                            this.hunger.setBurrowed(suitable);
                        }
                    } else {
                        this.hunger.setBurrowed(suitable);
                    }
                }
            }
            if (this.cacheitem != ItemStack.EMPTY) {
                this.tick++;

                if (this.tick % 16 == 0) {
                    ItemStack noench = this.hunger.modifyEnchantments(cacheitem, cacheitem.getDamage(), 1);
                    if (noench == null) {
                        noench = cacheitem;
                        this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_FOX_SPIT, this.hunger.getSoundCategory(), 0.8F, 0.8F);
                        noench.getOrCreateChildTag("Bitten");
                        this.hunger.world.addEntity(new ItemEntity(cacheentity.world, cacheentity.getPosX(), cacheentity.getPosY(), cacheentity.getPosZ(), noench));
                    } else if (noench != ItemStack.EMPTY) {
                        this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, this.hunger.getSoundCategory(), 0.8F, 0.6F);
                        noench.getOrCreateChildTag("Bitten");
                        this.hunger.world.addEntity(new ItemEntity(cacheentity.world, cacheentity.getPosX(), cacheentity.getPosY(), cacheentity.getPosZ(), noench));
                    } else if (hasEnchantibility(cacheitem)) {
                        this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, this.hunger.getSoundCategory(), 0.8F, 0.6F);
                    }
                    this.cacheitem = ItemStack.EMPTY;
                    this.tick = 0;
                }
                this.hunger.setBurrowed(true);
                this.hunger.setEnchanting(true);
            } else {
                this.hunger.setBurrowed(suitable);
                this.hunger.setEnchanting(false);
            }
        }
    }
}
