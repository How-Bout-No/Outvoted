package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib3.item.GeoArmorItem;

/**
 * For some reason getArmorTexture is final in the GeoArmorItem... no problem!
 */
@Mixin(GeoArmorItem.class)
public abstract class MixinGeoArmorItem {
    private static final String HELMET_TEXTURE_SOUL = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire_soul.png").toString();

    @Inject(method = "getArmorTexture", at = @At("RETURN"), cancellable = true, remap = false)
    public void armorTextures(ItemStack stack, Entity entity, EquipmentSlot slot, String type, CallbackInfoReturnable<String> cir) {
        if (!(stack.getItem() instanceof WildfireHelmetItem)) return;
        if (Config.wildfireVariants.get()) {
            if (stack.getTag() != null && stack.getTag().getFloat("SoulTexture") == 1.0F) {
                cir.setReturnValue(HELMET_TEXTURE_SOUL);
            }
        }
    }
}
