package io.github.how_bout_no.outvoted.block;

import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodType extends WoodType {
    public static final ModWoodType PALM = new ModWoodType("palm");

    public ModWoodType(String string) {
        super(string);
    }

    public static ModWoodType register(ModWoodType arg) {
        WoodType.register(arg);
        return arg;
    }
}
