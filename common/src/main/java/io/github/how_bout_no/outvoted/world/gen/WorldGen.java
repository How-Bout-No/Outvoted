package io.github.how_bout_no.outvoted.world.gen;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.config.EntityConfigBase;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import me.shedaniel.architectury.registry.BiomeModifications;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;

public class WorldGen {
    public static void addSpawnEntries() {
        BiomeModifications.postProcessProperties(biomeContext -> checkSpawning(biomeContext, Outvoted.config.get().entities.wildfire),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.WILDFIRE.get(),
                                Outvoted.config.get().entities.wildfire.rate, 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> checkSpawning(biomeContext, Outvoted.config.get().entities.hunger),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.HUNGER.get(),
                                Outvoted.config.get().entities.hunger.rate, 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> checkSpawning(biomeContext, Outvoted.config.get().entities.kraken),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.WATER_CREATURE,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.KRAKEN.get(),
                                Outvoted.config.get().entities.kraken.rate, 1, 1)));

        BiomeModifications.postProcessProperties(biomeContext -> Outvoted.config.get().generation.genpalmtrees && biomeContext.getKey().equals(BiomeKeys.DESERT_LAKES.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.PALM_TREE));
        BiomeModifications.postProcessProperties(biomeContext -> Outvoted.config.get().generation.genbaobabtrees && biomeContext.getKey().equals(BiomeKeys.SAVANNA.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.BAOBAB_TREE));
    }

    public static <E extends EntityConfigBase> boolean checkSpawning(BiomeModifications.BiomeContext biomeContext, E entity) {
        if (!entity.spawn) return false;
        for (String biome : entity.biomes) {
            if (!biomeContext.getKey().toString().equals(biome)) return false;
        }
        return true;
    }
}
