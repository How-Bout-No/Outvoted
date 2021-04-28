package io.github.how_bout_no.outvoted.world.gen;

import io.github.how_bout_no.outvoted.config.EntityConfigBase;
import io.github.how_bout_no.outvoted.config.OutvotedConfigCommon;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import me.shedaniel.architectury.registry.BiomeModifications;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;

public class WorldGen {
    public static void addSpawnEntries() {
        BiomeModifications.postProcessProperties(biomeContext -> checkSpawning(biomeContext, new OutvotedConfigCommon.Entities.Wildfire()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.WILDFIRE.get(),
                                OutvotedConfigCommon.Entities.Wildfire.getRate(), 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> checkSpawning(biomeContext, new OutvotedConfigCommon.Entities.Hunger()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.HUNGER.get(),
                                OutvotedConfigCommon.Entities.Hunger.getRate(), 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> checkSpawning(biomeContext, new OutvotedConfigCommon.Entities.Kraken()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.WATER_CREATURE,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.KRAKEN.get(),
                                OutvotedConfigCommon.Entities.Kraken.getRate(), 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> checkSpawning(biomeContext, new OutvotedConfigCommon.Entities.Meerkat()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.CREATURE,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.MEERKAT.get(),
                                OutvotedConfigCommon.Entities.Meerkat.getRate(), 1, 3)));

        BiomeModifications.postProcessProperties(biomeContext -> OutvotedConfigCommon.Generation.isGenPalmTrees() && biomeContext.getKey().equals(BiomeKeys.DESERT_LAKES.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.PALM_TREE));
        BiomeModifications.postProcessProperties(biomeContext -> OutvotedConfigCommon.Generation.isGenBaobabTrees() && biomeContext.getKey().equals(BiomeKeys.SAVANNA.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.BAOBAB_TREE));
    }

    public static <E extends EntityConfigBase> boolean checkSpawning(BiomeModifications.BiomeContext biomeContext, E entity) {
        if (!entity.isSpawn()) return false;
        for (String biome : entity.getBiomes()) {
            if (!biomeContext.getKey().toString().equals(biome)) return false;
        }
        return true;
    }
}
