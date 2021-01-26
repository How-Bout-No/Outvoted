package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.Collection;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block blockIn, Properties properties) {
        super(blockIn, properties.group(Outvoted.TAB_MISC));
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        Collection<ItemGroup> groups = new ArrayList<>();
        groups.add(Outvoted.TAB_MISC);
        groups.add(ItemGroup.SEARCH);
        return groups;
    }
}
