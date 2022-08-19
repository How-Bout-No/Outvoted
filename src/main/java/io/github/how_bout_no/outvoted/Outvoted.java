package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.block.ModWoodType;
import io.github.how_bout_no.outvoted.client.ClientModBusSubscriber;
import io.github.how_bout_no.outvoted.config.ForgeConfig;
import io.github.how_bout_no.outvoted.entity.*;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.world.SpawnUtil;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

@Mod(Outvoted.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Outvoted {
    public static final String MOD_ID = "outvoted";
    public static CreativeModeTab TAB = new CreativeModeTab("outvotedtab") {
        @Override
        public ItemStack makeIcon() {
            return ModItems.WILDFIRE_HELMET.get().getDefaultInstance();
        }
    };

    public Outvoted() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        forgeBus.register(this);

        GeckoLib.initialize();
        GeckoLibMod.DISABLE_IN_DEV = true;

        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ITEMS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModEntities.BLOCK_ENTITIES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModRecipes.RECIPES.register(modBus);
        ModSounds.SOUNDS.register(modBus);
        ModFeatures.PLACED_FEATURES.register(modBus);
        ModFeatures.CONFIGURED_FEATURES.register(modBus);
        ForgeConfig.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.register(new ClientModBusSubscriber.ShieldTex()));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(Outvoted.MOD_ID, path);
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        Sheets.addWoodType(ModWoodType.PALM);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        SpawnUtil.registerRestrictions();
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.WILDFIRE.get(), Wildfire.setCustomAttributes().build());
        event.put(ModEntities.GLUTTON.get(), Glutton.setCustomAttributes().build());
        event.put(ModEntities.BARNACLE.get(), Barnacle.setCustomAttributes().build());
        event.put(ModEntities.GLARE.get(), Glare.setCustomAttributes().build());
        event.put(ModEntities.COPPER_GOLEM.get(), CopperGolem.setCustomAttributes().build());
        event.put(ModEntities.MEERKAT.get(), Meerkat.setCustomAttributes().build());
        event.put(ModEntities.OSTRICH.get(), Ostrich.setCustomAttributes().build());
    }
}
