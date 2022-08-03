package io.github.how_bout_no.outvoted.block;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base class is made for the waxed buttons (no random tick implementation)
 */
public class BaseCopperButtonBlock extends ModButtonBlock implements IOxidizable {
    private final WeatherState weatherState;

    public BaseCopperButtonBlock(WeatherState oxidizationLevel, Properties settings) {
        super(settings);
        this.weatherState = oxidizationLevel;
    }

    @Override
    public int getPressDuration() {
        return 10 * (this.getAge().ordinal() + 1);
    }

    public BlockState getStateWithPropertiesNoPower(BlockState state) {
        BlockState blockState = state.setValue(ButtonBlock.POWERED, false);
        return withPropertiesOf(blockState);
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
