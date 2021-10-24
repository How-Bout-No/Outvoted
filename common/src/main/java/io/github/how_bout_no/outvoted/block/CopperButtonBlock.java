package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class CopperButtonBlock extends ModButtonBlock implements IOxidizable {
    private final OxidizationLevel oxidizationLevel;

    public CopperButtonBlock(OxidizationLevel oxidizationLevel, Settings settings) {
        super(false, settings);
        this.oxidizationLevel = oxidizationLevel;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    public boolean hasRandomTicks(BlockState state) {
        return IOxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    public OxidizationLevel getDegradationLevel() {
        return this.oxidizationLevel;
    }

    @Override
    protected int getPressTicks() {
        return 10 * (this.getDegradationLevel().ordinal() + 1);
    }
}
