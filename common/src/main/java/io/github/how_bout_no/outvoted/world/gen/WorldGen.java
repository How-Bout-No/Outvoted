package io.github.how_bout_no.outvoted.world.gen;

import io.github.how_bout_no.outvoted.Outvoted;
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
        OutvotedConfigCommon.Entities ent = Outvoted.commonConfig.entities;
        BiomeModifications.postProcessProperties(biomeContext -> ent.wildfire.spawn && ent.wildfire.biomes.contains(biomeContext.getKey().toString()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.WILDFIRE.get(),
                                Outvoted.commonConfig.entities.wildfire.rate, 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> ent.glutton.spawn && ent.glutton.biomes.contains(biomeContext.getKey().toString()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.GLUTTON.get(),
                                Outvoted.commonConfig.entities.glutton.rate, 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> ent.barnacle.spawn && ent.barnacle.biomes.contains(biomeContext.getKey().toString()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.WATER_CREATURE,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.BARNACLE.get(),
                                Outvoted.commonConfig.entities.barnacle.rate, 1, 1)));
        BiomeModifications.postProcessProperties(biomeContext -> ent.meerkat.spawn && ent.meerkat.biomes.contains(biomeContext.getKey().toString()),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.CREATURE,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.MEERKAT.get(),
                                Outvoted.commonConfig.entities.meerkat.rate, 1, 3)));

        BiomeModifications.postProcessProperties(biomeContext -> Outvoted.commonConfig.generation.genPalmTrees && biomeContext.getKey().equals(BiomeKeys.DESERT_LAKES.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.PALM_TREE));
        BiomeModifications.postProcessProperties(biomeContext -> Outvoted.commonConfig.generation.genBaobabTrees && biomeContext.getKey().equals(BiomeKeys.SAVANNA.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.BAOBAB_TREE));


        BiomeModifications.postProcessProperties(biomeContext -> biomeContext.getKey().equals(BiomeKeys.DESERT.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ModFeatures.Configured.BURROW));
        BiomeModifications.postProcessProperties(biomeContext -> biomeContext.getKey().equals(BiomeKeys.DESERT.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.LAKES, ModFeatures.Configured.OASIS));
    }
}