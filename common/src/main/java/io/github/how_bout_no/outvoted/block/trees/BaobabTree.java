package io.github.how_bout_no.outvoted.block.trees;

import io.github.how_bout_no.outvoted.init.ModFeatures;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BaobabTree extends GiantTree {
    @Nullable
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return null;
    }

    @Nullable
    protected ConfiguredFeature<TreeFeatureConfig, ?> getGiantTreeFeature(Random rand) {
        return ModFeatures.BAOBAB_TREE.get().configure(ModFeatures.Configs.BAOBAB_TREE_CONFIG);
    }
}
