/*
  This class only exists to make the Creative Tab config option work with normal items, however this can be easily expanded
 */

package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.Collection;

public class ModItem extends Item {

    public ModItem(Properties properties) {
        super(properties.group(Outvoted.TAB_MISC));
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        Collection<ItemGroup> groups = new ArrayList<>();
        groups.add(Outvoted.TAB_MISC);
        groups.add(ItemGroup.SEARCH);
        return groups;
    }
}