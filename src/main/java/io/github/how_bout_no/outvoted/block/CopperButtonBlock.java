package io.github.how_bout_no.outvoted.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Random;

public class CopperButtonBlock extends BaseCopperButtonBlock {
    public CopperButtonBlock(WeatherState weatherState, Properties settings) {
        super(weatherState, settings);
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (!state.getValue(POWERED)) {
            this.onRandomTick(state, world, pos, random);
        }
    }

    public boolean isRandomlyTicking(BlockState state) {
        return IOxidizable.getNext(state.getBlock()).isPresent();
    }

    // Don't know if below is needed at all, but I'll keep it so as not to break anything
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (state.getValue(POWERED)) {
            world.setBlock(pos, state.setValue(POWERED, false), 3);
            this.updateNeighbours(state, world, pos);
            this.playSound(null, world, pos, false);
            world.gameEvent(GameEvent.BLOCK_UNPRESS, pos);
        }
    }

    private void updateNeighbours(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, this);
        world.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
    }
}
