package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.render.HungerRenderer;
import io.github.how_bout_no.outvoted.client.render.WildfireRenderer;
import io.github.how_bout_no.outvoted.client.render.KrakenRenderer;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.item.ModdedSpawnEggItem;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HUNGER.get(), HungerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.KRAKEN.get(), KrakenRenderer::new);
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.WILDFIRE.get(), WildfireEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.HUNGER.get(), HungerEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.KRAKEN.get(), KrakenEntity.setCustomAttributes().create());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        EntitySpawnPlacementRegistry.register(ModEntityTypes.WILDFIRE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.HUNGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HungerEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.KRAKEN.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING, KrakenEntity::canSpawn);

        ModdedSpawnEggItem.initSpawnEggs();
    }
}
