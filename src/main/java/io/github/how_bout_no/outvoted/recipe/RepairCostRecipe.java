package io.github.how_bout_no.outvoted.recipe;

import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepairCostRecipe extends CustomRecipe {
    public RepairCostRecipe(ResourceLocation res) {
        super(res);
    }

    /**
     * Check matching items in crafting grid
     */
    public boolean matches(CraftingContainer inv, Level worldIn) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
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
    public ItemStack assemble(CraftingContainer inv) {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
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
                itemstack2.setDamageValue(item.getDamageValue());
                EnchantmentHelper.setEnchantments(map, itemstack2);

                return itemstack2;
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean hasEnchantibility(ItemStack itemStack) {
        return itemStack.isEnchantable() || itemStack.isEnchanted();
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.REPAIR_COST.get();
    }
}
