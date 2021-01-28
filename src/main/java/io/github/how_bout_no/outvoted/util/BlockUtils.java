package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.Map;

/*
Forge threw a hissy fit with this in ServerEvents so it's its own class now
 */
class BlockUtils {
    static class Stripping {
        public static Map<Block, Block> BLOCK_STRIPPING_MAP = new HashMap<>();

        static {
            BLOCK_STRIPPING_MAP.put(ModBlocks.PALM_LOG.get(), ModBlocks.STRIPPED_PALM_LOG.get());
            BLOCK_STRIPPING_MAP.put(ModBlocks.PALM_WOOD.get(), ModBlocks.STRIPPED_PALM_WOOD.get());
        }
    }
}
