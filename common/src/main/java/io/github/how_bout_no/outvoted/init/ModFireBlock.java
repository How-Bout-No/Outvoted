package io.github.how_bout_no.outvoted.init;

import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;

public class ModFireBlock {
    public static void init() {
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        fireblock.registerFlammableBlock(ModBlocks.PALM_LOG.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.PALM_WOOD.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.STRIPPED_PALM_LOG.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.STRIPPED_PALM_WOOD.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.PALM_PLANKS.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.PALM_SLAB.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.PALM_STAIRS.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.PALM_FENCE.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.PALM_FENCE_GATE.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.PALM_LEAVES.get(), 30, 60);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_LOG.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_WOOD.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.STRIPPED_BAOBAB_LOG.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.STRIPPED_BAOBAB_WOOD.get(), 5, 5);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_PLANKS.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_SLAB.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_STAIRS.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_FENCE.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_FENCE_GATE.get(), 5, 20);
        fireblock.registerFlammableBlock(ModBlocks.BAOBAB_LEAVES.get(), 30, 60);
    }
}
