package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CaveVines;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.NoWaterTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
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

public class GlareEntity extends PathAwareEntity implements IAnimatable {
    public static final int maxLight = 7;
    private GlareEntity.MoveToDarkGoal moveToDarkGoal;
    @Nullable
    protected BlockPos darkPos;
    private static final TrackedData<Boolean> ANGRY;

    public GlareEntity(EntityType<? extends GlareEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.lookControl = new LookControl(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    protected void initGoals() {
        this.moveToDarkGoal = new GlareEntity.MoveToDarkGoal();
        this.goalSelector.add(1, this.moveToDarkGoal);
        this.goalSelector.add(2, new GlareEntity.FindDarkSpotGoal());
        this.goalSelector.add(3, new GlareEntity.GlareWanderAroundGoal());
        this.goalSelector.add(4, new SwimGoal(this));
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4000000238418579D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, @Nullable EntityData spawnDataIn, @Nullable NbtCompound dataTag) {
        HealthUtil.setConfigHealth(this, Outvoted.commonConfig.entities.glare.health);

        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("IsAngry", this.isAngry());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setAngry(compound.getBoolean("IsAngry"));
    }

    static {
        ANGRY = DataTracker.registerData(GlareEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANGRY, Boolean.FALSE);
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.0F;
    }

    public boolean isAngry() {
        return this.dataTracker.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.dataTracker.set(ANGRY, angry);
    }

    @Override
    public void tick() {
        super.tick();
        this.setNoGravity(true);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            this.setAngry(this.getLight(this.getBlockPos()) <= maxLight);
        }
        if (this.isAngry() && this.age % 20 == 0)
            this.world.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getParticleX(0.75D), this.getY() + this.random.nextDouble(), this.getParticleZ(0.75D), this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());
        if (this.isAngry() && this.age % 15 == 0)
            this.world.playSoundFromEntity(null, this, SoundEvents.BLOCK_AZALEA_LEAVES_FALL, SoundCategory.NEUTRAL, 0.6F, lerp(0.25F, 0.75F, this.random.nextFloat()));
    }

    float lerp(float a, float b, float f) {
        return (a * (1.0F - f)) + (b * f);
    }

    void startMovingTo(BlockPos pos) {
        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
        int i = 0;
        BlockPos blockPos = this.getBlockPos();
        int j = (int) vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.getManhattanDistance(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3d vec3d2 = NoWaterTargeting.find(this, k, l, i, vec3d, 0.3141592741012573D);
        if (vec3d2 != null) {
            this.navigation.setRangeMultiplier(0.5F);
            this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0D);
        }
    }

    public boolean hasDarkPos() {
        return this.darkPos != null;
    }

    public boolean isDarkSpot(BlockPos pos) {
        return this.getLight(pos) <= maxLight && this.isGap(pos) && !this.world.isSkyVisible(pos.up());
    }

    boolean isGap(BlockPos pos) {
        boolean top = this.world.getBlockState(pos.up()).isAir();
        boolean bottom = this.world.getBlockState(pos.down()).isAir();
        return this.world.getBlockState(pos).isAir() && (top || bottom);
    }

    BlockPos modifyPos(BlockPos pos) {
        if (isGap(pos)) {
            if (this.world.getBlockState(pos.up()).isAir()) {
                return pos.up();
            }
        }
        return pos;
    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return isWithinDistance(pos, (double) distance);
    }

    boolean isWithinDistance(BlockPos pos, double distance) {
        return pos.isWithinDistance(this.getBlockPos(), distance);
    }

    boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    int getLight(BlockPos pos) {
        return this.world.getBaseLightLevel(pos, this.world.getAmbientDarkness());
    }

    class GlareWanderAroundGoal extends Goal {
        private static final int MAX_DISTANCE = 22;

        GlareWanderAroundGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return GlareEntity.this.navigation.isIdle() && GlareEntity.this.random.nextInt(20) == 0;
        }

        public boolean shouldContinue() {
            return GlareEntity.this.navigation.isFollowingPath();
        }

        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                GlareEntity.this.navigation.startMovingAlong(GlareEntity.this.navigation.findPathTo(new BlockPos(vec3d), 1), 1.0D);
            }
        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d3 = GlareEntity.this.getRotationVec(0.0F);
            Vec3d vec3d4 = AboveGroundTargeting.find(GlareEntity.this, 8, 7, vec3d3.x, vec3d3.z, 1.5707964F, 3, 1);
            return vec3d4 != null ? vec3d4 : NoPenaltySolidTargeting.find(GlareEntity.this, 8, 4, -2, vec3d3.x, vec3d3.z, 1.5707963705062866D);
        }
    }

    class FindDarkSpotGoal extends Goal {
        BlockPos cachePos;
        int ticks;

        FindDarkSpotGoal() {
            super();
            this.ticks = GlareEntity.this.world.random.nextInt(20) + 20;
        }

        public boolean canStart() {
            ++this.ticks;
            return (!GlareEntity.this.hasDarkPos() || !isDarkSpot(GlareEntity.this.darkPos)) && cachePos != GlareEntity.this.getBlockPos() && this.ticks > 40;
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            this.ticks = 0;
            BlockPos darkestSpot = getDarkestSpot();
            if (darkestSpot != null) {
                GlareEntity.this.darkPos = darkestSpot;
            }
        }

        private Map<BlockPos, Integer> getNearbyDarkSpots() {
            int mult = 5;
            Map<BlockPos, Integer> map = new HashMap<>();
            for (BlockPos blockPos : BlockPos.iterateOutwards(GlareEntity.this.getBlockPos(), mult, mult, mult)) {
                BlockPos blockPos1 = modifyPos(blockPos);
                if (isDarkSpot(blockPos1) && blockPos1.getY() >= 4)
                    map.put(blockPos1.toImmutable(), GlareEntity.this.getLight(blockPos1));
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
            super();
            this.ticks = GlareEntity.this.world.random.nextInt(10);
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return GlareEntity.this.darkPos != null && !GlareEntity.this.hasPositionTarget() && GlareEntity.this.getBlockPos() != GlareEntity.this.darkPos && !GlareEntity.this.isLeashed();
        }

        public boolean shouldContinue() {
            return this.canStart();
        }

        public void start() {
            this.ticks = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            GlareEntity.this.navigation.stop();
            GlareEntity.this.navigation.resetRangeMultiplier();
        }

        public void tick() {
            if (GlareEntity.this.darkPos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    GlareEntity.this.darkPos = null;
                } else if (!GlareEntity.this.navigation.isFollowingPath()) {
                    if (GlareEntity.this.isTooFar(GlareEntity.this.darkPos)) {
                        GlareEntity.this.darkPos = null;
                    } else {
                        GlareEntity.this.startMovingTo(GlareEntity.this.darkPos);
                    }
                }
            }
        }
    }

    private static boolean isSuitableSpawn(WorldAccess world, BlockPos pos, Random random) {
        int mult = 5;
        for (BlockPos blockPos : BlockPos.iterateOutwards(pos, mult, mult, mult)) {
            BlockState state = world.getBlockState(blockPos);
            if (state.getFluidState().isIn(FluidTags.LAVA)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canSpawn(EntityType<GlareEntity> entity, WorldAccess world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return !world.isSkyVisible(blockPos) && isSuitableSpawn(world, blockPos, random) && blockPos.getY() < world.getTopY() - 5 && canMobSpawn(entity, world, spawnReason, blockPos, random);
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        GeckoLibCache.getInstance().parser.setValue("mult", this.isAngry() ? 0.75F : 0.25F);
        event.getController().setAnimation(new AnimationBuilder().addAnimation("living", true));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<GlareEntity> controller = new AnimationController<>(this, "controller", 2, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
