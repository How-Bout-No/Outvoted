package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import io.github.how_bout_no.outvoted.entity.Meerkat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class BurrowBlock extends BaseEntityBlock {
    public static DirectionProperty FACING;

    public BurrowBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new BurrowBlockEntity(arg, arg2);
    }

    @Nullable
    private Direction getNewDirection(BlockPos pos, LevelAccessor world) {
        Direction opening = world.getBlockState(pos).getValue(FACING);
        if (opening != null && opening != Direction.DOWN)
            return null;
        for (Direction direction1 : Direction.values()) {
            if (direction1 != Direction.DOWN) {
                if (world.getBlockState(pos.relative(direction1)).isAir()) {
                    return direction1;
                }
            }
        }
        world.setBlock(pos, Blocks.SAND.defaultBlockState(), 0);
        return null;
    }

    private void refreshDirection(BlockPos pos, LevelAccessor world) {
        Direction direction = getNewDirection(pos, world);
        if (direction != null)
            world.setBlock(pos, world.getBlockState(pos).setValue(FACING, direction), 18);
    }


    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(oldState.getBlock())) {
            refreshDirection(pos, world);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        refreshDirection(pos, world);

        return super.updateShape(state, direction, newState, world, pos, posFrom);
    }

    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClientSide && blockEntity instanceof BurrowBlockEntity) {
            if (!((BurrowBlockEntity) blockEntity).isEmpty()) {
                for (Tag tag1 : ((BurrowBlockEntity) blockEntity).getMeerkats()) {
                    CompoundTag tag = ((CompoundTag) tag1).getCompound("EntityData");
                    Entity entity = EntityType.loadEntityRecursive(tag, world, (entityx) -> entityx);
                    if (entity != null) {
                        if (entity instanceof Meerkat) {
                            Meerkat meerkat = (Meerkat) entity;

                            this.ageMeerkat(((CompoundTag) tag1).getInt("TicksInBurrow"), meerkat);

                            entity.moveTo(pos, state.getValue(FACING).toYRot(), entity.getXRot());
                        }
                        world.addFreshEntity(entity);
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

        super.playerWillDestroy(world, pos, state, player);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private void ageMeerkat(int ticks, Meerkat meerkat) {
        int i = meerkat.getAge();
        if (i < 0) {
            meerkat.setAge(Math.min(0, i + ticks));
        } else if (i > 0) {
            meerkat.setAge(Math.max(0, i - ticks));
        }

        meerkat.setInLoveTime(Math.max(0, meerkat.getInLoveTime() - ticks));
    }

    static {
        FACING = DirectionalBlock.FACING;
    }
}

