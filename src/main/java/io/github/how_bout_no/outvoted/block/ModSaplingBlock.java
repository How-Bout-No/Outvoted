package io.github.how_bout_no.outvoted.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ModSaplingBlock extends SaplingBlock {
    public List<Block> blocks;

    public ModSaplingBlock(AbstractTreeGrower treeIn, Properties properties, Block... placeableOn) {
        super(treeIn, properties);
        blocks = List.of(placeableOn);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return blocks.isEmpty() ? super.mayPlaceOn(state, blockGetter, pos) : blocks.contains(state.getBlock());
    }
}
