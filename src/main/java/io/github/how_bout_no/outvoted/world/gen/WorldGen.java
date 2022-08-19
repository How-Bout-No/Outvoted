package io.github.how_bout_no.outvoted.world.gen;

import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModEntities;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldGen {
    // BiomeLoadingEvent can be used to add placed features to existing biomes.
    // Placed features added in the BiomeLoadingEvent must have been previously registered.
    // JSON features cannot be added to biomes via the BiomeLoadingEvent.
    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.BiomeCategory.DESERT) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACED_BURROW.getHolder().get());
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACED_PALM.getHolder().get());
        } else if (event.getCategory() == Biome.BiomeCategory.SAVANNA) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACED_BAOBAB.getHolder().get());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addSpawns(final BiomeLoadingEvent event) {
        if (Config.wildfireSpawn.get() && validBiome(event, Config.wildfireBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.WILDFIRE.get(),
                            Config.wildfireRate.get(), 1, 1)
            );
        } else if (Config.gluttonSpawn.get() && validBiome(event, Config.gluttonBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.GLUTTON.get(),
                            Config.gluttonRate.get(), 1, 1)
            );
        } else if (Config.barnacleSpawn.get() && validBiome(event, Config.barnacleBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.BARNACLE.get(),
                            Config.barnacleRate.get(), 1, 1)
            );
        } else if (Config.glareSpawn.get() && validBiome(event, Config.glareBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntities.GLARE.get(),
                            Config.glareRate.get(), 1, 1)
            );
        } else if (Config.ostrichSpawn.get() && validBiome(event, Config.ostrichBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.CREATURE,
                    new MobSpawnSettings.SpawnerData(ModEntities.OSTRICH.get(),
                            Config.ostrichRate.get(), 1, 3)
            );
        }
    }

    private static boolean validBiome(BiomeLoadingEvent biome, List<? extends String> filter) {
        if (biome.getName() == null) return false;
        return filter.contains(biome.getName().getPath()) || filter.contains("#" + biome.getCategory().getName());
    }
}