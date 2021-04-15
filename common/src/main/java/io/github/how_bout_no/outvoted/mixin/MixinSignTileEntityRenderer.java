// Adapted from noobutil https://maven.blamejared.com/noobanidus/libs/noobutil/1.16.4-0.0.7.62/

package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.block.IModdedSign;
import net.minecraft.block.Block;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlockEntityRenderer.class)
public abstract class MixinSignTileEntityRenderer {
    @Inject(method = "getModelTexture", at = @At("HEAD"), cancellable = true)
    private static void getMaterial(Block block, CallbackInfoReturnable<SpriteIdentifier> info) {
        if (block instanceof IModdedSign) {
            Identifier texture = ((IModdedSign) block).getTexture();
            info.setReturnValue(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, texture));
        }
    }
}
