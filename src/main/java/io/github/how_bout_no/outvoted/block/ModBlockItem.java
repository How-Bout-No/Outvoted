package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.Collection;

public class ModBlockItem extends BlockItem {
    private int type = 0;

    public ModBlockItem(Block blockIn, Properties properties) {
        super(blockIn, properties.group(Outvoted.TAB_BLOCKS));
    }

    /* Lazy and dumb method of sectioning off saplings and blocks :P */
    public ModBlockItem(Block blockIn, Properties properties, boolean sapling) {
        super(blockIn, properties.group(Outvoted.TAB_DECO));
        type = 1;
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        Collection<ItemGroup> groups = new ArrayList<>();
        groups.add(type == 0 ? Outvoted.TAB_BLOCKS : Outvoted.TAB_DECO);
        groups.add(ItemGroup.SEARCH);
        return groups;
    }
}
