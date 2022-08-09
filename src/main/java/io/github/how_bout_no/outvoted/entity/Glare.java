package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.util.ItemPlacementContextLiving;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.*;
import java.util.function.Predicate;

public class Glare extends PathfinderMob implements IAnimatable {
    public static final int maxLight = 7;
    public static int inventorySize = Config.glareInvSize.get();
    @Nullable
    protected BlockPos darkPos;
    private static final EntityDataAccessor<Boolean> ANGRY;
    static final Predicate<ItemEntity> PICKABLE_DROP_FILTER;

    public Glare(EntityType<? extends Glare> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
        this.setCanPickUpLoot(Config.glareShouldInteract.get());
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.isEmptyBlock(pos) ? 10.0F : 0.0F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TemptGoal(this, 1.5, Ingredient.of(Items.GLOW_BERRIES), false));
        if (Config.glareShouldInteract.get()) {
            this.goalSelector.addGoal(1, new PickupItemGoal());
            this.goalSelector.addGoal(2, new InDarkGoal());
        }
        this.goalSelector.addGoal(3, new MoveToDarkGoal());
        this.goalSelector.addGoal(4, new FindDarkSpotGoal());
        this.goalSelector.addGoal(5, new GlareWanderAroundGoal());
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.FLYING_SPEED, 0.4000000238418579D)
                .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        HealthUtil.setConfigHealth(this, Config.glareHealth.get());

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.isEmptyBlock(pos.below());
            }
        };
        birdNavigation.setCanOpenDoors(false);
        birdNavigation.setCanFloat(false);
        birdNavigation.setCanPassDoors(true);
        return birdNavigation;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsAngry", this.isAngry());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setAngry(compound.getBoolean("IsAngry"));
    }

    static {
        ANGRY = SynchedEntityData.defineId(Glare.class, EntityDataSerializers.BOOLEAN);
        PICKABLE_DROP_FILTER = (item) -> {
            if (item.getItem().getItem() instanceof BlockItem) {
                BlockState state = ((BlockItem) item.getItem().getItem()).getBlock().defaultBlockState();
                return !item.hasPickUpDelay() && item.isAlive() && state.getLightEmission() > maxLight;
            }
            return false;
        };
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGRY, Boolean.FALSE);
    }

    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.7F;
    }

    public boolean isAngry() {
        return this.entityData.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.entityData.set(ANGRY, angry);
    }

    public boolean canTakeItem(ItemStack stack) {
        EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(stack);
        if (!this.getItemBySlot(equipmentSlot).isEmpty()) {
            return false;
        } else {
            return equipmentSlot == EquipmentSlot.MAINHAND && super.canTakeItem(stack);
        }
    }

    public boolean canHoldItem(ItemStack stack) {
        return this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
    }

    private void dropItem(ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
        this.level.addFreshEntity(itemEntity);
    }

    protected void pickUpItem(ItemEntity item) {
        ItemStack itemStack = item.getItem();
        if (this.canHoldItem(itemStack) && PICKABLE_DROP_FILTER.test(item)) {
            int i = itemStack.getCount();
            if (i > inventorySize) {
                this.dropItem(itemStack.split(i - inventorySize));
            }

            this.onItemPickup(item);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemStack.split(inventorySize));
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.take(item, itemStack.getCount());
            item.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setNoGravity(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            this.setAngry(this.getLight(this.blockPosition()) <= maxLight);
        }
        if (this.isAngry() && this.tickCount % 20 == 0)
            this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getRandomX(0.75D), this.getY() + this.random.nextDouble(), this.getRandomZ(0.75D), this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());
        if (this.isAngry() && this.tickCount % 15 == 0)
            this.level.playSound(null, this, SoundEvents.AZALEA_LEAVES_FALL, SoundSource.NEUTRAL, 0.6F, Mth.lerp(this.random.nextFloat(), 0.25F, 0.75F));
    }

    void startMovingTo(BlockPos pos) {
        Vec3 vec3d = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int) vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3 vec3d2 = AirRandomPos.getPosTowards(this, k, l, i, vec3d, 0.3141592741012573D);
        if (vec3d2 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0D);
        }
    }

    public boolean hasDarkPos() {
        return this.darkPos != null;
    }

    public boolean isDarkSpot(BlockPos pos) {
        return this.getLight(pos) <= maxLight && this.level.isEmptyBlock(pos);
    }

//    boolean isGap(BlockPos pos) {
//        boolean top = this.world.getBlockState(pos.up()).isAir();
//        boolean bottom = this.world.getBlockState(pos.down()).isAir();
//        return this.world.getBlockState(pos).isAir() && (top || bottom);
//    }
//
//    BlockPos modifyPos(BlockPos pos) {
//        if (isGap(pos)) {
//            if (this.world.getBlockState(pos.up()).isAir()) {
//                return pos.up();
//            }
//        }
//        return pos;
//    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return isWithinDistance(pos, (double) distance);
    }

    boolean isWithinDistance(BlockPos pos, double distance) {
        return pos.closerThan(this.blockPosition(), distance);
    }

    boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    int getLight(BlockPos pos) {
        return this.level.getRawBrightness(pos, this.level.getSkyDarken());
    }

    class GlareWanderAroundGoal extends Goal {
        private static final int MAX_DISTANCE = 32;

        GlareWanderAroundGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return Glare.this.navigation.isDone() && Glare.this.random.nextInt(20) == 0;
        }

        public boolean canContinueToUse() {
            return Glare.this.navigation.isInProgress();
        }

        public void start() {
            Vec3 vec3d = this.getRandomLocation();
            if (vec3d != null) {
                Glare.this.navigation.moveTo(Glare.this.navigation.createPath(new BlockPos(vec3d), 1), 1.0D);
            }
        }

        @Nullable
        private Vec3 getRandomLocation() {
            Vec3 vec3d3 = Glare.this.getViewVector(0.0F);
            Vec3 vec3d4 = HoverRandomPos.getPos(Glare.this, 8, 7, vec3d3.x, vec3d3.z, 1.5707964F, 3, 1);
            return vec3d4 != null ? vec3d4 : AirAndWaterRandomPos.getPos(Glare.this, 8, 4, -2, vec3d3.x, vec3d3.z, 1.5707963705062866D);
        }
    }

    class FindDarkSpotGoal extends Goal {
        int ticks;

        FindDarkSpotGoal() {
            this.ticks = Glare.this.level.random.nextInt(30);
        }

        public boolean canUse() {
            ++this.ticks;
            return (!Glare.this.hasDarkPos() || !isDarkSpot(Glare.this.darkPos)) && this.ticks > 40;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            this.ticks = 0;
            BlockPos darkestSpot = getDarkestSpot();
            if (darkestSpot != null) {
                Glare.this.darkPos = darkestSpot;
            }
        }

        private Map<BlockPos, Integer> getNearbyDarkSpots() {
            int mult = 5;
            Map<BlockPos, Integer> map = new HashMap<>();
            for (BlockPos blockPos : BlockPos.withinManhattan(Glare.this.blockPosition(), mult, mult - 2, mult)) {
                if (isDarkSpot(blockPos))
                    map.put(blockPos.immutable(), Glare.this.getLight(blockPos));
            }
            return map;
        }

        @Nullable
        private BlockPos getDarkestSpot() {
            Map<BlockPos, Integer> map = getNearbyDarkSpots();
            BlockPos returnPos = null;
            if (!map.isEmpty()) {
                Map.Entry<BlockPos, Integer> min = Collections.min(map.entrySet(), Map.Entry.comparingByValue());
                returnPos = min.getKey();
            }
            return returnPos;
        }
    }

    class MoveToDarkGoal extends Goal {
        int ticks;

        MoveToDarkGoal() {
            this.ticks = Glare.this.level.random.nextInt(10);
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return Glare.this.darkPos != null && !Glare.this.hasRestriction() && Glare.this.blockPosition() != Glare.this.darkPos && !Glare.this.isLeashed();
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }

        public void start() {
            this.ticks = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            Glare.this.navigation.stop();
            Glare.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (Glare.this.darkPos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    Glare.this.darkPos = null;
                } else if (!Glare.this.navigation.isInProgress()) {
                    if (Glare.this.isTooFar(Glare.this.darkPos)) {
                        Glare.this.darkPos = null;
                    } else {
                        Glare.this.startMovingTo(Glare.this.darkPos);
                    }
                }
            }
        }
    }

    class InDarkGoal extends Goal {
        private int tick;

        public boolean canUse() {
            if (isDarkSpot(Glare.this.blockPosition()))
                Glare.this.darkPos = Glare.this.blockPosition();
            return Glare.this.darkPos != null && !Glare.this.hasRestriction() && Glare.this.blockPosition() == Glare.this.darkPos;
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }

        public void tick() {
            if (Glare.this.darkPos != null && tick >= 5) {
                if (!Glare.this.getMainHandItem().isEmpty() && Glare.this.getMainHandItem().getItem() instanceof BlockItem blockItem && isDarkSpot(Glare.this.blockPosition())) {
                    ItemPlacementContextLiving itemPlacementContextLiving = new ItemPlacementContextLiving(Glare.this.level, Glare.this, Glare.this.getUsedItemHand(), Glare.this.getMainHandItem(), new BlockHitResult(Vec3.atCenterOf(Glare.this.blockPosition()), Glare.this.getMotionDirection(), Glare.this.blockPosition(), !Glare.this.level.isEmptyBlock(Glare.this.blockPosition())));
                    InteractionResult actionResult = blockItem.place(itemPlacementContextLiving);
                    if (!actionResult.consumesAction()) {
                        for (Direction direction : Direction.values()) {
                            BlockPos blockPos = Glare.this.blockPosition().relative(direction);
                            if (Glare.this.level.isEmptyBlock(blockPos) && isDarkSpot(blockPos)) {
                                itemPlacementContextLiving = new ItemPlacementContextLiving(Glare.this.level, Glare.this, Glare.this.getUsedItemHand(), Glare.this.getMainHandItem(), new BlockHitResult(Vec3.atCenterOf(blockPos), Glare.this.getMotionDirection(), blockPos, !Glare.this.level.isEmptyBlock(blockPos)));
                                actionResult = blockItem.place(itemPlacementContextLiving);
                                if (actionResult.consumesAction()) break;
                            }
                        }
                    }
                    if (actionResult.consumesAction()) {
                        Glare.this.getMainHandItem().shrink(1);
                        tick = 0;
                    }
                } else if (!(Glare.this.getMainHandItem().getItem() instanceof BlockItem)) {
                    Glare.this.spawnAtLocation(Glare.this.getMainHandItem().getItem());
                    Glare.this.getMainHandItem().setCount(0);
                }
            }
            ++tick;
        }
    }

    class PickupItemGoal extends Goal {
        public PickupItemGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (!Config.glareShouldInteract.get() || !Glare.this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
                return false;
            if (Glare.this.getTarget() == null && Glare.this.getLastHurtByMob() == null) {
                if (Glare.this.getRandom().nextInt(10) != 0) {
                    return false;
                } else {
                    List<ItemEntity> list = Glare.this.level.getEntitiesOfClass(ItemEntity.class, Glare.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Glare.PICKABLE_DROP_FILTER);
                    return !list.isEmpty() && Glare.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
                }
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return (!Config.glareShouldInteract.get() || !Glare.this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && super.canContinueToUse();
        }

        public void tick() {
            List<ItemEntity> list = Glare.this.level.getEntitiesOfClass(ItemEntity.class, Glare.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Glare.PICKABLE_DROP_FILTER);
            ItemStack itemStack = Glare.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack.isEmpty() && !list.isEmpty()) {
                Glare.this.getNavigation().moveTo(list.get(0), 1.2D);
            }
        }

        public void start() {
            List<ItemEntity> list = Glare.this.level.getEntitiesOfClass(ItemEntity.class, Glare.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Glare.PICKABLE_DROP_FILTER);
            if (!list.isEmpty()) {
                Glare.this.getNavigation().moveTo(list.get(0), 1.2D);
            }
        }
    }

    private static boolean isSuitableSpawn(LevelAccessor world, BlockPos pos, Random random) {
        int mult = 5;
        for (BlockPos blockPos : BlockPos.withinManhattan(pos, mult, mult, mult)) {
            BlockState state = world.getBlockState(blockPos);
            if (state.getFluidState().is(FluidTags.LAVA)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canSpawn(EntityType<Glare> entity, LevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, Random random) {
        return !world.canSeeSky(blockPos) && isSuitableSpawn(world, blockPos, random) && blockPos.getY() < 63 && checkMobSpawnRules(entity, world, spawnReason, blockPos, random);
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        GeckoLibCache.getInstance().parser.setValue("mult", this.isAngry() ? 0.6F : 0.2F);
        event.getController().setAnimation(new AnimationBuilder().addAnimation("living", true));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<Glare> controller = new AnimationController<>(this, "controller", 2, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
