package io.github.how_bout_no.outvoted.mixin;

import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.autoconfig.util.Utils;
import net.fabricmc.api.EnvType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.nio.file.Paths;

@Mixin(Utils.class)
public class MixinUtils {
    @Inject(method = "getConfigFolder", at = @At("RETURN"), cancellable = true, remap = false)
    private static void injectConfig(CallbackInfoReturnable<Path> cir) {
        System.out.println("conf");
        if (Platform.getEnv() == EnvType.SERVER)
            cir.setReturnValue(Paths.get(cir.getReturnValue().toString(), "/outvoted/"));
    }
}
