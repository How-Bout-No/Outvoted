package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {
    public MixinAnvilMenu(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId, playerInventory, context);
    }

    @Redirect(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private Item overwriteResult(ItemStack itemStack) {
        if (this.inputSlots.getItem(0).getItem() instanceof WildfireHelmetItem && itemStack.getItem() == Items.ENCHANTED_BOOK) {
            if (EnchantmentHelper.getEnchantments(itemStack).containsKey(Enchantments.MENDING)) {
                return Items.AIR;
            }
        }
        return itemStack.getItem();
    }
}
