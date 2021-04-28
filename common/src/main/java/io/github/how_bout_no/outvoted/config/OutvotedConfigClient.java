package io.github.how_bout_no.outvoted.config;

import io.github.how_bout_no.completeconfig.api.ConfigEntry;
import io.github.how_bout_no.completeconfig.api.ConfigGroup;
import lombok.Getter;

public class OutvotedConfigClient implements ConfigGroup {
    @Getter
    @ConfigEntry(requiresRestart = true)
    private static boolean creativeTab = true;

    @Override
    public String getID() {
        return "client";
    }
}
