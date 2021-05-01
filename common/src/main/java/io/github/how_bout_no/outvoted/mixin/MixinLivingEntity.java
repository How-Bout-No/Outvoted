package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.item.WildfireShieldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static java.lang.Math.*;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    private DamageSource damageSource;

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getActiveItem();

    @Inject(at = @At(value = "HEAD"), method = "blockedByShield")
    private void blocked(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        damageSource = source;
    }

    @Redirect(method = "blockedByShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;dotProduct(Lnet/minecraft/util/math/Vec3d;)D"))
    private double injected(Vec3d vec3d, Vec3d vec) {
        if (this.getActiveItem().getItem() instanceof WildfireShieldItem) {
            Entity livingentity = damageSource.getSource();
            double d1 = livingentity.getX() - this.getX();
            double d2 = livingentity.getBodyY(0.5D) - this.getBodyY(0.5D);
            double d3 = livingentity.getZ() - this.getZ();

            return abs((atan2(d2, sqrt((d1 * d1) + (d3 * d3))))) < 1.2 ? -0.1 : 0;
        }
        return vec3d.dotProduct(vec);
    }
}
