package io.github.how_bout_no.outvoted.util.compat;

import io.github.how_bout_no.outvoted.config.OutvotedConfigCommon;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliCompat {
    public static void updateFlag() {
        PatchouliAPI.get().setConfigFlag("outvoted:wildfirevariant", OutvotedConfigCommon.Entities.Wildfire.isVariants());
    }
}
