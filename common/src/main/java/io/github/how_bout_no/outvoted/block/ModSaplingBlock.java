package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Arrays;
import java.util.HashSet;

public class ModSaplingBlock extends SaplingBlock {
    public Tag<Block> blockTag;

    public ModSaplingBlock(SaplingGenerator treeIn, Settings properties) {
        super(treeIn, properties);
        blockTag = Tag.of(new HashSet<>(Arrays.asList(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND)));
    }

    public ModSaplingBlock(SaplingGenerator treeIn, Settings properties, Tag<Block> blockTagIn) {
        super(treeIn, properties);
        blockTag = blockTagIn;
    }

    @Override
    public boolean canPlantOnTop(BlockState state, BlockView worldIn, BlockPos pos) {
        return blockTag == null ? super.canPlantOnTop(state, worldIn, pos) : super.canPlantOnTop(state, worldIn, pos) || state.isIn(blockTag);
    }
}
