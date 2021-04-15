package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ShieldModelProvider {
    public static void registerItemsWithModelProvider() {
        registerShieldModels();
    }

    private static void registerShieldModels() {
        ModelPredicateProviderRegistry.register(ModItems.WILDFIRE_SHIELD.get(), new Identifier("blocking"), (itemStack, clientWorld, livingEntity) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });
    }
}
