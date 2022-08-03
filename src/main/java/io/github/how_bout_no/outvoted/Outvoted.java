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
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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

    public static final ResourceLocation BIOME_CATEGORY_PLACEMENT = new ResourceLocation(MOD_ID, "biome_category");
    public static final ResourceLocation RED_WOOL = new ResourceLocation(MOD_ID, "red_wool");
    public static final ResourceLocation LAZY_RED_WOOL = new ResourceLocation(MOD_ID, "lazy_red_wool");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_RED_WOOL = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, RED_WOOL);
    public static final ResourceKey<PlacedFeature> PLACED_RED_WOOL = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, RED_WOOL);


    public Outvoted() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(this::clientSetup);
        forgeBus.register(this);

        GeckoLib.initialize();
        GeckoLibMod.DISABLE_IN_DEV = true;

        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ITEMS.register(modBus);
        ModEntityTypes.ENTITY_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModRecipes.RECIPES.register(modBus);
        ModSounds.SOUNDS.register(modBus);
        ModFeatures.PLACED_FEATURES.register(modBus);
        ModFeatures.CONFIGURED_FEATURES.register(modBus);
        ModTags.init();
        ForgeConfig.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.register(new ShieldTex()));
        forgeBus.addListener(this::onBiomeLoading);
    }

    // BiomeLoadingEvent can be used to add placed features to existing biomes.
    // Placed features added in the BiomeLoadingEvent must have been previously registered.
    // JSON features cannot be added to biomes via the BiomeLoadingEvent.
    private void onBiomeLoading(BiomeLoadingEvent event)
    {
        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACED_TNT_PILE.getHolder().get());
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
        event.registerEntityRenderer(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GLUTTON.get(), GluttonRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BARNACLE.get(), BarnacleRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GLARE.get(), GlareRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        SpawnUtil.registerRestrictions();
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.WILDFIRE.get(), WildfireEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.GLUTTON.get(), GluttonEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.BARNACLE.get(), BarnacleEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.GLARE.get(), GlareEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemEntity.setCustomAttributes().build());
    }

    static class ShieldTex {
        @SubscribeEvent
        public void registerTextureAtlas(TextureStitchEvent.Pre event) {
            event.addSprite(WildfireShield.base.texture());
            event.addSprite(WildfireShield.base_nop.texture());
        }
    }
}
