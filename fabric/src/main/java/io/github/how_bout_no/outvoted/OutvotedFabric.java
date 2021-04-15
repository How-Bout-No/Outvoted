package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.entity.util.MobAttributes;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.world.gen.WorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.registry.BuiltinRegistries;

public class OutvotedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Outvoted.init();
        MobAttributes.init();
        WorldGen.addSpawnEntries();
        RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, id, biome) -> {
            WorldGen.addSpawnEntries();
        });
        ModSpawnEggItem.initSpawnEggs();
        ModFeatures.Configured.registerConfiguredFeatures();
    }
}
