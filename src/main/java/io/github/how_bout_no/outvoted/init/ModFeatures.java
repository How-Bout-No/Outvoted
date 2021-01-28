package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.world.feature.tree.PalmTreeFeature;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Outvoted.MOD_ID);

    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> PALM_TREE = FEATURES.register("palm_tree", () -> new PalmTreeFeature(BaseTreeFeatureConfig.CODEC));

    public static final class States {
        private static final BlockState PALM_LOG = ModBlocks.PALM_LOG.get().getDefaultState();
        private static final BlockState PALM_LEAVES = ModBlocks.PALM_LEAVES.get().getDefaultState();
    }

    public static final class Configs {
        public static final BaseTreeFeatureConfig PALM_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PALM_LOG), new SimpleBlockStateProvider(States.PALM_LEAVES), new BlobFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0), 0), new StraightTrunkPlacer(0, 0, 0), new TwoLayerFeature(0, 0, 0))).setIgnoreVines().build();
    }

    public static final class Configured {
//        public static final ConfiguredFeature<?, ?> PALM_TREE = ModFeatures.PALM_TREE.get().withConfiguration(Configs.PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.1F, 1)));
        public static final ConfiguredFeature<?, ?> PALM_TREE = ModFeatures.PALM_TREE.get().withConfiguration(Configs.PALM_TREE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.CHANCE.configure(new ChanceConfig(2)));

        private static <FC extends IFeatureConfig> void register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Outvoted.MOD_ID, name), configuredFeature);
        }

        public static void registerConfiguredFeatures() {
            register("palm_tree", PALM_TREE);
        }
    }
}
