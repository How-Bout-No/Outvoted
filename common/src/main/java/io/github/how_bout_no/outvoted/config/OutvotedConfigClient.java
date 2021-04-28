package io.github.how_bout_no.outvoted.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "client")
public class OutvotedConfigClient implements ConfigData {
    @ConfigEntry.Gui.RequiresRestart
    public boolean creativeTab = true;

    @ConfigEntry.Gui.RequiresRestart
    public boolean wildfireVariants = true;
}
