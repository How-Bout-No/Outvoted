package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.item.ItemGroup;

public interface GroupCheck {
    static boolean isIn(ItemGroup input, ItemGroup check) {
        return input == check || input == ItemGroup.SEARCH;
    }
}