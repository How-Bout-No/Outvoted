package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.ShieldModelProvider;
import io.github.how_bout_no.outvoted.client.render.HungerRenderer;
import io.github.how_bout_no.outvoted.client.render.KrakenRenderer;
import io.github.how_bout_no.outvoted.client.render.WildfireHelmetRenderer;
import io.github.how_bout_no.outvoted.client.render.WildfireRenderer;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.minecraft.block.Block;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusSubscriber {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HUNGER.get(), HungerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.KRAKEN.get(), KrakenRenderer::new);

        GeoArmorRenderer.registerArmorRenderer(WildfireHelmetItem.class, new WildfireHelmetRenderer());
        ShieldModelProvider.registerItemsWithModelProvider();

        RenderLayers.setRenderLayer(ModBlocks.PALM_SAPLING.get(), RenderLayer.getCutoutMipped());
        RenderLayers.setRenderLayer(ModBlocks.PALM_TRAPDOOR.get(), RenderLayer.getCutoutMipped());
        RenderLayers.setRenderLayer(ModBlocks.PALM_DOOR.get(), RenderLayer.getCutoutMipped());
        RenderLayers.setRenderLayer(ModBlocks.BAOBAB_SAPLING.get(), RenderLayer.getCutoutMipped());
        RenderLayers.setRenderLayer(ModBlocks.BAOBAB_TRAPDOOR.get(), RenderLayer.getCutoutMipped());
        RenderLayers.setRenderLayer(ModBlocks.BAOBAB_DOOR.get(), RenderLayer.getCutoutMipped());

        ModelPredicateProvider prop = (stack, world, entity) -> stack.hasTag() && Outvoted.config.get().entities.wildfire.variants ? stack.getTag().getFloat("SoulTexture") : 0.0F;
        ModelPredicateProviderRegistry.register(ModItems.WILDFIRE_HELMET.get(), new Identifier(Outvoted.MOD_ID, "soul_texture"), prop);

        SignSprites.addRenderMaterial(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/signs/palm")));
        SignSprites.addRenderMaterial(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/signs/baobab")));
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.WILDFIRE.get(), WildfireEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.HUNGER.get(), HungerEntity.setCustomAttributes().build());
        event.put(ModEntityTypes.KRAKEN.get(), KrakenEntity.setCustomAttributes().build());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        SpawnRestriction.register(ModEntityTypes.WILDFIRE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);
        SpawnRestriction.register(ModEntityTypes.HUNGER.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HungerEntity::canSpawn);
        SpawnRestriction.register(ModEntityTypes.KRAKEN.get(), SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING, KrakenEntity::canSpawn);

        ModSpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterBlocks(final RegistryEvent.Register<Block> event) {
        ModFireBlock.init();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterFeatures(final RegistryEvent.Register<Feature<?>> event) {
        ModFeatures.Configured.registerConfiguredFeatures();
    }
}
