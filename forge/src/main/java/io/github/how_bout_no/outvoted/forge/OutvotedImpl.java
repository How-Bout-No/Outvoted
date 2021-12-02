package io.github.how_bout_no.outvoted.forge;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OutvotedImpl {
    public static void registerModels() {
        registerHelmetModels();
        registerShieldModels();
    }

    public static void registerShieldModels() {
        ModelPredicateProviderRegistry.register(ModItems.WILDFIRE_SHIELD.get(), new Identifier("blocking"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
    }

    public static void registerHelmetModels() {
        UnclampedModelPredicateProvider prop = (stack, world, entity, seed) -> stack.hasNbt() && Outvoted.clientConfig.wildfireVariants ? stack.getNbt().getFloat("SoulTexture") : 0.0F;
        ModelPredicateProviderRegistry.register(ModItems.WILDFIRE_HELMET.get(), new Identifier(Outvoted.MOD_ID, "soul_texture"), prop);
    }
}
