package io.github.how_bout_no.outvoted.init;

import net.minecraft.world.food.FoodProperties;

public final class ModFoods {
    private ModFoods() {}

    public static final FoodProperties BAOBAB = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.6F).build();
}
