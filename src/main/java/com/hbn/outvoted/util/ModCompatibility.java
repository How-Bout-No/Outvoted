package com.hbn.outvoted.util;

import net.minecraftforge.fml.ModList;

public class ModCompatibility {
    // TODO: Implement this
    public static boolean extendednether = false;
    public static boolean geckolib3 = false;

    public static void initCompat() {
        extendednether = isModLoaded("extendednether");
        geckolib3 = isModLoaded("geckolib3");
    }

    private static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
