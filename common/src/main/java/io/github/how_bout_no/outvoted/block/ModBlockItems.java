package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.item.TallBlockItem;

/* I don't know if splitting it up into a bunch of inner classes is better or worse, I just don't like
 * having a clutter of files and idk any smoother implementation :v */
public class ModBlockItems {
    public static class ModBlockItem extends BlockItem {
        public ModBlockItem(Block blockIn, Settings properties) {
            super(blockIn, properties);
        }

        @Override
        protected boolean isIn(ItemGroup group) {
            return GroupCheck.isIn(group, Outvoted.TAB_BLOCKS);
        }
    }

    public static class ModDecoBlockItem extends ModBlockItem {
        public ModDecoBlockItem(Block blockIn, Settings properties) {
            super(blockIn, properties);
        }

        @Override
        protected boolean isIn(ItemGroup group) {
            return GroupCheck.isIn(group, Outvoted.TAB_DECO);
        }
    }

    public static class ModTallBlockItem extends TallBlockItem {
        public ModTallBlockItem(Block blockIn, Settings properties) {
            super(blockIn, properties);
        }

        @Override
        protected boolean isIn(ItemGroup group) {
            return GroupCheck.isIn(group, Outvoted.TAB_REDSTONE);
        }
    }

    public static class ModSignItem extends SignItem {
        public ModSignItem(Settings properties, Block floorBlockIn, Block wallBlockIn) {
            super(properties.maxCount(16), floorBlockIn, wallBlockIn);
        }

        @Override
        protected boolean isIn(ItemGroup group) {
            return GroupCheck.isIn(group, Outvoted.TAB_DECO);
        }
    }
}
