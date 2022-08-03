package io.github.how_bout_no.outvoted.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;

public class ModButtonBlock extends ButtonBlock {
    public ModButtonBlock(Properties settings) {
        this(false, settings);
    }

    public ModButtonBlock(boolean bl, Properties settings) {
        super(bl, settings);
    }

    @Override
    protected SoundEvent getSound(boolean powered) {
        return powered ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}
