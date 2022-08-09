package io.github.how_bout_no.outvoted.block;

import net.minecraft.world.level.block.WeatheringCopper;

/**
 * Base class is made for the waxed buttons (no random tick implementation)
 */
public class BaseCopperButtonBlock extends ModButtonBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public BaseCopperButtonBlock(WeatherState oxidizationLevel, Properties settings) {
        super(settings);
        this.weatherState = oxidizationLevel;
    }

    @Override
    public int getPressDuration() {
        return 10 * (this.getAge().ordinal() + 1);
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
