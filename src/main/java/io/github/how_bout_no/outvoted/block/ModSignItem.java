package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;

import java.util.ArrayList;
import java.util.Collection;

public class ModSignItem extends SignItem {

    public ModSignItem(Properties properties, Block floorBlockIn, Block wallBlockIn) {
        super(properties.maxStackSize(16).group(Outvoted.TAB_DECO), floorBlockIn, wallBlockIn);
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        Collection<ItemGroup> groups = new ArrayList<>();
        groups.add(Outvoted.TAB_DECO);
        groups.add(ItemGroup.SEARCH);
        return groups;
    }
}
