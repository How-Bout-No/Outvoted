package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.init.ModBlockEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class BurrowBlock extends BlockWithEntity {
    public static DirectionProperty FACING;

    public BurrowBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BurrowBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntityTypes.BURROW.get(), BurrowBlockEntity::serverTick);
    }

    @Nullable
    private Direction getNewDirection(BlockPos pos, WorldAccess world) {
        Direction opening = world.getBlockState(pos).get(FACING);
        if (opening != null && opening != Direction.DOWN)
            return null;
        for (Direction direction1 : Direction.values()) {
            if (direction1 != Direction.DOWN) {
                if (world.getBlockState(pos.offset(direction1)).isAir()) {
                    return direction1;
                }
            }
        }
        world.setBlockState(pos, Blocks.SAND.getDefaultState(), 0);
        return null;
    }

    private void refreshDirection(BlockPos pos, WorldAccess world) {
        Direction direction = getNewDirection(pos, world);
        if (direction != null)
            world.setBlockState(pos, world.getBlockState(pos).with(FACING, direction), 18);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(oldState.getBlock())) {
            refreshDirection(pos, world);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        refreshDirection(pos, world);

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClient && blockEntity instanceof BurrowBlockEntity) {
            if (!((BurrowBlockEntity) blockEntity).hasNoMeerkats()) {
                for (NbtElement tag1 : ((BurrowBlockEntity) blockEntity).getMeerkats()) {
                    NbtCompound tag = ((NbtCompound) tag1).getCompound("EntityData");
                    Entity entity = EntityType.loadEntityWithPassengers(tag, world, (entityx) -> {
                        return entityx;
                    });
                    if (entity != null) {
                        if (entity instanceof MeerkatEntity) {
                            MeerkatEntity meerkatEntity = (MeerkatEntity) entity;

                            this.ageMeerkat(((NbtCompound) tag1).getInt("TicksInBurrow"), meerkatEntity);

                            entity.refreshPositionAndAngles(pos, state.get(FACING).asRotation(), entity.getPitch());
                        }
                        world.spawnEntity(entity);
                    }
//                    NbtCompound tag = ((NbtCompound) tag1);
//                    MeerkatEntity meerkatEntity = ModEntityTypes.MEERKAT.get().create(world);
//                    meerkatEntity.refreshPositionAndAngles(pos, state.get(FACING).asRotation(), 0.0F);
//                    meerkatEntity.readCustomDataFromTag(tag.getCompound("EntityData"));
//                    meerkatEntity.age += tag.getInt("TicksInBurrow");
//                    world.spawnEntity(meerkatEntity);
//                    meerkatEntity.setHealth(tag.getCompound("EntityData").getFloat("Health")); // Sometimes health doesn't get applied from tag, idk why
                }
            }
        }

        super.onBreak(world, pos, state, player);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private void ageMeerkat(int ticks, MeerkatEntity meerkat) {
        int i = meerkat.getBreedingAge();
        if (i < 0) {
            meerkat.setBreedingAge(Math.min(0, i + ticks));
        } else if (i > 0) {
            meerkat.setBreedingAge(Math.max(0, i - ticks));
        }

        meerkat.setLoveTicks(Math.max(0, meerkat.getLoveTicks() - ticks));
    }

    static {
        FACING = FacingBlock.FACING;
    }
}
