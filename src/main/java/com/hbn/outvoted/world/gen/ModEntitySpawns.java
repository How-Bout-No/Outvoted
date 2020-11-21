package com.hbn.outvoted.world.gen;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.entities.InfernoEntity;
import com.hbn.outvoted.entities.KrakenEntity;
import com.hbn.outvoted.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntitySpawns {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void spawnEntities(BiomeLoadingEvent event) {
        String biomename = event.getName().toString();
        if (OutvotedConfig.COMMON.spawninferno.get()) {
            if (event.getCategory() == Biome.Category.NETHER) {
                if (biomename.equals("minecraft:soul_sand_valley")) {
                    event.getSpawns().withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.SOUL_BLAZE.get(), OutvotedConfig.COMMON.rateblaze.get(), 3, 4));
                } else {
                    event.getSpawns().withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.BLAZE, OutvotedConfig.COMMON.rateblaze.get(), 3, 4));
                }
            }
        }
        if (OutvotedConfig.COMMON.spawnhunger.get()) {
            if (event.getCategory() == Biome.Category.DESERT || event.getCategory() == Biome.Category.SWAMP) {
                event.getSpawns().withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.HUNGER.get(), OutvotedConfig.COMMON.ratehunger.get(), 1, 1));
            }
        }
        if (OutvotedConfig.COMMON.spawnkraken.get()) {
            if (event.getDepth() == -1.8F && !biomename.equals("minecraft:deep_frozen_ocean")) { // Possibly makes modded deep oceans compatible? (If those even exist, and use vanilla values)
                //if (biomename.equals("minecraft:deep_ocean") || biomename.equals("minecraft:deep_warm_ocean") || biomename.equals("minecraft:deep_lukewarm_ocean") || biomename.equals("minecraft:deep_cold_ocean")) {
                event.getSpawns().withSpawner(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(ModEntityTypes.KRAKEN.get(), OutvotedConfig.COMMON.ratekraken.get(), 1, 1));
            }
        }
    }

    @SubscribeEvent
    public static void checkMobs(LivingSpawnEvent.CheckSpawn event) { // Below is probably bad practice, but I don't know of any other way to force 1 mob
        double area = 6.0; // Value for x, y, and z expansion to check for entities
        Entity e = event.getEntity();
        if (OutvotedConfig.COMMON.spawnkraken.get()) {
            if (e instanceof KrakenEntity) {
                if (event.getSpawnReason() == SpawnReason.NATURAL) {
                    List<Entity> entities = event.getWorld().getEntitiesWithinAABBExcludingEntity(event.getEntity(), event.getEntity().getBoundingBox().expand(area, area / 2, area).expand(-area, -area / 2, -area));
                    if (!entities.isEmpty()) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void changeMobs(LivingSpawnEvent.SpecialSpawn event) {
        double area = 6.0D;
        Entity e = event.getEntity();
        if (OutvotedConfig.COMMON.spawninferno.get()) {
            if (e instanceof BlazeEntity) {
                if (event.getSpawnReason() == SpawnReason.NATURAL) {
                    List<Entity> allentities = event.getWorld().getEntitiesWithinAABBExcludingEntity(e, e.getBoundingBox().expand(area, area / 2, area).expand(-area, -area / 2, -area));
                    List<Entity> entities = new ArrayList<Entity>();
                    for (Entity entity : allentities) {
                        if (entity instanceof BlazeEntity) {
                            entities.add(entity);
                        }
                    }
                    if (entities.size() > 1 && allentities.stream().noneMatch(entity -> entity instanceof InfernoEntity)) {
                        World world = event.getEntity().getEntityWorld();
                        InfernoEntity inferno = ModEntityTypes.INFERNO.get().create(world);
                        //inferno.setPositionAndRotation(e.getPosXRandom(1.0D), e.getPosY(), e.getPosZRandom(1.0D), e.rotationYaw, e.rotationPitch);
                        inferno.setPositionAndRotation(e.getPosX(), e.getPosY(), e.getPosZ(), e.rotationYaw, e.rotationPitch);

                        world.addEntity(inferno);
                    }
                } else if (event.getSpawnReason() == SpawnReason.SPAWNER) {
                    if (Math.random() > 0.8) {
                        World world = event.getEntity().getEntityWorld();

                        InfernoEntity inferno = ModEntityTypes.INFERNO.get().create(world);
                        inferno.setPositionAndRotation(e.getPosXRandom(1.0D), e.getPosY(), e.getPosZRandom(2.0D), e.rotationYaw, e.rotationPitch);

                        world.addEntity(inferno);
                    }
                }
            }
            else if (event.getSpawnReason() == SpawnReason.SPAWNER) {
                if (Math.random() > 0.9 ) {
                    World world = event.getEntity().getEntityWorld();

                    InfernoEntity inferno = ModEntityTypes.INFERNO.get().create(world);
                    inferno.setPositionAndRotation(e.getPosXRandom(1.0D), e.getPosY(), e.getPosZRandom(2.0D), e.rotationYaw, e.rotationPitch);

                    world.addEntity(inferno);
                }
            }
        }
    }
}
