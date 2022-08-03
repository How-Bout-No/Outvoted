package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.recipe.RepairCostRecipe;
import io.github.how_bout_no.outvoted.recipe.WildfireShieldDecorationRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Outvoted.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<RepairCostRecipe>> REPAIR_COST = RECIPES.register("repair_cost", () -> new SimpleRecipeSerializer<>(RepairCostRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<WildfireShieldDecorationRecipe>> SHIELD_DECO = RECIPES.register("wildfire_shield_deco", () -> new SimpleRecipeSerializer<>(WildfireShieldDecorationRecipe::new));
}
