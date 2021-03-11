package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import net.minecraft.block.Blocks;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        SmithingBuilder.smithingRecipe(Ingredient.of(Items.SHIELD), Ingredient.of(ModItems.WILDFIRE_SHIELD_PART.get()), ModItems.WILDFIRE_SHIELD.get())
                .unlocks("has_wildfire_part", has(ModItems.WILDFIRE_SHIELD_PART.get()))
                .save(consumer, ModItems.WILDFIRE_SHIELD.getId());
        ShapedBuilder.shapedRecipe(ModItems.WILDFIRE_SHIELD_PART.get())
                .pattern("MMM")
                .pattern("MCM")
                .pattern("MMM")
                .define('M', Blocks.POLISHED_BLACKSTONE)
                .define('C', ModItems.WILDFIRE_PIECE.get())
                .unlockedBy("has_wildfire_piece", has(ModItems.WILDFIRE_PIECE.get()))
                .save(consumer);
        ShapedBuilder.shapedRecipe(ModItems.PRISMARINE_ROD.get())
                .pattern("P")
                .pattern("P")
                .pattern("P")
                .define('P', Blocks.PRISMARINE_BRICKS)
                .unlockedBy("has_prismarine", has(Blocks.PRISMARINE_BRICKS))
                .save(consumer);
        ShapedBuilder.shapedRecipe(Items.TRIDENT)
                .pattern(" TT")
                .pattern(" HT")
                .pattern("R  ")
                .define('T', ModItems.KRAKEN_TOOTH.get())
                .define('H', Items.HEART_OF_THE_SEA)
                .define('R', ModItems.PRISMARINE_ROD.get())
                .unlockedBy("has_tooth", has(ModItems.KRAKEN_TOOTH.get()))
                .save(consumer);
        CustomRecipeBuilder.special(ModRecipes.REPAIR_COST.get()).save(consumer, ModRecipes.REPAIR_COST.getId().getPath());
    }
}
