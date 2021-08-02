package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.client.WildfireShield;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

public class ShieldModelProvider {
    public static void registerItemsWithModelProvider() {
        registerShieldModels();
    }

    private static void registerShieldModels() {
        FabricModelPredicateProviderRegistry.register(ModItems.WILDFIRE_SHIELD.get(), new Identifier("blocking"),
                (itemStack, clientWorld, livingEntity) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(ShieldModelProvider::registerShieldTextures);
    }

    public static void registerShieldTextures(SpriteAtlasTexture atlas, ClientSpriteRegistryCallback.Registry registry) {
        registry.register(WildfireShield.base.getTextureId());
        registry.register(WildfireShield.base_nop.getTextureId());
    }
}
