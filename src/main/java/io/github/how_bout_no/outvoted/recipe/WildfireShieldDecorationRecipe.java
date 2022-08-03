package io.github.how_bout_no.outvoted.recipe;

import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WildfireShieldDecorationRecipe extends CustomRecipe {
    public WildfireShieldDecorationRecipe(ResourceLocation identifier) {
        super(identifier);
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack craftingStack = craftingInventory.getItem(i);
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
        CompoundTag compoundTag = itemStack.getTagElement("BlockEntityTag");
        CompoundTag compoundTag2 = compoundTag == null ? new CompoundTag() : compoundTag.copy();
        compoundTag2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
        itemStack2.addTagElement("BlockEntityTag", compoundTag2);
        return itemStack2;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.SHIELD_DECO.get();
    }

    @Override
    public boolean matches(CraftingContainer craftingInventory, Level world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack itemStack3 = craftingInventory.getItem(i);
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

                    if (itemStack3.getTagElement("BlockEntityTag") != null) {
                        return false;
                    }

                    itemStack = itemStack3;
                }
            }
        }

        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }
}
