package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.ShieldModelProvider;
import io.github.how_bout_no.outvoted.client.render.HungerRenderer;
import io.github.how_bout_no.outvoted.client.render.KrakenRenderer;
import io.github.how_bout_no.outvoted.client.render.MeerkatRenderer;
import io.github.how_bout_no.outvoted.client.render.WildfireRenderer;
import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import io.github.how_bout_no.outvoted.init.ModFireBlock;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.Block;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
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
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusSubscriber {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(FMLClientSetupEvent event) {
        Outvoted.clientInit();

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HUNGER.get(), HungerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.KRAKEN.get(), KrakenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MEERKAT.get(), MeerkatRenderer::new);

        ShieldModelProvider.registerItemsWithModelProvider();

        ModelPredicateProvider prop = (stack, world, entity) -> stack.hasTag() && Outvoted.config.entities.wildfire.variants ? stack.getTag().getFloat("SoulTexture") : 0.0F;
        ModelPredicateProviderRegistry.register(ModItems.WILDFIRE_HELMET.get(), new Identifier(Outvoted.MOD_ID, "soul_texture"), prop);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () ->
                (client, parent) -> AutoConfig.getConfigScreen(OutvotedConfig.class, parent).get());
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
