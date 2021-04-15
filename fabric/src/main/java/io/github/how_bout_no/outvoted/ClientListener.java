package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.model.ShieldModelProvider;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class ClientListener implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.WILDFIRE.get(), (entityRenderDispatcher, context) -> new WildfireRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.HUNGER.get(), (entityRenderDispatcher, context) -> new HungerRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.KRAKEN.get(), (entityRenderDispatcher, context) -> new KrakenRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.MEERKAT.get(), (entityRenderDispatcher, context) -> new MeerkatRenderer(entityRenderDispatcher));

        GeoArmorRenderer.registerArmorRenderer(WildfireHelmetItem.class, new WildfireHelmetRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.WILDFIRE_SHIELD.get(), ShieldRenderer::render);
        ShieldModelProvider.registerItemsWithModelProvider();

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALM_SAPLING.get(), RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALM_TRAPDOOR.get(), RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALM_DOOR.get(), RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BAOBAB_SAPLING.get(), RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BAOBAB_TRAPDOOR.get(), RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BAOBAB_DOOR.get(), RenderLayer.getCutoutMipped());
    }
}
