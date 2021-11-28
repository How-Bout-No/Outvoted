package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;

/**
 * Base class is made for the waxed buttons (no random tick implementation)
 */
public class BaseCopperButtonBlock extends ModButtonBlock implements IOxidizable {
    private final OxidizationLevel oxidizationLevel;

    public BaseCopperButtonBlock(OxidizationLevel oxidizationLevel, Settings settings) {
        super(settings);
        this.oxidizationLevel = oxidizationLevel;
    }

    public OxidizationLevel getDegradationLevel() {
        return this.oxidizationLevel;
    }

    @Override
    protected int getPressTicks() {
        return 10 * (this.getDegradationLevel().ordinal() + 1);
    }

    public BlockState getStateWithPropertiesNoPower(BlockState state) {
        BlockState blockState = state.with(AbstractButtonBlock.POWERED, false);
        return getStateWithProperties(blockState);
    }
}
