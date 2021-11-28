package io.github.how_bout_no.outvoted.fabric;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class ClientListener implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Outvoted.clientInit();

        EntityRendererRegistry.register(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GLUTTON.get(), GluttonRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.BARNACLE.get(), BarnacleRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GLARE.get(), GlareRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);

        GeoArmorRenderer.registerArmorRenderer(new WildfireHelmetRenderer(), ModItems.WILDFIRE_HELMET.get());
    }
}
