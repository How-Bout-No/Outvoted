package io.github.how_bout_no.outvoted.world.feature;

import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.ForkyTrunkPlacer;

import java.util.Random;

public class PalmTree extends Tree {

    /*
    public static final BaseTreeFeatureConfig PALM_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(ModBlocks.PALM_LOG.get().getDefaultState()),
            new SimpleBlockStateProvider(ModBlocks.PALM_LEAVES.get().getDefaultState()),
            new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(4), 4)))
            .baseHeight(150).heightRandA(5).foliageHeight(125).ignoreVines()
            .setSapling((IPlantable) ModBlocks.PALM_SAPLING.get()).build();

     */


    public static final BaseTreeFeatureConfig PALM_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(ModBlocks.PALM_LOG.get().getDefaultState()),
            new SimpleBlockStateProvider(ModBlocks.PALM_LEAVES.get().getDefaultState()),
            new AcaciaFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)),
            new ForkyTrunkPlacer(5, 2, 2),
            new TwoLayerFeature(1, 0, 2))).setIgnoreVines().build();



    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.withConfiguration(PALM_TREE_CONFIG);
    }

}
