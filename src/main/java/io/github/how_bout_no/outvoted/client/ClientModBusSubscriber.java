package io.github.how_bout_no.outvoted.client;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.ModWoodType;
import io.github.how_bout_no.outvoted.client.model.WildfireShield;
import io.github.how_bout_no.outvoted.client.render.*;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModEntities;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.util.compat.PatchouliCompat;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import static io.github.how_bout_no.outvoted.Outvoted.id;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Outvoted.MOD_ID, value = Dist.CLIENT)
public class ClientModBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("patchouli")) PatchouliCompat.updateFlag();

        // register models
        ItemProperties.register(ModItems.WILDFIRE_SHIELD.get(), new ResourceLocation("blocking"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);

        ClampedItemPropertyFunction prop = (stack, world, entity, seed) -> stack.hasTag() && Config.wildfireVariants.get() ? stack.getTag().getFloat("SoulTexture") : 0.0F;
        ItemProperties.register(ModItems.WILDFIRE_HELMET.get(), id("soul_texture"), prop);

        ModWoodType.register(ModWoodType.PALM);
        BlockEntityRenderers.register(ModEntities.SIGN_BLOCK_ENTITIES.get(), SignRenderer::new);
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
        event.registerEntityRenderer(ModEntities.OSTRICH.get(), OstrichRenderer::new);
    }

    public static class ShieldTex {
        @SubscribeEvent
        public void registerTextureAtlas(TextureStitchEvent.Pre event) {
            event.addSprite(WildfireShield.base.texture());
            event.addSprite(WildfireShield.base_nop.texture());
        }
    }
}
