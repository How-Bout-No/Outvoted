package io.github.how_bout_no.outvoted.world;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

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
        if (e instanceof BarnacleEntity || e instanceof GluttonEntity || e instanceof WildfireEntity) {
            if (event.getSpawnReason() == SpawnReason.NATURAL) {
                List<Entity> entities = event.getWorld().getOtherEntities(event.getEntity(), event.getEntity().getBoundingBox().stretch(area, area, area).stretch(-area, -area, -area));
                if (entities.stream().anyMatch(entity -> entity instanceof BarnacleEntity || entity instanceof GluttonEntity || entity instanceof WildfireEntity)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
