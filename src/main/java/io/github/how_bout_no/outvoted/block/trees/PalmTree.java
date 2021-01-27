package io.github.how_bout_no.outvoted.block.trees;

import io.github.how_bout_no.outvoted.init.ModFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class PalmTree extends Tree {
    @Nullable
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return ModFeatures.PALM_TREE.get().withConfiguration(ModFeatures.Configs.PALM_TREE_CONFIG);
    }
}
