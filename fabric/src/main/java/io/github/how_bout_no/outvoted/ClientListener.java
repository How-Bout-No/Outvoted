package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.model.ShieldModelProvider;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientListener implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Outvoted.clientInit();

        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.GLUTTON.get(), GluttonRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.BARNACLE.get(), BarnacleRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.MEERKAT.get(), MeerkatRenderer::new);
        EntityRendererRegistry.INSTANCE.register(ModEntityTypes.OSTRICH.get(), OstrichRenderer::new);

//        GeoArmorRendererRegistry.INSTANCE.register(WildfireHelmetItem.class, (context) -> new WildfireHelmetRenderer(context));
        ShieldModelProvider.registerItemsWithModelProvider();

        UnclampedModelPredicateProvider prop = (stack, world, entity, seed) -> stack.hasNbt() && Outvoted.clientConfig.wildfireVariants ? stack.getNbt().getFloat("SoulTexture") : 0.0F;
        FabricModelPredicateProviderRegistry.register(ModItems.WILDFIRE_HELMET.get(), new Identifier(Outvoted.MOD_ID, "soul_texture"), prop);
    }
}
