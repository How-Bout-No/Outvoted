package io.github.how_bout_no.outvoted.world;

import io.github.how_bout_no.outvoted.entity.*;
import io.github.how_bout_no.outvoted.init.ModEntities;
import io.github.how_bout_no.outvoted.mixin.SpawnPlacementsInvoker;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnUtil {
    public static void registerRestrictions() {
        SpawnPlacementsInvoker.callRegister(ModEntities.WILDFIRE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
        SpawnPlacementsInvoker.callRegister(ModEntities.GLUTTON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Glutton::canSpawn);
        SpawnPlacementsInvoker.callRegister(ModEntities.BARNACLE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, Barnacle::canSpawn);
        SpawnPlacementsInvoker.callRegister(ModEntities.GLARE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Glare::canSpawn);
        SpawnPlacementsInvoker.callRegister(ModEntities.COPPER_GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, CopperGolem::canSpawn);
        SpawnPlacementsInvoker.callRegister(ModEntities.OSTRICH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Ostrich::canSpawn);
    }
}
