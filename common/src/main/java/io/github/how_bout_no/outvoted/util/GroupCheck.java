package io.github.how_bout_no.outvoted.util;

import net.minecraft.item.ItemGroup;

public final class GroupCheck {
    public static boolean isIn(ItemGroup input, ItemGroup check) {
        return input == check || input == ItemGroup.SEARCH;
    }
}