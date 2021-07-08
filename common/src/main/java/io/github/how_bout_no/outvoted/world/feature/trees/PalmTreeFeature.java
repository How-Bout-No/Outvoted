package io.github.how_bout_no.outvoted.world.feature.trees;

import com.mojang.serialization.Codec;
import io.github.how_bout_no.outvoted.block.ModSaplingBlock;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

import java.util.Random;

public class PalmTreeFeature extends Feature<TreeFeatureConfig> {
    public PalmTreeFeature(Codec<TreeFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(FeatureContext<TreeFeatureConfig> context) {
        StructureWorldAccess worldIn = context.getWorld();
        Random random = context.getRandom();
        BlockPos position = context.getOrigin();
        TreeFeatureConfig config = context.getConfig();

        int i = random.nextInt(2) + 6; // Tree height

        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getHeight()) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 1;
                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2) {
                    k = 2;
                }

                BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < worldIn.getHeight()) {
                            if (!isAirOrLeaves(worldIn, blockpos$mutableblockpos.set(l, j, i1))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else if (isValidGround(worldIn, position.down()) && position.getY() < worldIn.getHeight() - i - 1) {
                setDirtAt(worldIn, position.down());

                BlockPos blockpos = position.up(i - 1);
                boolean flag1 = random.nextBoolean();
                boolean flag2 = true;
                boolean has2bends = random.nextBoolean();
                int bends = 0;
                int rand = random.nextBoolean() ? -1 : 1;

                for (int i2 = 0; i2 < i; ++i2) { // do tree bends
                    if (bends < 2 && flag2 && (i2 >= i / 3 && random.nextBoolean() && i2 < i * 2 / 3) || (has2bends && i2 == i * 2 / 3)) {
                        if (has2bends || i2 >= i / 2) {
                            if (flag1) {
                                this.placeLogAt(worldIn, position.up(i2).add(rand, 0, 0), random, config);
                                blockpos = blockpos.add(rand, 0, 0);
                                position = position.add(rand, 0, 0);
                            } else {
                                this.placeLogAt(worldIn, position.up(i2).add(0, 0, rand), random, config);
                                blockpos = blockpos.add(0, 0, rand);
                                position = position.add(0, 0, rand);
                            }
                            bends++;
                            flag2 = false;
                        }
                    } else {
                        this.placeLogAt(worldIn, position.up(i2), random, config);
                        flag2 = has2bends;
                    }
                }

                for (BlockPos blockpos1 : BlockPos.iterate(blockpos.add(-(i - 2), -1, -(i - 2)), blockpos.add(i - 2, 2, i - 2))) {
                    double d0 = blockpos1.getSquaredDistance(blockpos.getX(), blockpos.getY(), blockpos.getZ(), false);
                    int y1 = blockpos1.getY();
                    int y2 = blockpos.getY();
                    /*
                     Wow this is ugly
                     I'm so sorry
                     */
                    if ((d0 <= 1 && (y1 > y2 || isStraight(blockpos1, blockpos))) ||
                            (d0 <= 4 && (y1 == y2 + 1 || y1 == y2 + 2)) ||
                            (d0 <= 7 && y1 == y2 + 2 && isStraight(blockpos1, blockpos)) ||
                            (d0 <= i * 1.75 && isStraight(blockpos1, blockpos) && y1 == y2 + 1) ||
                            (d0 > i * 1.75 && d0 < (i - 2) * (i - 2) && isStraight(blockpos1, blockpos) && y1 == y2) ||
                            (d0 >= (i - 2) * (i - 2) && isStraight(blockpos1, blockpos) && y1 == y2 - (i - 6)) ||
                            (d0 > i && d0 <= (i - 3) * (i - 3) && isDiag(blockpos1, blockpos) && y1 == y2 + 1) ||
                            (d0 > (i - 3) * (i - 3) && d0 <= (i - 2) * (i - 2) + 2 && isDiag(blockpos1, blockpos) && y1 == y2) ||
                            (d0 > (i - 3) * (i - 3) && d0 == (int) ((i - 2) * (i - 2) * Math.sqrt(1.75)) && isDiag(blockpos1, blockpos) && y1 < y2)) {
                        if (isAirOrLeaves(worldIn, blockpos1)) {
                            this.placeLeafAt(worldIn, blockpos1, random, config);
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isStraight(BlockPos blockPos1, BlockPos blockPos2) {
        return blockPos1.getX() == blockPos2.getX() || blockPos1.getZ() == blockPos2.getZ();
    }

    private boolean isDiag(BlockPos blockPos1, BlockPos blockPos2) {
        int diffX = Math.abs(blockPos1.getX() - blockPos2.getX());
        int diffZ = Math.abs(blockPos1.getZ() - blockPos2.getZ());
        return diffX == diffZ;
    }

    private void placeLogAt(ModifiableWorld worldIn, BlockPos pos, Random rand, TreeFeatureConfig config) {
        this.setLogState(worldIn, pos, config.trunkProvider.getBlockState(rand, pos));
    }

    private void placeWoodAt(ModifiableWorld worldIn, BlockPos pos, Random rand, TreeFeatureConfig config) {
        this.setLogState(worldIn, pos, new SimpleBlockStateProvider(ModBlocks.PALM_WOOD.get().getDefaultState()).getBlockState(rand, pos));
    }

    private void placeLeafAt(ModifiableTestableWorld world, BlockPos pos, Random rand, TreeFeatureConfig config) {
        if (isAirOrLeaves(world, pos)) {
            this.setLogState(world, pos, config.foliageProvider.getBlockState(rand, pos).with(LeavesBlock.DISTANCE, 1));
        }
    }

    protected final void setLogState(ModifiableWorld worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state, 19);
    }

    public static boolean isAirOrLeaves(TestableWorld worldIn, BlockPos pos) {
        if (worldIn instanceof WorldView) {
            return worldIn.testBlockState(pos, state ->
                    state.isAir() || state.isIn(BlockTags.LEAVES));
        }
        return worldIn.testBlockState(pos, (state) -> state.isAir() || state.isIn(BlockTags.LEAVES));
    }

    public static void setDirtAt(WorldAccess worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.GRASS_BLOCK || block == Blocks.FARMLAND) {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 18);
        }
    }

    public static boolean isValidGround(WorldAccess world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos);
        ModSaplingBlock sapling = (ModSaplingBlock) ModBlocks.PALM_SAPLING.get();
        return floor.isIn(sapling.blockTag) || floor.isOf(Blocks.GRASS_BLOCK) || floor.isOf(Blocks.DIRT) || floor.isOf(Blocks.COARSE_DIRT) || floor.isOf(Blocks.PODZOL) || floor.isOf(Blocks.FARMLAND);
    }
}
