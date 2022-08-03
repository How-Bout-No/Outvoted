package io.github.how_bout_no.outvoted.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Fix mobs to interact with ANY shield so long as it extends from ShieldItem
@Mixin(Mob.class)
public abstract class MixinMob {
    @Redirect(method = "maybeDisableShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    public boolean generalize(ItemStack itemStack, Item item) {
        return itemStack.getItem() instanceof ShieldItem;
    }
}
