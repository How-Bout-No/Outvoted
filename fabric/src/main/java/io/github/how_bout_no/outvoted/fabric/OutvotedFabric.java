package io.github.how_bout_no.outvoted.fabric;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.world.SpawnUtil;
import net.fabricmc.api.ModInitializer;

public class OutvotedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Outvoted.init();
        ModSpawnEggItem.initSpawnEggs();
        SpawnUtil.registerRestrictions();
    }
}