package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.model.ShieldModelProvider;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderer.geo.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class ClientListener implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Outvoted.clientInit();

        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.WILDFIRE.get(), (entityRenderDispatcher, context) -> new WildfireRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.HUNGER.get(), (entityRenderDispatcher, context) -> new HungerRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.BARNACLE.get(), (entityRenderDispatcher, context) -> new BarnacleRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.MEERKAT.get(), (entityRenderDispatcher, context) -> new MeerkatRenderer(entityRenderDispatcher));

        GeoArmorRenderer.registerArmorRenderer(WildfireHelmetItem.class, new WildfireHelmetRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.WILDFIRE_SHIELD.get(), ShieldRenderer::render);
        ShieldModelProvider.registerItemsWithModelProvider();

        ModelPredicateProvider prop = (stack, world, entity) -> stack.hasTag() && Outvoted.config.client.wildfireVariants ? stack.getTag().getFloat("SoulTexture") : 0.0F;
        FabricModelPredicateProviderRegistry.register(ModItems.WILDFIRE_HELMET.get(), new Identifier(Outvoted.MOD_ID, "soul_texture"), prop);
    }
}
