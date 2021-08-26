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

import java.util.List;

public class WorldGen {
    public static void addSpawnEntries() {
        OutvotedConfigCommon.Entities ent = Outvoted.commonConfig.entities;

        BiomeModifications.addProperties(biomeContext -> ent.wildfire.spawn && parseBiomes(ent.wildfire.biomes, biomeContext),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.WILDFIRE.get(),
                                Outvoted.commonConfig.entities.wildfire.rate, 1, 1)));
        BiomeModifications.addProperties(biomeContext -> ent.glutton.spawn && parseBiomes(ent.glutton.biomes, biomeContext),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.GLUTTON.get(),
                                Outvoted.commonConfig.entities.glutton.rate, 1, 1)));
        BiomeModifications.addProperties(biomeContext -> ent.barnacle.spawn && parseBiomes(ent.barnacle.biomes, biomeContext),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.WATER_CREATURE,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.BARNACLE.get(),
                                Outvoted.commonConfig.entities.barnacle.rate, 1, 1)));
//        BiomeModifications.addProperties(biomeContext -> ent.meerkat.spawn && parseBiomes(ent.meerkat.biomes, biomeContext),
//                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.CREATURE,
//                        new SpawnSettings.SpawnEntry(ModEntityTypes.MEERKAT.get(),
//                                Outvoted.commonConfig.entities.meerkat.rate, 2, 4)));
        BiomeModifications.addProperties(biomeContext -> ent.meerkat.spawn && parseBiomes(ent.meerkat.biomes, biomeContext),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ModFeatures.Configured.BURROW));
        BiomeModifications.addProperties(biomeContext -> ent.ostrich.spawn && parseBiomes(ent.ostrich.biomes, biomeContext),
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.CREATURE,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.OSTRICH.get(),
                                Outvoted.commonConfig.entities.ostrich.rate, 1, 3)));

        BiomeModifications.addProperties(biomeContext -> Outvoted.commonConfig.generation.genPalmTrees && biomeContext.getKey().equals(BiomeKeys.DESERT_LAKES.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.PALM_TREE));
        BiomeModifications.addProperties(biomeContext -> Outvoted.commonConfig.generation.genBaobabTrees && biomeContext.getKey().equals(BiomeKeys.SAVANNA.getValue()),
                (biomeContext, mutable) -> mutable.getGenerationProperties()
                        .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ModFeatures.Configured.BAOBAB_TREE));

    }

    private static boolean parseBiomes(List<String> biomes, BiomeModifications.BiomeContext biomeContext) {
        return biomes.contains(biomeContext.getKey().toString()) || biomes.contains("#" + biomeContext.getProperties().getCategory().asString());
    }
}