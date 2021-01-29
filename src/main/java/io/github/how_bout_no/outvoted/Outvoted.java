package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.util.ServerEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

@Mod("outvoted")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Outvoted {
    public static final String MOD_ID = "outvoted";
    public static ItemGroup TAB_BLOCKS;
    public static ItemGroup TAB_DECO;
    public static ItemGroup TAB_COMBAT;
    //public static ItemGroup TAB_MATERIAL;
    public static ItemGroup TAB_MISC;

    public static final Logger LOGGER = LogManager.getLogger();

    public Outvoted() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OutvotedConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OutvotedConfig.COMMON_SPEC);

        GeckoLib.initialize();
        GeckoLibMod.DISABLE_IN_DEV = true;
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ITEMS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        ModRecipes.RECIPES.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new ServerEvents());
    }

    public void setup(final FMLCommonSetupEvent event) {
        if (OutvotedConfig.CLIENT.creativetab.get()) {
            ItemGroup TAB = new ItemGroup("modtab") {
                public ItemStack createIcon() {
                    return new ItemStack(ModItems.INFERNO_HELMET.get());
                }
            };
            TAB_BLOCKS = TAB;
            TAB_DECO = TAB;
            TAB_COMBAT = TAB;
            TAB_MISC = TAB;
        } else {
            TAB_BLOCKS = ItemGroup.BUILDING_BLOCKS;
            TAB_DECO = ItemGroup.DECORATIONS;
            TAB_COMBAT = ItemGroup.COMBAT;
            TAB_MISC = ItemGroup.MISC;
        }
    }
}
