package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MendingEnchantment.class)
public abstract class MixinMendingEnchantment extends Enchantment {
    protected MixinMendingEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentCategory.BREAKABLE, slotTypes);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return !(stack.getItem() instanceof WildfireHelmetItem) && super.canEnchant(stack);
    }
}
