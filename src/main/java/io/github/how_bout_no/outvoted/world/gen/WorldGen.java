package io.github.how_bout_no.outvoted.world.gen;

import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldGen {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addSpawns(final BiomeLoadingEvent event) {
        if (Config.wildfireSpawn.get() && validBiome(event, Config.wildfireBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntityTypes.WILDFIRE.get(),
                            Config.wildfireRate.get(), 1, 1)
            );
        } else if (Config.gluttonSpawn.get() && validBiome(event, Config.gluttonBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntityTypes.GLUTTON.get(),
                            Config.gluttonRate.get(), 1, 1)
            );
        } else if (Config.barnacleSpawn.get() && validBiome(event, Config.barnacleBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntityTypes.BARNACLE.get(),
                            Config.barnacleRate.get(), 1, 1)
            );
        } else if (Config.glareSpawn.get() && validBiome(event, Config.glareBiomes.get())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(ModEntityTypes.GLARE.get(),
                            Config.glareRate.get(), 1, 1)
            );
        }
    }

    private static boolean validBiome(BiomeLoadingEvent biome, List<? extends String> filter) {
        if (biome.getName() == null) return false;
        return filter.contains(biome.getName().getPath()) || filter.contains("#" + biome.getCategory().getSerializedName());
    }
}