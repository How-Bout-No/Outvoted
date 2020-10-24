package com.hbn.outvoted;

import com.hbn.outvoted.entities.InfernoEntity;
import com.hbn.outvoted.init.ModEntityTypes;
import com.hbn.outvoted.init.ModItems;
import com.hbn.outvoted.items.InfernoSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("outvoted")
public class Outvoted
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "outvoted";

    public Outvoted() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModEntityTypes.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModEntityTypes.INFERNO.get(), InfernoEntity.setCustomAttributes().create());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    public static final ItemGroup TAB = new ItemGroup("modTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.DIAMOND_BLOCK);
        }
    };

    @Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModEventBusSubscriber {
        @SubscribeEvent
        public void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
            InfernoSpawnEggItem.initSpawnEggs();
        }
    }
}
