package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/*
  This class only exists to make the Creative Tab config option work with normal items, however this can be easily expanded
 */
public class ModItem extends Item {
    public ModItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isIn(group, Outvoted.TAB_MISC);
    }
}