package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.model.WildfireShield;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.config.ForgeConfig;
import io.github.how_bout_no.outvoted.entity.*;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.util.compat.PatchouliCompat;
import io.github.how_bout_no.outvoted.world.SpawnUtil;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

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

        modBus.addListener(this::clientSetup);
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

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.register(new ShieldTex()));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("patchouli")) PatchouliCompat.updateFlag();

        // register models
        ItemProperties.register(ModItems.WILDFIRE_SHIELD.get(), new ResourceLocation("blocking"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);

        ClampedItemPropertyFunction prop = (stack, world, entity, seed) -> stack.hasTag() && Config.wildfireVariants.get() ? stack.getTag().getFloat("SoulTexture") : 0.0F;
        ItemProperties.register(ModItems.WILDFIRE_HELMET.get(), new ResourceLocation(Outvoted.MOD_ID, "soul_texture"), prop);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(WildfireHelmetItem.class, new WildfireHelmetRenderer());
    }

    @SubscribeEvent
    public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.WILDFIRE.get(), WildfireRenderer::new);
        event.registerEntityRenderer(ModEntities.GLUTTON.get(), GluttonRenderer::new);
        event.registerEntityRenderer(ModEntities.BARNACLE.get(), BarnacleRenderer::new);
        event.registerEntityRenderer(ModEntities.GLARE.get(), GlareRenderer::new);
        event.registerEntityRenderer(ModEntities.COPPER_GOLEM.get(), CopperGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.MEERKAT.get(), MeerkatRenderer::new);
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
    }

    static class ShieldTex {
        @SubscribeEvent
        public void registerTextureAtlas(TextureStitchEvent.Pre event) {
            event.addSprite(WildfireShield.base.texture());
            event.addSprite(WildfireShield.base_nop.texture());
        }
    }
}
