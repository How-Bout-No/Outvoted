package com.hbn.outvoted.util;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.render.InfernoRenderer;
import com.hbn.outvoted.init.ModEntityTypes;
import com.hbn.outvoted.items.InfernoSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusSubscriber {

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        InfernoSpawnEggItem.initSpawnEggs();
    }
}
