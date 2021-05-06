package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.util.PlayerUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements PlayerUtil {
    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /*
     This overwrites the call to isShield() which for whatever reason I can't get to run when I specify the target method
     Thus, we just redirect the first call in this method which doesn't seem like it would work but... it does?
    */
    @Redirect(method = "damageShield(F)V", at = @At(value = "INVOKE", ordinal = 0))
    private boolean sh(ItemStack stack, LivingEntity entity) {
        return stack.getItem() instanceof ShieldItem;
    }
}