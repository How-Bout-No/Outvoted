package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.init.ModRecipes;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.minecraft.util.registry.Registry;

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
                .input('T', ModItems.KRAKEN_TOOTH.get())
                .input('H', Items.HEART_OF_THE_SEA)
                .input('R', ModItems.PRISMARINE_ROD.get())
                .criterion("has_tooth", conditionsFromItem(ModItems.KRAKEN_TOOTH.get()))
                .offerTo(consumer);
        ComplexRecipeJsonFactory.create(ModRecipes.REPAIR_COST.get())
                .offerTo(consumer, ModRecipes.REPAIR_COST.getId().getPath());

        planksFromLogs(consumer, ModBlocks.PALM_PLANKS.get(), ModTags.Items.PALM_LOGS);
        woodFromLogs(consumer, ModBlocks.PALM_WOOD.get(), ModBlocks.PALM_LOG.get());
        woodenStairs(consumer, ModBlocks.PALM_STAIRS.get(), ModBlocks.PALM_PLANKS.get());
        woodenSlab(consumer, ModBlocks.PALM_SLAB.get(), ModBlocks.PALM_PLANKS.get());
        woodenButton(consumer, ModBlocks.PALM_BUTTON.get(), ModBlocks.PALM_PLANKS.get());
        woodenPressurePlate(consumer, ModBlocks.PALM_PRESSURE_PLATE.get(), ModBlocks.PALM_PLANKS.get());
        woodenFence(consumer, ModBlocks.PALM_FENCE.get(), ModBlocks.PALM_PLANKS.get());
        woodenFenceGate(consumer, ModBlocks.PALM_FENCE_GATE.get(), ModBlocks.PALM_PLANKS.get());
        woodenTrapdoor(consumer, ModBlocks.PALM_TRAPDOOR.get(), ModBlocks.PALM_PLANKS.get());
        woodenDoor(consumer, ModBlocks.PALM_DOOR.get(), ModBlocks.PALM_PLANKS.get());
        woodenSign(consumer, ModBlocks.PALM_SIGN.get(), ModBlocks.PALM_PLANKS.get());

        planksFromLogs(consumer, ModBlocks.BAOBAB_PLANKS.get(), ModTags.Items.BAOBAB_LOGS);
        woodFromLogs(consumer, ModBlocks.BAOBAB_WOOD.get(), ModBlocks.BAOBAB_LOG.get());
        woodenStairs(consumer, ModBlocks.BAOBAB_STAIRS.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenSlab(consumer, ModBlocks.BAOBAB_SLAB.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenButton(consumer, ModBlocks.BAOBAB_BUTTON.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenPressurePlate(consumer, ModBlocks.BAOBAB_PRESSURE_PLATE.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenFence(consumer, ModBlocks.BAOBAB_FENCE.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenFenceGate(consumer, ModBlocks.BAOBAB_FENCE_GATE.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenTrapdoor(consumer, ModBlocks.BAOBAB_TRAPDOOR.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenDoor(consumer, ModBlocks.BAOBAB_DOOR.get(), ModBlocks.BAOBAB_PLANKS.get());
        woodenSign(consumer, ModBlocks.BAOBAB_SIGN.get(), ModBlocks.BAOBAB_PLANKS.get());
    }

    private static void planksFromLogs(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible planks, Tag<Item> input) {
        ShapelessRecipeJsonFactory.create(planks, 4).input(input).group("planks").criterion("has_logs", conditionsFromTag(input)).offerTo(recipeConsumer);
    }

    private static void woodFromLogs(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible stripped, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(stripped, 3).input('#', input).pattern("##").pattern("##").group("bark").criterion("has_log", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenButton(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible button, ItemConvertible input) {
        ShapelessRecipeJsonFactory.create(button).input(input).group("wooden_button").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenDoor(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible door, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(door, 3).input('#', input).pattern("##").pattern("##").pattern("##").group("wooden_door").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenFence(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible fence, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(fence, 3).input('#', Items.STICK).input('W', input).pattern("W#W").pattern("W#W").group("wooden_fence").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenFenceGate(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible fenceGate, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(fenceGate).input('#', Items.STICK).input('W', input).pattern("#W#").pattern("#W#").group("wooden_fence_gate").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenPressurePlate(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible pressurePlate, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(pressurePlate).input('#', input).pattern("##").group("wooden_pressure_plate").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenSlab(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible slab, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(slab, 6).input('#', input).pattern("###").group("wooden_slab").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenStairs(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible stairs, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(stairs, 4).input('#', input).pattern("#  ").pattern("## ").pattern("###").group("wooden_stairs").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenTrapdoor(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible trapdoor, ItemConvertible input) {
        ShapedRecipeJsonFactory.create(trapdoor, 2).input('#', input).pattern("###").pattern("###").group("wooden_trapdoor").criterion("has_planks", conditionsFromItem(input)).offerTo(recipeConsumer);
    }

    private static void woodenSign(Consumer<RecipeJsonProvider> recipeConsumer, ItemConvertible sign, ItemConvertible input) {
        String s = Registry.ITEM.getId(input.asItem()).getPath();
        ShapedRecipeJsonFactory.create(sign, 3).group("sign").input('#', input).input('X', Items.STICK).pattern("###").pattern("###").pattern(" X ").criterion("has_" + s, conditionsFromItem(input)).offerTo(recipeConsumer);
    }
}
