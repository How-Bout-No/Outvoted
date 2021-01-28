package io.github.how_bout_no.outvoted.block;


import io.github.how_bout_no.outvoted.block.trees.PalmTree;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.Tags;

public class PalmTreeSaplingBlock extends SaplingBlock {
    public PalmTreeSaplingBlock(Properties properties) {
        super(new PalmTree(), properties);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return super.isValidGround(state, worldIn, pos) || state.isIn(Tags.Blocks.SAND);
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.DESERT;
    }
}
