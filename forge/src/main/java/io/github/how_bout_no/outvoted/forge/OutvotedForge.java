package io.github.how_bout_no.outvoted.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.WildfireShield;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.world.SpawnUtil;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod(Outvoted.MOD_ID)
public class OutvotedForge {
    public OutvotedForge() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Outvoted.MOD_ID, modEventBus);
        Outvoted.init();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.register(new ShieldTex()));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusSubscriber {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            Outvoted.clientInit();

            ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                    () -> new ConfigGuiHandler.ConfigGuiFactory((mc, screen) -> AutoConfig.getConfigScreen(OutvotedConfig.class, screen).get()));
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
            GeoArmorRenderer.registerArmorRenderer(WildfireHelmetItem.class, new WildfireHelmetRenderer());
        }

        @SubscribeEvent
        public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.GLUTTON.get(), GluttonRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.BARNACLE.get(), BarnacleRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.GLARE.get(), GlareRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
            ModSpawnEggItem.initSpawnEggs();
            SpawnUtil.registerRestrictions();
        }
    }

    static class ShieldTex {
        @SubscribeEvent
        public void registerTextureAtlas(TextureStitchEvent.Pre event) {
            event.addSprite(WildfireShield.base.getTextureId());
            event.addSprite(WildfireShield.base_nop.getTextureId());
        }
    }
}
