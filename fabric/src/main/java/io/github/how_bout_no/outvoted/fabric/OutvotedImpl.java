package io.github.how_bout_no.outvoted.fabric;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.WildfireShield;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class OutvotedImpl {
    public static void registerModels() {
        registerHelmetModels();
        registerShieldModels();
    }

    public static void registerShieldModels() {
        FabricModelPredicateProviderRegistry.register(ModItems.WILDFIRE_SHIELD.get(), new Identifier("blocking"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(OutvotedImpl::registerShieldTextures);
    }

    public static void registerHelmetModels() {
        UnclampedModelPredicateProvider prop = (stack, world, entity, seed) -> stack.hasNbt() && Outvoted.clientConfig.wildfireVariants ? stack.getNbt().getFloat("SoulTexture") : 0.0F;
        FabricModelPredicateProviderRegistry.register(ModItems.WILDFIRE_HELMET.get(), new Identifier(Outvoted.MOD_ID, "soul_texture"), prop);
    }

    public static void registerShieldTextures(SpriteAtlasTexture atlas, ClientSpriteRegistryCallback.Registry registry) {
        registry.register(WildfireShield.base.getTextureId());
        registry.register(WildfireShield.base_nop.getTextureId());
    }
}
