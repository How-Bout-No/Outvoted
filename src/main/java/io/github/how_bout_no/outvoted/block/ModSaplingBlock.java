package io.github.how_bout_no.outvoted.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ModSaplingBlock extends SaplingBlock {
    public Tag<Block> blockTag;

    public ModSaplingBlock(AbstractTreeGrower treeIn, Properties properties) {
        super(treeIn, properties);
        blockTag = new Tag<>(List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND));
    }

    public ModSaplingBlock(AbstractTreeGrower treeIn, Properties properties, Tag<Block> blockTagIn) {
        super(treeIn, properties);
        blockTag = blockTagIn;
    }

    @Override
    public boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return blockTag == null ? super.mayPlaceOn(state, worldIn, pos) : super.mayPlaceOn(state, worldIn, pos) || blockTag.getValues().contains(state.getBlock());
    }
}
