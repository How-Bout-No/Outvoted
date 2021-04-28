package io.github.how_bout_no.outvoted.config;

import io.github.how_bout_no.outvoted.Outvoted;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Outvoted.MOD_ID)
public class OutvotedConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Gui.CollapsibleObject
    public OutvotedConfigClient client = new OutvotedConfigClient();

    @ConfigEntry.Gui.TransitiveObject
    public OutvotedConfigCommon common = new OutvotedConfigCommon();
}
