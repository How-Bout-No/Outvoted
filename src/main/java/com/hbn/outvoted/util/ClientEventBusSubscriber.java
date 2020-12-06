package com.hbn.outvoted.util;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.render.HungerRenderer;
import com.hbn.outvoted.client.render.InfernoRenderer;
import com.hbn.outvoted.client.render.KrakenRenderer;
import com.hbn.outvoted.entity.HungerEntity;
import com.hbn.outvoted.entity.InfernoEntity;
import com.hbn.outvoted.entity.KrakenEntity;
import com.hbn.outvoted.init.ModEntityTypes;
import com.hbn.outvoted.item.ModdedSpawnEggItem;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.INFERNO.get(), InfernoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HUNGER.get(), HungerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.KRAKEN.get(), KrakenRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        GlobalEntityTypeAttributes.put(ModEntityTypes.INFERNO.get(), InfernoEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.HUNGER.get(), HungerEntity.setCustomAttributes().create());
        GlobalEntityTypeAttributes.put(ModEntityTypes.KRAKEN.get(), KrakenEntity.setCustomAttributes().create());

        EntitySpawnPlacementRegistry.register(ModEntityTypes.INFERNO.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.HUNGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HungerEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.KRAKEN.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING, KrakenEntity::canSpawn);

        ModdedSpawnEggItem.initSpawnEggs();
    }
}
