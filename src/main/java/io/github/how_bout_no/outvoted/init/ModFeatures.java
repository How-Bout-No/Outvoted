package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Outvoted.MOD_ID);
    public static final DeferredRegister<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURE_FEATURES = DeferredRegister.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, Outvoted.MOD_ID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Outvoted.MOD_ID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_BURROW =
            CONFIGURED_FEATURES.register("burrow",
                    () -> new ConfiguredFeature<>(Feature.BLOCK_COLUMN,
                            new BlockColumnConfiguration(List.of(BlockColumnConfiguration.layer(ConstantInt.of(1), BlockStateProvider.simple(ModBlocks.BURROW.get()))), Direction.DOWN, BlockPredicate.matchesBlock(Blocks.AIR, BlockPos.ZERO), false)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_BAOBAB =
            CONFIGURED_FEATURES.register("baobab",
                    () -> new ConfiguredFeature<>(Feature.TREE,
                            (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ModBlocks.BAOBAB_LOG.get()),
                                    new StraightTrunkPlacer(4, 2, 0),
                                    BlockStateProvider.simple(ModBlocks.BAOBAB_LEAVES.get()),
                                    new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 70),
                                    new TwoLayersFeatureSize(0, 0, 0))).build()));
//                                    Optional.of(new MangroveRootPlacer(UniformInt.of(1, 3), BlockStateProvider.simple(Blocks.MANGROVE_ROOTS), Optional.of(new AboveRootPlacement(BlockStateProvider.simple(Blocks.MOSS_CARPET), 0.5F)), new MangroveRootPlacement(Registry.BLOCK.getOrCreateTag(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH), HolderSet.direct(Block::builtInRegistryHolder, new Block[]{Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS}), BlockStateProvider.simple(Blocks.MUDDY_MANGROVE_ROOTS), 8, 15, 0.2F))), new TwoLayersFeatureSize(2, 0, 2))).decorators(List.of(new LeaveVineDecorator(0.125F), new AttachedToLeavesDecorator(0.14F, 1, 0, new RandomizedIntStateProvider(BlockStateProvider.simple((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState().setValue(MangrovePropaguleBlock.HANGING, true)), MangrovePropaguleBlock.AGE, UniformInt.of(0, 4)), 2, List.of(Direction.DOWN)), BEEHIVE_001)).ignoreVines().build());

    public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_PALM =
            CONFIGURED_FEATURES.register("palm",
                    () -> new ConfiguredFeature<>(Feature.TREE,
                            (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ModBlocks.PALM_LOG.get()),
                                    new ForkingTrunkPlacer(5, 2, 2), BlockStateProvider.simple(ModBlocks.PALM_LEAVES.get()),
                                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                                    new TwoLayersFeatureSize(1, 0, 2))).ignoreVines().build()));


    // A PlacedFeature is a configured feature and a list of placement modifiers.
    // This is what goes into biomes, the placement modifiers determine where and how often to
    // place the configured feature.
    // Each placed feature in any of the biomes in a chunk generates in that chunk when the chunk generates.
    // When a feature generates in a chunk, the chunk's local 0,0,0 origin coordinate is given to the
    // list of placement modifiers (if any).
    // Each placement modifier converts the input position to zero or more output positions, each of which
    // is given to the next placement modifier.
    // The ConfiguredFeature is generated at the positions generated after all placement modifiers have run.
    public static final RegistryObject<PlacedFeature> PLACED_BURROW =
            PLACED_FEATURES.register("burrow",
                    // InSquarePlacement.spread() takes the input position
                    // and randomizes the X and Z coordinates within the chunk
                    // PlacementUtils.HEIGHTMAP sets the Y-coordinate of the input position to the heightmap.
                    // This causes the tnt pile to be generated at a random surface position in the chunk.
                    () -> new PlacedFeature(CONFIGURED_BURROW.getHolder().get(),
                            List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
    public static final RegistryObject<PlacedFeature> PLACED_BAOBAB =
            PLACED_FEATURES.register("baobab",
                    () -> new PlacedFeature(CONFIGURED_BAOBAB.getHolder().get(),
                            List.of(PlacementUtils.filteredByBlockSurvival(ModBlocks.BAOBAB_SAPLING.get()))));
    public static final RegistryObject<PlacedFeature> PLACED_PALM =
            PLACED_FEATURES.register("palm",
                    () -> new PlacedFeature(CONFIGURED_PALM.getHolder().get(),
                            List.of(PlacementUtils.filteredByBlockSurvival(ModBlocks.PALM_SAPLING.get()))));
}
