package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepairCostRecipe extends SpecialRecipe {
    public RepairCostRecipe(ResourceLocation res) {
        super(res);
    }

    /**
     * Check matching items in crafting grid
     */
    public boolean matches(CraftingInventory inv, World worldIn) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if ((itemstack.getItem() != ModItems.VOID_HEART.get() && itemstack1.getItem() != ModItems.VOID_HEART.get()) || (!hasEnchantibility(itemstack) && !hasEnchantibility(itemstack1))) {
                        return false;
                    }
                }
            }
        }

        return list.size() == 2;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(CraftingInventory inv) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if ((itemstack.getItem() != ModItems.VOID_HEART.get() && itemstack1.getItem() != ModItems.VOID_HEART.get()) || (!hasEnchantibility(itemstack) && !hasEnchantibility(itemstack1))) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (list.size() == 2) {
            ItemStack itemstack3 = list.get(0);
            ItemStack itemstack4 = list.get(1);
            if ((itemstack3.getItem() == ModItems.VOID_HEART.get() || itemstack4.getItem() == ModItems.VOID_HEART.get()) && (hasEnchantibility(itemstack3) || hasEnchantibility(itemstack4))) {
                ItemStack item;
                Map<Enchantment, Integer> map;
                item = hasEnchantibility(itemstack3) ? itemstack3 : itemstack4;
                map = EnchantmentHelper.getEnchantments(item);

                ItemStack itemstack2 = new ItemStack(item.getItem());
                itemstack2.setRepairCost(0);
                itemstack2.setDamage(item.getDamage());
                EnchantmentHelper.setEnchantments(map, itemstack2);

                return itemstack2;
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean hasEnchantibility(ItemStack itemStack) {
        return itemStack.isEnchantable() || itemStack.isEnchanted();
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.REPAIR_COST.get();
    }
}
