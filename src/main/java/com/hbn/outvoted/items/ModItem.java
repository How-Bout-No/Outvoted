/**
 * This class only exists to make the Creative Tab config option work, however this can be easily expanded
 */

package com.hbn.outvoted.items;

import com.hbn.outvoted.Outvoted;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.Collection;
import java.util.Collections;

public class ModItem extends Item {
    public ModItem(Properties properties) {
        super(properties);
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        return Collections.singletonList(Outvoted.TAB_MISC);
    }
}
