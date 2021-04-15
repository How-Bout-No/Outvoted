// Adapted from noobutil https://maven.blamejared.com/noobanidus/libs/noobutil/1.16.4-0.0.7.62/

package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.util.SignSprites;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(TexturedRenderLayers.class)
public abstract class MixinAtlases {
    @Inject(method = "addDefaultTextures", at = @At("RETURN"))
    private static void collectModdedSigns(Consumer<SpriteIdentifier> consumer, CallbackInfo info) {
        for (SpriteIdentifier material : SignSprites.getSprites()) {
            consumer.accept(material);
        }
    }
}
