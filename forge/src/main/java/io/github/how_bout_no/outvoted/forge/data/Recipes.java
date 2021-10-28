package io.github.how_bout_no.outvoted.forge.data;

import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.util.function.Consumer;

public class Recipes extends RecipesProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void generate(Consumer<RecipeJsonProvider> consumer) {
        SmithingRecipeJsonFactory.create(Ingredient.ofItems(Items.SHIELD), Ingredient.ofItems(ModItems.WILDFIRE_SHIELD_PART.get()), ModItems.WILDFIRE_SHIELD.get())
                .criterion("has_wildfire_part", conditionsFromItem(ModItems.WILDFIRE_SHIELD_PART.get()))
                .offerTo(consumer, ModItems.WILDFIRE_SHIELD.getId());
        ShapedRecipeJsonFactory.create(ModItems.WILDFIRE_SHIELD_PART.get())
                .pattern("MMM")
                .pattern("MCM")
                .pattern("MMM")
                .input('M', Blocks.POLISHED_BLACKSTONE)
                .input('C', ModItems.WILDFIRE_PIECE.get())
                .criterion("has_wildfire_piece", conditionsFromItem(ModItems.WILDFIRE_PIECE.get()))
                .offerTo(consumer);
        ShapedRecipeJsonFactory.create(ModItems.PRISMARINE_ROD.get())
                .pattern("P")
                .pattern("P")
                .pattern("P")
                .input('P', Blocks.PRISMARINE_BRICKS)
                .criterion("has_prismarine", conditionsFromItem(Blocks.PRISMARINE_BRICKS))
                .offerTo(consumer);
        ShapedRecipeJsonFactory.create(Items.TRIDENT)
                .pattern(" TT")
                .pattern(" HT")
                .pattern("R  ")
                .input('T', ModItems.BARNACLE_TOOTH.get())
                .input('H', Items.HEART_OF_THE_SEA)
                .input('R', ModItems.PRISMARINE_ROD.get())
                .criterion("has_tooth", conditionsFromItem(ModItems.BARNACLE_TOOTH.get()))
                .offerTo(consumer);
        ComplexRecipeJsonFactory.create(ModRecipes.REPAIR_COST.get())
                .offerTo(consumer, ModRecipes.REPAIR_COST.getId().getPath());
        ComplexRecipeJsonFactory.create(ModRecipes.SHIELD_DECO.get())
                .offerTo(consumer, ModRecipes.SHIELD_DECO.getId().getPath());
        ShapelessRecipeJsonFactory.create(ModBlocks.COPPER_BUTTON.get())
                .input(Blocks.COPPER_BLOCK)
                .criterion("has_copper", conditionsFromItem(Blocks.COPPER_BLOCK))
                .offerTo(consumer);
        ShapelessRecipeJsonFactory.create(ModBlocks.EXPOSED_COPPER_BUTTON.get())
                .input(Blocks.EXPOSED_COPPER)
                .criterion("has_ecopper", conditionsFromItem(Blocks.EXPOSED_COPPER))
                .offerTo(consumer);
        ShapelessRecipeJsonFactory.create(ModBlocks.WEATHERED_COPPER_BUTTON.get())
                .input(Blocks.WEATHERED_COPPER)
                .criterion("has_wcopper", conditionsFromItem(Blocks.WEATHERED_COPPER))
                .offerTo(consumer);
        ShapelessRecipeJsonFactory.create(ModBlocks.OXIDIZED_COPPER_BUTTON.get())
                .input(Blocks.OXIDIZED_COPPER)
                .criterion("has_ocopper", conditionsFromItem(Blocks.OXIDIZED_COPPER))
                .offerTo(consumer);
    }
}
