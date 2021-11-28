package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Random;

public class CopperButtonBlock extends BaseCopperButtonBlock {
    public CopperButtonBlock(OxidizationLevel oxidizationLevel, Settings settings) {
        super(oxidizationLevel, settings);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(POWERED)) {
            this.tickDegradation(state, world, pos, random);
        }
    }

    public boolean hasRandomTicks(BlockState state) {
        return IOxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    // Don't know if below is needed at all, but I'll keep it so as not to break anything
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, false), 3);
            this.updateNeighbors(state, world, pos);
            this.playClickSound(null, world, pos, false);
            world.emitGameEvent(GameEvent.BLOCK_UNPRESS, pos);
        }
    }

    private void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, this);
        world.updateNeighborsAlways(pos.offset(getDirection(state).getOpposite()), this);
    }
}
