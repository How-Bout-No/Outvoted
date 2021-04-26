package io.github.how_bout_no.outvoted.init;

import net.minecraft.block.entity.BlockEntityType;

import java.util.HashSet;

public class ModSigns {
    public static void init() {
        BlockEntityType.SIGN.blocks = new HashSet<>(BlockEntityType.SIGN.blocks);
        BlockEntityType.SIGN.blocks.add(ModBlocks.PALM_SIGN.get());
        BlockEntityType.SIGN.blocks.add(ModBlocks.PALM_WALL_SIGN.get());
        BlockEntityType.SIGN.blocks.add(ModBlocks.BAOBAB_SIGN.get());
        BlockEntityType.SIGN.blocks.add(ModBlocks.BAOBAB_WALL_SIGN.get());
    }
}
