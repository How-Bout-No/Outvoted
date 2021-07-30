package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.init.ModSignType;
import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(SignType.class)
public abstract class MixinSignType {
    @Inject(method = "stream", at = @At("HEAD"))
    private static void overrideStream(CallbackInfoReturnable<Stream<SignType>> cir) {
        ModSignType.init();
    }
}
