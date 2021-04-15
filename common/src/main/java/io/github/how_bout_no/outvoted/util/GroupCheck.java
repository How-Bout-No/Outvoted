package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.item.ItemGroup;

public interface GroupCheck {
    static boolean isInCombat(ItemGroup group) {
        return group == Outvoted.TAB_COMBAT || group == ItemGroup.SEARCH;
    }

    static boolean isInMisc(ItemGroup group) {
        return group == Outvoted.TAB_MISC || group == ItemGroup.SEARCH;
    }

    static boolean isInBlocks(ItemGroup group) {
        return group == Outvoted.TAB_BLOCKS || group == ItemGroup.SEARCH;
    }

    static boolean isInDeco(ItemGroup group) {
        return group == Outvoted.TAB_DECO || group == ItemGroup.SEARCH;
    }

    static boolean isInRedstone(ItemGroup group) {
        return group == Outvoted.TAB_REDSTONE || group == ItemGroup.SEARCH;
    }
}