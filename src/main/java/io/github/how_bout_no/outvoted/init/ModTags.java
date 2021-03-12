package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class ModTags {
    public static final ITag.INamedTag<Block> HUNGER_CAN_BURROW = BlockTags.makeWrapperTag(Outvoted.MOD_ID + ":hunger_can_burrow");
}
