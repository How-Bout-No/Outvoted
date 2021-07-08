package io.github.how_bout_no.outvoted.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.recipe.RepairCostRecipe;
import io.github.how_bout_no.outvoted.recipe.WildfireShieldDecorationRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(Outvoted.MOD_ID, Registry.RECIPE_SERIALIZER_KEY);

    public static final RegistrySupplier<SpecialRecipeSerializer<RepairCostRecipe>> REPAIR_COST = RECIPES.register("repair_cost", () -> new SpecialRecipeSerializer<>(RepairCostRecipe::new));
    public static final RegistrySupplier<SpecialRecipeSerializer<WildfireShieldDecorationRecipe>> SHIELD_DECO = RECIPES.register("wildfire_shield_deco", () -> new SpecialRecipeSerializer<>(WildfireShieldDecorationRecipe::new));
}
