package io.github.how_bout_no.outvoted.world.gen;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

@SuppressWarnings("deprecation")
public class WorldGen {
    public static void addSpawnEntries() {
        if (Outvoted.config.get().entities.wildfire.spawn) {
            BiomeModifications.addSpawn(selector -> Outvoted.config.get().entities.wildfire.biomes.contains(selector.getBiome().toString()),
                    SpawnGroup.MONSTER, ModEntityTypes.WILDFIRE.get(),
                    Outvoted.config.get().entities.wildfire.rate, 1, 1);
        }
        if (Outvoted.config.get().entities.hunger.spawn) {
            BiomeModifications.addSpawn(selector -> Outvoted.config.get().entities.hunger.biomes.contains(selector.getBiome().toString()),
                    SpawnGroup.MONSTER, ModEntityTypes.HUNGER.get(),
                    Outvoted.config.get().entities.hunger.rate, 1, 1);
        }
        if (Outvoted.config.get().entities.kraken.spawn) {
            BiomeModifications.addSpawn(selector -> Outvoted.config.get().entities.kraken.biomes.contains(selector.getBiome().toString()),
                    SpawnGroup.WATER_CREATURE, ModEntityTypes.KRAKEN.get(),
                    Outvoted.config.get().entities.kraken.rate, 1, 1);
        }

        if (Outvoted.config.get().generation.genpalmtrees) {
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.DESERT_LAKES),
                    GenerationStep.Feature.VEGETAL_DECORATION,
                    ModFeatures.Configured.palmTree);
        }
        if (Outvoted.config.get().generation.genbaobabtrees) {
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SAVANNA),
                    GenerationStep.Feature.VEGETAL_DECORATION,
                    ModFeatures.Configured.baobabTree);
        }
    }
}
