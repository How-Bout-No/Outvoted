package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.world.feature.BurrowGenFeature;
import io.github.how_bout_no.outvoted.world.feature.trees.BaobabTreeFeature;
import io.github.how_bout_no.outvoted.world.feature.trees.PalmTreeFeature;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Outvoted.MOD_ID, Registry.FEATURE_KEY);

    public static final RegistrySupplier<Feature<DefaultFeatureConfig>> BURROW = FEATURES.register("burrow", () -> new BurrowGenFeature(DefaultFeatureConfig.CODEC));
    public static final RegistrySupplier<Feature<TreeFeatureConfig>> PALM_TREE = FEATURES.register("palm_tree", () -> new PalmTreeFeature(TreeFeatureConfig.CODEC));
    public static final RegistrySupplier<Feature<TreeFeatureConfig>> BAOBAB_TREE = FEATURES.register("baobab_tree", () -> new BaobabTreeFeature(TreeFeatureConfig.CODEC));

    public static final class States {
        private static final BlockState PALM_LOG = ModBlocks.PALM_LOG.get().getDefaultState();
        private static final BlockState PALM_LEAVES = ModBlocks.PALM_LEAVES.get().getDefaultState();

        private static final BlockState BAOBAB_LOG = ModBlocks.BAOBAB_LOG.get().getDefaultState();
        private static final BlockState BAOBAB_LEAVES = ModBlocks.BAOBAB_LEAVES.get().getDefaultState();
    }

    public static final class Configs {
        public static final TreeFeatureConfig PALM_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PALM_LOG), new SimpleBlockStateProvider(States.PALM_LEAVES), new BlobFoliagePlacer(UniformIntDistribution.of(0), UniformIntDistribution.of(0), 0), new StraightTrunkPlacer(0, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).ignoreVines().build();
        public static final TreeFeatureConfig BAOBAB_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.BAOBAB_LOG), new SimpleBlockStateProvider(States.BAOBAB_LEAVES), new BlobFoliagePlacer(UniformIntDistribution.of(0), UniformIntDistribution.of(0), 0), new StraightTrunkPlacer(0, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).ignoreVines().build();
    }

    public static final class Configured {
        public static final ConfiguredFeature<?, ?> BURROW = ModFeatures.BURROW.get().configure(FeatureConfig.DEFAULT).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT.configure(new CountConfig(1)).applyChance(50));
        public static final ConfiguredFeature<?, ?> PALM_TREE = ModFeatures.PALM_TREE.get().configure(Configs.PALM_TREE_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT.configure(new CountConfig(1)));
        //        public static final ConfiguredFeature<?, ?> PALM_TREE = ModFeatures.PALM_TREE.get().withConfiguration(Configs.PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.CHANCE.configure(new ChanceConfig(2)));
        public static final ConfiguredFeature<?, ?> BAOBAB_TREE = ModFeatures.BAOBAB_TREE.get().configure(Configs.BAOBAB_TREE_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT.configure(new CountConfig(1)).applyChance(10));

        private static <FC extends net.minecraft.world.gen.feature.FeatureConfig> void register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Outvoted.MOD_ID, name), configuredFeature);
        }

        public static void registerConfiguredFeatures() {
            register("palm_tree", PALM_TREE);
            register("baobab_tree", BAOBAB_TREE);
            register("burrow", BURROW);
        }
    }
}
