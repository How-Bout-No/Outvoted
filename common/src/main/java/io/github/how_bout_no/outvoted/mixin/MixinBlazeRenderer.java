package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.config.OutvotedConfigCommon;
import io.github.how_bout_no.outvoted.entity.util.IMixinBlazeEntity;
import net.minecraft.client.render.entity.BlazeEntityRenderer;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Replace Blaze textures with soul/blue variant when in Soul Sand Valleys, akin to Wildfires
 */
@Mixin(BlazeEntityRenderer.class)
public abstract class MixinBlazeRenderer {
    private static final Identifier BLAZE_TEXTURES_SOUL = new Identifier(Outvoted.MOD_ID, "textures/entity/soul_blaze.png");

    @Inject(method = "getTexture", at = @At("RETURN"), cancellable = true)
    private void soulTextures(BlazeEntity entity, CallbackInfoReturnable<Identifier> cir) {
        if (!OutvotedConfigCommon.Entities.Wildfire.isVariants()) return;
        if (((IMixinBlazeEntity) entity).getVariant() == 1) cir.setReturnValue(BLAZE_TEXTURES_SOUL);
    }
}
