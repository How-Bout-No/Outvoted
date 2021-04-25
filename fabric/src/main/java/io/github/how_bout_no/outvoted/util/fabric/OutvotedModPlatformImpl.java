package io.github.how_bout_no.outvoted.util.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;

public class OutvotedModPlatformImpl {
    public static Item.Settings setISTER(Item.Settings properties) {
        return properties;
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    }
}
