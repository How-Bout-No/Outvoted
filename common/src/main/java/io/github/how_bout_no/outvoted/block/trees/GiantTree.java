package io.github.how_bout_no.outvoted.block.trees;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class GiantTree extends SaplingGenerator {
    public boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random rand) {
        for (int i = 0; i >= -3; --i) {
            for (int j = 0; j >= -3; --j) {
                if (canGiantTreeSpawnAt(state, world, pos, i, j)) {
                    return this.growGiantTree(world, chunkGenerator, pos, state, rand, i, j);
                }
            }
        }

        return super.generate(world, chunkGenerator, pos, state, rand);
    }

    /**
     * Get a {@link net.minecraft.world.gen.feature.ConfiguredFeature} of the huge variant of this tree
     */
    @Nullable
    protected abstract ConfiguredFeature<TreeFeatureConfig, ?> getGiantTreeFeature(Random rand);

    public boolean growGiantTree(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random rand, int branchX, int branchY) {
        ConfiguredFeature<TreeFeatureConfig, ?> configuredfeature = this.getGiantTreeFeature(rand);
        if (configuredfeature == null) {
            return false;
        } else {
            configuredfeature.config.ignoreFluidCheck();
            BlockState blockstate = Blocks.AIR.getDefaultState();
            for (int i = 0; i <= 3; ++i) {
                for (int j = 0; j <= 3; ++j) {
                    world.setBlockState(pos.add(branchX + i, 0, branchY + j), blockstate, 4);
                }
            }
            if (configuredfeature.generate(world, chunkGenerator, rand, pos.add(branchX, 0, branchY))) {
                return true;
            } else {
                for (int i = 0; i <= 3; ++i) {
                    for (int j = 0; j <= 3; ++j) {
                        world.setBlockState(pos.add(branchX + i, 0, branchY + j), state, 4);
                    }
                }
                return false;
            }
        }
    }

    public static boolean canGiantTreeSpawnAt(BlockState blockUnder, BlockView worldIn, BlockPos pos, int xOffset, int zOffset) {
        Block block = blockUnder.getBlock();
        for (int i = 0; i <= 3; ++i) {
            for (int j = 0; j <= 3; ++j) {
                if (block != worldIn.getBlockState(pos.add(xOffset + i, 0, zOffset + j)).getBlock()) {
                    return false;
                }
            }
        }
        return true;
    }
}
