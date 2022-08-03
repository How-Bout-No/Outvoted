package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        UpgradeRecipeBuilder.smithing(Ingredient.of(Items.SHIELD), Ingredient.of(ModItems.WILDFIRE_SHIELD_PART.get()), ModItems.WILDFIRE_SHIELD.get())
                .unlocks("has_wildfire_part", has(ModItems.WILDFIRE_SHIELD_PART.get()))
                .save(consumer, ModItems.WILDFIRE_SHIELD.getId());
        ShapedRecipeBuilder.shaped(ModItems.WILDFIRE_SHIELD_PART.get())
                .pattern("MMM")
                .pattern("MCM")
                .pattern("MMM")
                .define('M', Blocks.POLISHED_BLACKSTONE)
                .define('C', ModItems.WILDFIRE_PIECE.get())
                .unlockedBy("has_wildfire_piece", has(ModItems.WILDFIRE_PIECE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.PRISMARINE_ROD.get())
                .pattern("P")
                .pattern("P")
                .pattern("P")
                .define('P', Blocks.PRISMARINE_BRICKS)
                .unlockedBy("has_prismarine", has(Blocks.PRISMARINE_BRICKS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(Items.TRIDENT)
                .pattern(" TT")
                .pattern(" HT")
                .pattern("R  ")
                .define('T', ModItems.BARNACLE_TOOTH.get())
                .define('H', Items.HEART_OF_THE_SEA)
                .define('R', ModItems.PRISMARINE_ROD.get())
                .unlockedBy("has_tooth", has(ModItems.BARNACLE_TOOTH.get()))
                .save(consumer);
        SpecialRecipeBuilder.special(ModRecipes.REPAIR_COST.get())
                .save(consumer, ModRecipes.REPAIR_COST.getId().getPath());
        SpecialRecipeBuilder.special(ModRecipes.SHIELD_DECO.get())
                .save(consumer, ModRecipes.SHIELD_DECO.getId().getPath());
        ShapelessRecipeBuilder.shapeless(ModBlocks.COPPER_BUTTON.get())
                .requires(Items.COPPER_INGOT)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(consumer);
    }
}
