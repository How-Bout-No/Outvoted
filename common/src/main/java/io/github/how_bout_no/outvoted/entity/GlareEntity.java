package io.github.how_bout_no.outvoted.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.*;

public class GlareEntity extends PathAwareEntity implements IAnimatable {
    private GlareEntity.MoveToDarkGoal moveToDarkGoal;
    @Nullable
    BlockPos darkPos;

    public GlareEntity(EntityType<? extends GlareEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.lookControl = new LookControl(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
        this.setNoGravity(true);
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
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);

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

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
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

    @Nullable
    public BlockPos getDarkPos() {
        return this.darkPos;
    }

    public boolean hasDarkPos() {
        return this.darkPos != null;
    }

    public void setDarkPos(BlockPos pos) {
        this.darkPos = pos;
    }

    public boolean isDarkSpot(BlockPos pos) {
        return this.world.getLightLevel(LightType.BLOCK, pos) <= 8 && this.world.getBlockState(pos).isSolidBlock(this.world, pos.down()) && ((this.world.isNight() || this.world.isThundering()) || !this.world.isSkyVisible(pos.up()));
    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), (double) distance);
    }

    boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
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

        FindDarkSpotGoal() {
            super();
        }

        public boolean canStart() {
            return (!GlareEntity.this.hasDarkPos() || !isDarkSpot(GlareEntity.this.darkPos)) && cachePos != GlareEntity.this.getBlockPos();
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            BlockPos darkestSpot = getDarkestSpot();
            if (darkestSpot != null) {
                GlareEntity.this.darkPos = darkestSpot;
            }
        }

        private Map<BlockPos, Integer> getNearbyDarkSpots() {
            int mult = 5;
            Map<BlockPos, Integer> map = new HashMap<>();
            for (BlockPos blockPos : BlockPos.iterateOutwards(GlareEntity.this.getBlockPos(), mult, mult, mult)) {
                if (isDarkSpot(blockPos))
                    map.put(blockPos.toImmutable(), GlareEntity.this.world.getLightLevel(LightType.BLOCK, blockPos));
            }
            System.out.println(map);
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
        private static final int MAX_DARK_NAVIGATION_TICKS = 600;
        int ticks;

        MoveToDarkGoal() {
            super();
            this.ticks = GlareEntity.this.world.random.nextInt(10);
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return GlareEntity.this.darkPos != null && !GlareEntity.this.hasPositionTarget() && !GlareEntity.this.isWithinDistance(GlareEntity.this.darkPos, 10);
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

    public static boolean canSpawn(EntityType<GlareEntity> entity, WorldAccess world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return canMobSpawn(entity, world, spawnReason, blockPos, random);
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
