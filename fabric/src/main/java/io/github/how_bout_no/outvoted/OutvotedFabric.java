package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import io.github.how_bout_no.outvoted.entity.GlareEntity;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
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
        spawns();
    }

    public void spawns() {
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.WILDFIRE.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.GLUTTON.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GluttonEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.BARNACLE.get(), SpawnRestriction.Location.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING, BarnacleEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.GLARE.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING, GlareEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.COPPER_GOLEM.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING, CopperGolemEntity::canSpawn);
    }
}