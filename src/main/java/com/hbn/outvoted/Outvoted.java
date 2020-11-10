package com.hbn.outvoted;

import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.entities.hunger.HungerEntity;
import com.hbn.outvoted.entities.inferno.InfernoEntity;
import com.hbn.outvoted.entities.kraken.KrakenEntity;
import com.hbn.outvoted.init.ModEntityTypes;
import com.hbn.outvoted.init.ModItems;
import com.hbn.outvoted.util.ServerEvents;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("outvoted")
public class Outvoted {
    public static final String MOD_ID = "outvoted";

    public Outvoted() {
        final FMLJavaModLoadingContext modLoadingContext = FMLJavaModLoadingContext.get();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OutvotedConfig.COMMON_SPEC);

        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityTypes.INFERNO.get(), InfernoEntity.setCustomAttributes().create());
        });
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityTypes.HUNGER.get(), HungerEntity.setCustomAttributes().create());
        });
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityTypes.KRAKEN.get(), KrakenEntity.setCustomAttributes().create());
        });
    }

    public static final ItemGroup TAB = new ItemGroup("modTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.INFERNO_HELMET.get());
        }
    };
}
