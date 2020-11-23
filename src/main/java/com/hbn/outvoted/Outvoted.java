package com.hbn.outvoted;

import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.init.ModEntityTypes;
import com.hbn.outvoted.init.ModItems;
import com.hbn.outvoted.init.ModRecipes;
import com.hbn.outvoted.util.ServerEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod("outvoted")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Outvoted {
    public static final String MOD_ID = "outvoted";
    public static ItemGroup TAB_COMBAT;
    public static ItemGroup TAB_MISC;

    public Outvoted() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OutvotedConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OutvotedConfig.COMMON_SPEC);

        GeckoLib.initialize();
        ModItems.ITEMS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModRecipes.RECIPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new ServerEvents());
    }

    private void setup(final FMLLoadCompleteEvent event) {
        if (OutvotedConfig.CLIENT.creativetab.get()) {
            ItemGroup TAB = new ItemGroup(-1, "modTab") {
                public ItemStack createIcon() {
                    return new ItemStack(ModItems.INFERNO_HELMET.get());
                }
            };
            TAB_COMBAT = TAB;
            TAB_MISC = TAB;
        } else {
            TAB_COMBAT = ItemGroup.COMBAT;
            TAB_MISC = ItemGroup.MISC;
        }
    }


}
