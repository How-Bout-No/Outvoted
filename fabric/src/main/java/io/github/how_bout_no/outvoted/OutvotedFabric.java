package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.entity.OstrichEntity;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.Heightmap;

public class OutvotedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Outvoted.init();
        ModSpawnEggItem.initSpawnEggs();
        ModFeatures.Configured.registerConfiguredFeatures();
        ModFireBlock.init();
        ModSigns.init();
        ModPOITypes.POINT_OF_INTEREST_TYPES.register();
        spawns();
    }

    public void spawns() {
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.WILDFIRE.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.GLUTTON.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GluttonEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.BARNACLE.get(), SpawnRestriction.Location.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING, BarnacleEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.MEERKAT.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MeerkatEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.OSTRICH.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, OstrichEntity::canSpawn);
    }
}