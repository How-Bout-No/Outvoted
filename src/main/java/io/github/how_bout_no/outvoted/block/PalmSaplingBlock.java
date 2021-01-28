package io.github.how_bout_no.outvoted.block;


import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.Tags;

public class PalmSaplingBlock extends SaplingBlock {
    public PalmSaplingBlock(Tree treeIn, Properties properties) {
        super(treeIn, properties);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.isIn(Tags.Blocks.SAND);
    }
}
