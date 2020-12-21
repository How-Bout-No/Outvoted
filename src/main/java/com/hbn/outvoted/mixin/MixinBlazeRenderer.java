package com.hbn.outvoted.mixin;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.util.IMixinBlazeEntity;
import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Replace Blaze textures with soul/blue variant when in Soul Sand Valleys, akin to Infernos
 */
@Mixin(BlazeRenderer.class)
public abstract class MixinBlazeRenderer {
    private static final ResourceLocation BLAZE_TEXTURES_SOUL = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/soul_blaze.png");

    @Inject(method = "getEntityTexture", at = @At("RETURN"), cancellable = true)
    private void soulTextures(BlazeEntity entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (!OutvotedConfig.COMMON.infernovariant.get()) return;
        if (((IMixinBlazeEntity) entity).getVariant() == 1) cir.setReturnValue(BLAZE_TEXTURES_SOUL);
    }
}
