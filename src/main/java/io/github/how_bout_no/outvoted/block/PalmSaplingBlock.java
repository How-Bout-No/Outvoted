package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class PalmSaplingBlock extends ModSaplingBlock{
    public PalmSaplingBlock(Tree treeIn, Properties properties) {
        super(treeIn, properties);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.isIn(Blocks.GRASS_BLOCK) || state.isIn(Blocks.DIRT) || state.isIn(Blocks.COARSE_DIRT) || state.isIn(Blocks.PODZOL) || state.isIn(Blocks.FARMLAND) || state.isIn(Blocks.SAND) || state.isIn(Blocks.RED_SAND);
    }

}
