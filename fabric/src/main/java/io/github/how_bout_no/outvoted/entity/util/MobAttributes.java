package io.github.how_bout_no.outvoted.entity.util;

import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class MobAttributes {
    public static void init() {
        FabricDefaultAttributeRegistry.register(ModEntityTypes.WILDFIRE.get(), WildfireEntity.setCustomAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.HUNGER.get(), HungerEntity.setCustomAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.KRAKEN.get(), KrakenEntity.setCustomAttributes());
//        FabricDefaultAttributeRegistry.register(ModEntityTypes.MEERKAT.get(), MeerkatEntity.setCustomAttributes());
    }
}
