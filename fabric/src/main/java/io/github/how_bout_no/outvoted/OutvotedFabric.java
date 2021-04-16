package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.entity.util.MobAttributes;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import io.github.how_bout_no.outvoted.init.ModFireBlock;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import net.fabricmc.api.ModInitializer;

public class OutvotedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Outvoted.init();
        MobAttributes.init();
        ModSpawnEggItem.initSpawnEggs();
        ModFeatures.Configured.registerConfiguredFeatures();
        ModFireBlock.init();
    }
}