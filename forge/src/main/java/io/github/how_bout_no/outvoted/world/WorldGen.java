package io.github.how_bout_no.outvoted.world;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldGen {
    /**
     * Checks entities in an area to force limit spawn count
     * Probably awful practice, but this is a quick and dirty way to force 1 mob in an area
     */
    @SubscribeEvent
    public static void checkMobs(LivingSpawnEvent.CheckSpawn event) {
        double area = 6.0; // Value for x, y, and z expansion to check for entities
        Entity e = event.getEntity();
        if (e instanceof KrakenEntity || e instanceof HungerEntity) {
            if (event.getSpawnReason() == SpawnReason.NATURAL && !event.getWorld().getLevelProperties().isHardcore()) {
                List<Entity> entities = event.getWorld().getOtherEntities(event.getEntity(), event.getEntity().getBoundingBox().stretch(area, area, area).stretch(-area, -area, -area));
                if (!entities.isEmpty()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        } else if (e instanceof WildfireEntity) {
            if (event.getSpawnReason() == SpawnReason.NATURAL && event.getWorld().getDifficulty() != Difficulty.HARD) {
                List<Entity> entities = event.getWorld().getOtherEntities(event.getEntity(), event.getEntity().getBoundingBox().stretch(area, area, area).stretch(-area, -area, -area));
                if (entities.stream().anyMatch(entity -> entity instanceof WildfireEntity)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    /**
     * Adds Blazes to Wildfire spawns and "adds" Wildfires to Mob Spawners
     */
    @SubscribeEvent
    public static void changeMobs(LivingSpawnEvent.SpecialSpawn event) {
        Entity e = event.getEntity();
        if (Outvoted.config.entities.wildfire.spawn) {
            if (e instanceof WildfireEntity) {
                if (event.getSpawnReason() == SpawnReason.NATURAL) {
                    World world = event.getEntity().getEntityWorld();
                    int max = 3;
                    switch (world.getDifficulty()) {
                        case NORMAL:
                            max = 4;
                            break;
                        case HARD:
                            max = 5;
                            break;
                    }
                    int min = max - 1;
                    int rand = new Random().nextInt(max - min) + min;
                    for (int i = 1; i <= rand; i++) {
                        BlazeEntity blaze = EntityType.BLAZE.create(world);
                        blaze.updatePositionAndAngles(e.getParticleX(2.0D), e.getY(), e.getParticleZ(2.0D), e.yaw, e.pitch);
                        while (!world.isAir(blaze.getBlockPos())) { // Should prevent spawning inside of blocks
                            blaze.updatePositionAndAngles(e.getParticleX(2.0D), e.getY(), e.getParticleZ(2.0D), e.yaw, e.pitch);
                        }
                        world.spawnEntity(blaze);
                    }
                }
            }
            if (e instanceof BlazeEntity) {
                if (event.getSpawnReason() == SpawnReason.SPAWNER) {
                    if (Math.random() > 0.85) {
                        World world = event.getEntity().getEntityWorld();

                        WildfireEntity wildfire = ModEntityTypes.WILDFIRE.get().create(world);
                        wildfire.updatePositionAndAngles(e.getX(), e.getY(), e.getZ(), e.yaw, e.pitch);

                        world.spawnEntity(wildfire);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
