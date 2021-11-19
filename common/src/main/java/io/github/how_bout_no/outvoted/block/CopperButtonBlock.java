package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class CopperButtonBlock extends BaseCopperButtonBlock {
    public CopperButtonBlock(OxidizationLevel oxidizationLevel, Settings settings) {
        super(oxidizationLevel, settings);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    public boolean hasRandomTicks(BlockState state) {
        return IOxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }
}
