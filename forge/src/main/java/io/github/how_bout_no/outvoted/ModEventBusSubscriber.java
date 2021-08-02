package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.model.ShieldModelProvider;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.entity.*;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusSubscriber {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(FMLClientSetupEvent event) {
        Outvoted.clientInit();

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GLUTTON.get(), GluttonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BARNACLE.get(), BarnacleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MEERKAT.get(), MeerkatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.OSTRICH.get(), OstrichRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TERMITE.get(), TermiteRenderer::new);

        GeoArmorRenderer.registerArmorRenderer(WildfireHelmetItem.class, new WildfireHelmetRenderer());
        ShieldModelProvider.registerItemsWithModelProvider();

        ModelPredicateProvider prop = (stack, world, entity) -> stack.hasTag() && Outvoted.clientConfig.wildfireVariants ? stack.getTag().getFloat("SoulTexture") : 0.0F;
        ModelPredicateProviderRegistry.register(ModItems.WILDFIRE_HELMET.get(), new Identifier(Outvoted.MOD_ID, "soul_texture"), prop);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () ->
                (client, parent) -> AutoConfig.getConfigScreen(OutvotedConfig.class, parent).get());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        SpawnRestriction.register(ModEntityTypes.WILDFIRE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);
        SpawnRestriction.register(ModEntityTypes.GLUTTON.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GluttonEntity::canSpawn);
        SpawnRestriction.register(ModEntityTypes.BARNACLE.get(), SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING, BarnacleEntity::canSpawn);
        SpawnRestriction.register(ModEntityTypes.MEERKAT.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MeerkatEntity::canSpawn);
        SpawnRestriction.register(ModEntityTypes.OSTRICH.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, OstrichEntity::canSpawn);
        SpawnRestriction.register(ModEntityTypes.TERMITE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TermiteEntity::canSpawn);

        ModSpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterBlocks(final RegistryEvent.Register<Block> event) {
        ModFireBlock.init();
        ModPOITypes.POINT_OF_INTEREST_TYPES.register();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterFeatures(final RegistryEvent.Register<Feature<?>> event) {
        ModFeatures.Configured.registerConfiguredFeatures();
    }
}
