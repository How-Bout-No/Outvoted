package io.github.how_bout_no.outvoted.recipe;

import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class WildfireShieldDecorationRecipe extends SpecialCraftingRecipe {
    public WildfireShieldDecorationRecipe(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack craftingStack = craftingInventory.getStack(i);
            if (!craftingStack.isEmpty()) {
                if (craftingStack.getItem() instanceof BannerItem) {
                    itemStack = craftingStack;
                } else if (craftingStack.getItem() == ModItems.WILDFIRE_SHIELD.get()) {
                    itemStack2 = craftingStack.copy();
                }
            }
        }

        if (itemStack2.isEmpty()) {
            return itemStack2;
        }
        NbtCompound compoundTag = itemStack.getSubNbt("BlockEntityTag");
        NbtCompound compoundTag2 = compoundTag == null ? new NbtCompound() : compoundTag.copy();
        compoundTag2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
        itemStack2.setSubNbt("BlockEntityTag", compoundTag2);
        return itemStack2;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SHIELD_DECO.get();
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStack(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem) {
                    if (!itemStack2.isEmpty()) {
                        return false;
                    }

                    itemStack2 = itemStack3;
                } else {
                    if (itemStack3.getItem() != ModItems.WILDFIRE_SHIELD.get()) {
                        return false;
                    }

                    if (!itemStack.isEmpty()) {
                        return false;
                    }

                    if (itemStack3.getSubNbt("BlockEntityTag") != null) {
                        return false;
                    }

                    itemStack = itemStack3;
                }
            }
        }

        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }
}
