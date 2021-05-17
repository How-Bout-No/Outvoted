package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
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
    public BlockEntity createBlockEntity(BlockView world) {
        return new BurrowBlockEntity();
    }

//    @Nullable
//    private Direction getNewDirection(BlockPos pos, WorldAccess world) {
//        Direction opening = world.getBlockState(pos).get(FACING);
//        if (opening != null)
//            if (world.getBlockState(pos.offset(opening)).isAir())
//                return null;
//        for (Direction direction1 : Direction.values()) {
//            if (direction1 != Direction.DOWN) {
//                if (world.getBlockState(pos.offset(direction1)).isAir()) {
//                    return direction1;
//                }
//            }
//        }
//        world.setBlockState(pos, Blocks.SAND.getDefaultState(), 0);
//        return null;
//    }
//
//    private void refreshDirection(BlockPos pos, WorldAccess world) {
//        Direction direction = getNewDirection(pos, world);
//        if (direction != null)
//            world.setBlockState(pos, world.getBlockState(pos).with(FACING, direction), 18);
//    }
//
//    @Override
//    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
//        if (!state.isOf(oldState.getBlock())) {
//            refreshDirection(pos, world);
//        }
//    }
//
//    @Override
//    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
//        refreshDirection(pos, world);
//
//        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
//    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClient && blockEntity instanceof BurrowBlockEntity) {
            if (!((BurrowBlockEntity) blockEntity).hasNoMeerkats()) {
                for (Tag tag : ((BurrowBlockEntity) blockEntity).getMeerkats()) {
                    MeerkatEntity meerkatEntity = ModEntityTypes.MEERKAT.get().create(world);
                    meerkatEntity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
                    meerkatEntity.initialize((ServerWorldAccess) world, world.getLocalDifficulty(pos), SpawnReason.DISPENSER, (EntityData) ((CompoundTag) tag).get("EntityData"), (CompoundTag) null);
                    world.spawnEntity(meerkatEntity);
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

    static {
        FACING = FacingBlock.FACING;
    }
}
