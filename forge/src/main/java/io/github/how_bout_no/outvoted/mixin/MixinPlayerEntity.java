package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.util.PlayerUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements PlayerUtil {
    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "damageShield(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/extensions/IForgeItem;isShield(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Z"), remap = false)
    private boolean shields(IForgeItem iForgeItem, ItemStack stack, LivingEntity entity) {
        System.out.println("called");
        return stack.getItem() instanceof ShieldItem;
    }
}