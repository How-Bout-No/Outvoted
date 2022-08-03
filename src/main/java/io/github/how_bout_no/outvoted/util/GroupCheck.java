package io.github.how_bout_no.outvoted.util;

import net.minecraft.world.item.CreativeModeTab;

public interface GroupCheck {
    static boolean isIn(CreativeModeTab input, CreativeModeTab check) {
        return input == check || input == CreativeModeTab.TAB_SEARCH;
    }
}