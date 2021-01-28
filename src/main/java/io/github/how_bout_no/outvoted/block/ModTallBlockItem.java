package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.TallBlockItem;

import java.util.ArrayList;
import java.util.Collection;

public class ModTallBlockItem extends TallBlockItem {
    public ModTallBlockItem(Block blockIn, Properties properties) {
        super(blockIn, properties.group(Outvoted.TAB_DECO));
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        Collection<ItemGroup> groups = new ArrayList<>();
        groups.add(Outvoted.TAB_DECO);
        groups.add(ItemGroup.SEARCH);
        return groups;
    }
}
