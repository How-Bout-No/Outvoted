package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ModSaplingBlock extends SaplingBlock {
    public Tag.Identified<Block> blockTag = null;

    public ModSaplingBlock(SaplingGenerator treeIn, Settings properties) {
        super(treeIn, properties);
    }

    public ModSaplingBlock(SaplingGenerator treeIn, Settings properties, Tag.Identified<Block> blockTagIn) {
        super(treeIn, properties);
        blockTag = blockTagIn;
    }

    @Override
    public boolean canPlantOnTop(BlockState state, BlockView worldIn, BlockPos pos) {
        return blockTag == null ? super.canPlantOnTop(state, worldIn, pos) : super.canPlantOnTop(state, worldIn, pos) || state.isIn(blockTag);
    }
}
