package io.github.how_bout_no.outvoted.world.gen;

import dev.architectury.registry.level.biome.BiomeModifications;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.config.OutvotedConfigCommon;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;

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
        BiomeModifications.addProperties(biomeContext -> ent.glare.spawn && biomeContext.getProperties().getCategory() != Biome.Category.NETHER && biomeContext.getProperties().getCategory() != Biome.Category.THEEND,
                (biomeContext, mutable) -> mutable.getSpawnProperties().addSpawn(SpawnGroup.MONSTER,
                        new SpawnSettings.SpawnEntry(ModEntityTypes.GLARE.get(),
                                Outvoted.commonConfig.entities.glare.rate, 1, 1)));
    }

    private static boolean parseBiomes(List<String> biomes, BiomeModifications.BiomeContext biomeContext) {
        return biomes.contains(biomeContext.getKey().toString()) || biomes.contains("#" + biomeContext.getProperties().getCategory().asString());
    }
}