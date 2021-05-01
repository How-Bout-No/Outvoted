package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.item.WildfireShieldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean isBlocking();

    @Shadow
    public abstract ItemStack getActiveItem();

    @Inject(at = @At(value = "HEAD"), method = "damage", locals = LocalCapture.CAPTURE_FAILHARD)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (amount > 0.0F && this.isBlocking() && this.getActiveItem().getItem() instanceof WildfireShieldItem) {
            if (source.getSource() != null) {
                if (source.isProjectile()) {
                    source.getSource().setOnFireFor(5);
                } else {
                    source.getAttacker().setOnFireFor(5);
                }
            }
        }
    }
}
