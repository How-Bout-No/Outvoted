package io.github.how_bout_no.outvoted.block.trees;

import io.github.how_bout_no.outvoted.init.ModFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PalmTree extends SaplingGenerator {
    @Nullable
    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random randomIn, boolean largeHive) {
        return ModFeatures.PALM_TREE.get().configure(ModFeatures.Configs.PALM_TREE_CONFIG);
    }
}
