package com.hbn.outvoted.util;

import com.google.common.collect.Lists;
import com.hbn.outvoted.init.ModItems;
import com.hbn.outvoted.init.ModRecipes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class RepairCostRecipe extends SpecialRecipe {
    public RepairCostRecipe(ResourceLocation res) {
        super(res);
    }


    public boolean matches(CraftingInventory inv, World worldIn) {
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if ((!(itemstack.getItem() == ModItems.VOID_HEART.get()) && !(itemstack1.getItem() == ModItems.VOID_HEART.get())) || ((!(itemstack.getItem() instanceof TieredItem) && !(itemstack.getItem() instanceof ArmorItem)) && (!(itemstack1.getItem() instanceof TieredItem) && !(itemstack1.getItem() instanceof ArmorItem)))) {
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
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if ((!(itemstack.getItem() == ModItems.VOID_HEART.get()) && !(itemstack1.getItem() == ModItems.VOID_HEART.get())) || ((!(itemstack.getItem() instanceof TieredItem) && !(itemstack.getItem() instanceof ArmorItem)) && (!(itemstack1.getItem() instanceof TieredItem) && !(itemstack1.getItem() instanceof ArmorItem)))) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (list.size() == 2) {
            ItemStack itemstack3 = list.get(0);
            ItemStack itemstack4 = list.get(1);
            if ((itemstack3.getItem() == ModItems.VOID_HEART.get() || itemstack4.getItem() == ModItems.VOID_HEART.get()) && ((itemstack3.getItem() instanceof TieredItem || itemstack3.getItem() instanceof ArmorItem) || (itemstack4.getItem() instanceof TieredItem || itemstack4.getItem() instanceof ArmorItem))) {
                Item item;
                Map<Enchantment, Integer> map;
                if (itemstack3.getItem() instanceof TieredItem || itemstack3.getItem() instanceof ArmorItem) {
                    item = itemstack3.getItem();
                    map = EnchantmentHelper.getEnchantments(itemstack3);
                } else {
                    item = itemstack4.getItem();
                    map = EnchantmentHelper.getEnchantments(itemstack4);
                }

                ItemStack itemstack2 = new ItemStack(item);
                itemstack2.setRepairCost(0);
                EnchantmentHelper.setEnchantments(map, itemstack2);

                return itemstack2;
            }
        }

        return ItemStack.EMPTY;
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
