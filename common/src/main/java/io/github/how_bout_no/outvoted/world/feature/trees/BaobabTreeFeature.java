package io.github.how_bout_no.outvoted.world.feature.trees;

import com.mojang.serialization.Codec;
import io.github.how_bout_no.outvoted.block.ModSaplingBlock;
import io.github.how_bout_no.outvoted.config.OutvotedConfigCommon;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

import java.util.Random;

public class BaobabTreeFeature extends Feature<TreeFeatureConfig> {
    public BaobabTreeFeature(Codec<TreeFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(StructureWorldAccess worldIn, ChunkGenerator generator, Random random, BlockPos position, TreeFeatureConfig config) {
        int treeHeight = random.nextInt(8) + 8;

        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + treeHeight + 1 <= worldIn.getHeight()) {
            for (int j = position.getY(); j <= position.getY() + 1 + treeHeight; ++j) {
                int k = 1;
                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + treeHeight - 2) {
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
            } else if (checkValidGround(worldIn, position.down()) && position.getY() < worldIn.getHeight() - treeHeight - 1) {
                setDirtAt(worldIn, position.down());

                /*
                    This stores Y values and end height of the last branch location in each direction
                    This is a quick and dirty way to prevent branches from generating inside each other by allowing enough vertical spacing

                    First value is Y of branch, second is additional height at end of branch
                 */
                int[][] lastBranch = new int[][]{{0, 0}, {0, 0}, {0, 0}, {0, 0}};
                int minDistance = 3; // Minimum vertical distance between branches

                for (int i2 = 0; i2 < treeHeight; ++i2) {
                    BlockPos pos = position.up(i2);
                    for (int j = 0; j <= 3; ++j) {
                        for (int k = 0; k <= 3; ++k) {
                            if (pos.getY() == position.up(treeHeight - 1).getY()) {
                                this.placeWoodAt(worldIn, pos.add(j, 0, k), random, config);
                            } else {
                                this.placeLogAt(worldIn, pos.add(j, 0, k), random, config);
                            }
                            if (j % 3 == 0 || k % 3 == 0) {
                                Random random1 = new Random();
                                boolean condition;
                                if (OutvotedConfigCommon.Generation.isBaobabType()) {
                                    condition = i2 >= treeHeight / (1.5 + ((treeHeight - 7) / 2.0)) && random1.nextFloat() < 0.05F * (treeHeight - 7);
                                } else {
                                    condition = false;
                                }
                                if (condition || pos.getY() == position.up(treeHeight - 1).getY()) {
                                    int rand = random1.nextInt(3) + 4; // Length of branch
                                    int randh = random1.nextInt(2); // Height variation at end of branch
                                    for (int l = 1; l <= rand; ++l) { // Loop for full length of branch
                                        if (j == 0 && pos.getY() > lastBranch[0][0] + minDistance + Math.max(lastBranch[0][1], 1)) {
                                            if (l == rand) {
                                                lastBranch[0][0] = pos.getY();
                                                lastBranch[0][1] = randh;
                                                this.placeWoodAt(worldIn, pos.add(j, 0, k).west(l), random, config, Direction.Axis.X);
                                                for (int m = 1; m <= randh; ++m) {
                                                    this.placeLogAt(worldIn, pos.add(j, 0, k).west(l).up(m), random, config);
                                                }
                                                this.placeLeaves(worldIn, random, pos.add(j, 0, k).west(l).up(randh), config, randh);
                                            } else {
                                                this.placeLogAt(worldIn, pos.add(j, 0, k).west(l), random, config, Direction.Axis.X);
                                            }
                                        } else if (j == 3 && pos.getY() > lastBranch[1][0] + minDistance + Math.max(lastBranch[1][1], 1)) {
                                            if (l == rand) {
                                                lastBranch[1][0] = pos.getY();
                                                lastBranch[1][1] = randh;
                                                this.placeWoodAt(worldIn, pos.add(j, 0, k).east(l), random, config, Direction.Axis.X);
                                                for (int m = 1; m <= randh; ++m) {
                                                    this.placeLogAt(worldIn, pos.add(j, 0, k).east(l).up(m), random, config);
                                                }
                                                this.placeLeaves(worldIn, random, pos.add(j, 0, k).east(l).up(randh), config, randh);
                                            } else {
                                                this.placeLogAt(worldIn, pos.add(j, 0, k).east(l), random, config, Direction.Axis.X);
                                            }
                                        } else if (k == 0 && pos.getY() > lastBranch[2][0] + minDistance + Math.max(lastBranch[2][1], 1)) {
                                            if (l == rand) {
                                                lastBranch[2][0] = pos.getY();
                                                lastBranch[2][1] = randh;
                                                this.placeWoodAt(worldIn, pos.add(j, 0, k).north(l), random, config, Direction.Axis.Z);
                                                for (int m = 1; m <= randh; ++m) {
                                                    this.placeLogAt(worldIn, pos.add(j, 0, k).north(l).up(m), random, config);
                                                }
                                                this.placeLeaves(worldIn, random, pos.add(j, 0, k).north(l).up(randh), config, randh);
                                            } else {
                                                this.placeLogAt(worldIn, pos.add(j, 0, k).north(l), random, config, Direction.Axis.Z);
                                            }
                                        } else if (pos.getY() > lastBranch[3][0] + minDistance + Math.max(lastBranch[3][1], 1)) {
                                            if (l == rand) {
                                                lastBranch[3][0] = pos.getY();
                                                lastBranch[3][1] = randh;
                                                this.placeWoodAt(worldIn, pos.add(j, 0, k).south(l), random, config, Direction.Axis.Z);
                                                for (int m = 1; m <= randh; ++m) {
                                                    this.placeLogAt(worldIn, pos.add(j, 0, k).south(l).up(m), random, config);
                                                }
                                                this.placeLeaves(worldIn, random, pos.add(j, 0, k).south(l).up(randh), config, randh);
                                            } else {
                                                this.placeLogAt(worldIn, pos.add(j, 0, k).south(l), random, config, Direction.Axis.Z);
                                            }
                                        }
                                    }
                                } else if (random1.nextFloat() > 0.8F && pos.getY() == position.getY()) {
                                    if (j == 0 && isValidGround(worldIn, pos.add(j, 0, k).west(1).down())) {
                                        this.placeWoodAt(worldIn, pos.add(j, 0, k).west(1), random, config, Direction.Axis.X);
                                    } else if (j == 3 && isValidGround(worldIn, pos.add(j, 0, k).east(1).down())) {
                                        this.placeWoodAt(worldIn, pos.add(j, 0, k).east(1), random, config, Direction.Axis.X);
                                    } else if (k == 0 && isValidGround(worldIn, pos.add(j, 0, k).north(1).down())) {
                                        this.placeWoodAt(worldIn, pos.add(j, 0, k).north(1), random, config, Direction.Axis.Z);
                                    } else if (isValidGround(worldIn, pos.add(j, 0, k).south(1).down())) {
                                        this.placeWoodAt(worldIn, pos.add(j, 0, k).south(1), random, config, Direction.Axis.Z);
                                    }
                                }
                            }
                        }
                    }
                }

                return true;
            } else return false;
        } else return false;
    }

    private void placeLeaves(StructureWorldAccess worldIn, Random random, BlockPos branchPos, TreeFeatureConfig config, int randh) {
        Random random1 = new Random();
        int smolleaf = random1.nextInt(3);
        for (BlockPos blockpos1 : BlockPos.iterate(branchPos.add(-3, 1, -3), branchPos.add(3, Math.max(randh, 1) + 1, 3))) {
            double d0 = blockpos1.getSquaredDistance(branchPos.getX(), branchPos.getY(), branchPos.getZ(), false);
            if (isAirOrLeaves(worldIn, blockpos1)) {
                switch (smolleaf) {
                    case 0:
                        if ((blockpos1.getY() == branchPos.getY() + 1 && d0 < 8)) {
                            this.placeLeafAt(worldIn, blockpos1, random, config);
                        }
                        break;
                    case 1:
                        if ((blockpos1.getY() == branchPos.getY() + 1 && d0 < 12) || d0 < 4) {
                            this.placeLeafAt(worldIn, blockpos1, random, config);
                        }
                        break;
                    case 2:
                        if ((blockpos1.getY() == branchPos.getY() + 1 && d0 < 18) || d0 < 8) {
                            this.placeLeafAt(worldIn, blockpos1, random, config);
                        }
                        break;
                }
            }
        }
    }

    private void placeLogAt(ModifiableWorld worldIn, BlockPos pos, Random rand, TreeFeatureConfig config) {
        this.setLogState(worldIn, pos, config.trunkProvider.getBlockState(rand, pos));
    }

    private void placeLogAt(ModifiableWorld worldIn, BlockPos pos, Random rand, TreeFeatureConfig config, Direction.Axis axis) {
        this.setLogState(worldIn, pos, config.trunkProvider.getBlockState(rand, pos).with(PillarBlock.AXIS, axis));
    }

    private void placeWoodAt(ModifiableWorld worldIn, BlockPos pos, Random rand, TreeFeatureConfig config) {
        this.setLogState(worldIn, pos, new SimpleBlockStateProvider(ModBlocks.BAOBAB_WOOD.get().getDefaultState()).getBlockState(rand, pos));
    }

    private void placeWoodAt(ModifiableWorld worldIn, BlockPos pos, Random rand, TreeFeatureConfig config, Direction.Axis axis) {
        this.setLogState(worldIn, pos, new SimpleBlockStateProvider(ModBlocks.BAOBAB_WOOD.get().getDefaultState()).getBlockState(rand, pos).with(PillarBlock.AXIS, axis));
    }

    private void placeLeafAt(ModifiableTestableWorld world, BlockPos pos, Random rand, TreeFeatureConfig config) {
        if (isAirOrLeaves(world, pos)) {
            this.setLogState(world, pos, config.leavesProvider.getBlockState(rand, pos).with(LeavesBlock.DISTANCE, 1));
        }
    }

    protected final void setLogState(ModifiableWorld worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state, 19);
    }

    public static boolean isAirOrLeaves(TestableWorld worldIn, BlockPos pos) {
        if (worldIn instanceof net.minecraft.world.WorldView) {
            return worldIn.testBlockState(pos, state ->
                    state.isAir() || state.isIn(BlockTags.LEAVES));
        }
        return worldIn.testBlockState(pos, (state) -> {
            return state.isAir() || state.isIn(BlockTags.LEAVES);
        });
    }

    public static void setDirtAt(WorldAccess worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.GRASS_BLOCK || block == Blocks.FARMLAND) {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 18);
        }
    }

    public static boolean isValidGround(WorldAccess world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos);
        ModSaplingBlock sapling = (ModSaplingBlock) ModBlocks.BAOBAB_SAPLING.get();
        return floor.isIn(sapling.blockTag) || floor.isOf(Blocks.GRASS_BLOCK) || floor.isOf(Blocks.DIRT) || floor.isOf(Blocks.COARSE_DIRT) || floor.isOf(Blocks.PODZOL) || floor.isOf(Blocks.FARMLAND);
    }

    public static boolean checkValidGround(WorldAccess world, BlockPos pos) {
        for (int i = 0; i <= 3; ++i) {
            for (int j = 0; j <= 3; ++j) {
                if (!isValidGround(world, pos.add(i, 0, j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
