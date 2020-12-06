package com.hbn.outvoted.mixin;

import com.hbn.outvoted.Outvoted;
import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlazeRenderer.class)
public class MixinBlazeRenderer {
    @Inject(method = "getEntityTexture", at = @At("RETURN"), remap = false, cancellable = true)
    private void soulTextures(BlazeEntity entity, CallbackInfoReturnable<ResourceLocation> cir){
        ResourceLocation name = entity.world.getBiome(entity.getPosition()).getRegistryName();
        if (name != null) cir.setReturnValue(name.toString().equals("minecraft:soul_sand_valley") ? new ResourceLocation(Outvoted.MOD_ID, "textures/entity/soul_blaze.png") : cir.getReturnValue());
    }
}
