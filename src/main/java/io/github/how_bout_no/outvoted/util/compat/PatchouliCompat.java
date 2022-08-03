package io.github.how_bout_no.outvoted.util.compat;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.config.Config;

public class PatchouliCompat {
    public static boolean loaded() {
        return true;
    }

    public static void updateFlag() {
        vazkii.patchouli.api.PatchouliAPI.get().setConfigFlag("outvoted:wildfirevariant", Config.wildfireVariants.get());
    }
}
