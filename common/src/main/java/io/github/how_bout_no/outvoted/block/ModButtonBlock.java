package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class ModButtonBlock extends AbstractButtonBlock {
    public ModButtonBlock(Settings settings) {
        this(false, settings);
    }

    public ModButtonBlock(boolean bl, Settings settings) {
        super(bl, settings);
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return powered ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    }
}
