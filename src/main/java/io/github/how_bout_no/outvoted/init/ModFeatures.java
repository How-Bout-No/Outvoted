package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Outvoted.MOD_ID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Outvoted.MOD_ID);

    private static final String TNT_PILE_NAME = "tnt_pile";

    // your ConfiguredFeature RegistryObject fields must use <?,?> as the generic params for technical reasons
    public static final RegistryObject<ConfiguredFeature<?,?>> CONFIGURED_TNT_PILE =
            CONFIGURED_FEATURES.register(TNT_PILE_NAME,
                    // ConfiguredFeature takes a feature type and a featureconfig.
                    // You generally can't static init featureconfigs ahead of time, as they
                    // very often have hard references to blocks (such as this one does).
                    // The feature type defines the generation logic, the feature config is extra data used by that logic.
                    // Feature.BLOCK_PILE generates blocks in a pile, it takes a BlockPileConfiguration.
                    () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                            // BlockPileConfiguration takes a blockstate provider, we use one that always provides TNT.
                            new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.TNT))));

    // A PlacedFeature is a configured feature and a list of placement modifiers.
    // This is what goes into biomes, the placement modifiers determine where and how often to
    // place the configured feature.
    // Each placed feature in any of the biomes in a chunk generates in that chunk when the chunk generates.
    // When a feature generates in a chunk, the chunk's local 0,0,0 origin coordinate is given to the
    // list of placement modifiers (if any).
    // Each placement modifier converts the input position to zero or more output positions, each of which
    // is given to the next placement modifier.
    // The ConfiguredFeature is generated at the positions generated after all placement modifiers have run.
    public static final RegistryObject<PlacedFeature> PLACED_TNT_PILE =
            PLACED_FEATURES.register(TNT_PILE_NAME,
                    () -> new PlacedFeature(CONFIGURED_TNT_PILE.getHolder().get(),
                            // InSquarePlacement.spread() takes the input position
                            // and randomizes the X and Z coordinates within the chunk
                            // PlacementUtils.HEIGHTMAP sets the Y-coordinate of the input position to the heightmap.
                            // This causes the tnt pile to be generated at a random surface position in the chunk.
                            List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP)));
}
