package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.IMixinPillarBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

public class TermiteEntity extends HostileEntity implements IAnimatable {
    public TermiteEntity(EntityType<? extends TermiteEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(5, new TermiteEntity.WanderAndInfestGoal(this));
        this.targetSelector.add(1, (new RevengeGoal(this, TermiteEntity.class)));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D);
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, @Nullable EntityData spawnDataIn, @Nullable NbtCompound dataTag) {
        HealthUtil.setConfigHealth(this, Outvoted.commonConfig.entities.termite.health);

        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public double getHeightOffset() {
        return 0.1D;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.13F;
    }

    protected boolean canClimb() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    public void tick() {
        this.bodyYaw = this.yaw;
        super.tick();
    }

    public void setBodyYaw(float bodyYaw) {
        this.yaw = bodyYaw;
        super.setBodyYaw(bodyYaw);
    }

//    public float getPathfindingFavor(BlockPos pos, WorldView world) {
//        return NestedBlock.isNestable(world.getBlockState(pos.down())) ? 10.0F : super.getPathfindingFavor(pos, world);
//    }

    public static boolean canSpawn(EntityType<TermiteEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (canSpawnIgnoreLightLevel(type, world, spawnReason, pos, random)) {
            PlayerEntity playerEntity = world.getClosestPlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0D, true);
            return playerEntity == null;
        } else {
            return false;
        }
    }

    public boolean isValidBlock(Block block) {
        return block.isIn(BlockTags.LOGS_THAT_BURN);
    }

    public boolean isValidBlock(BlockState blockState) {
        return isValidBlock(blockState.getBlock()) && blockState.get(Nest.NESTED) < 3;
    }

    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    static class WanderAndInfestGoal extends WanderAroundGoal {
        private Direction direction;
        private boolean canInfest;

        public WanderAndInfestGoal(TermiteEntity entity) {
            super(entity, 1.0D, 10);
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            if (this.mob.getTarget() != null) {
                return false;
            } else if (!this.mob.getNavigation().isIdle()) {
                return false;
            } else {
                Random random = this.mob.getRandom();
                if (this.mob.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && random.nextInt(10) == 0) {
                    this.direction = Direction.random(random);
                    BlockPos blockPos = (new BlockPos(this.mob.getX(), this.mob.getY(), this.mob.getZ())).offset(this.direction);
                    BlockState blockState = this.mob.world.getBlockState(blockPos);
                    if (((TermiteEntity) this.mob).isValidBlock(blockState)) {
                        this.canInfest = true;
                        return true;
                    }
                }

                this.canInfest = false;
                return super.canStart();
            }
        }

        public boolean shouldContinue() {
            return !this.canInfest && super.shouldContinue();
        }

        public void start() {
            if (!this.canInfest) {
                super.start();
            } else {
                WorldAccess worldAccess = this.mob.world;
                BlockPos blockPos = (new BlockPos(this.mob.getX(), this.mob.getY(), this.mob.getZ())).offset(this.direction);
                System.out.println(blockPos);
                BlockState blockState = worldAccess.getBlockState(blockPos);
                if (((TermiteEntity) this.mob).isValidBlock(blockState)) {
                    worldAccess.setBlockState(blockPos, blockState.with(Nest.NESTED, blockState.get(Nest.NESTED) + 1), 3);
                    this.mob.stopRiding();
                    this.mob.removeAllPassengers();
                    NbtCompound compoundTag = new NbtCompound();
                    this.mob.saveNbt(compoundTag);
                    ((IMixinPillarBlock) blockState.getBlock()).addTermite(compoundTag);
                    this.mob.remove();
                }

            }
        }
    }

    public static class Nest {
        public static final IntProperty NESTED;

        static {
//            NESTED = IntProperty.of("nested", 0, Outvoted.commonConfig.entities.termite.maxCount);
            NESTED = IntProperty.of("nested", 0, 3);
        }
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
