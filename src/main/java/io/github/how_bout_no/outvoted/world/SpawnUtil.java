package io.github.how_bout_no.outvoted.world;

import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import io.github.how_bout_no.outvoted.entity.GlareEntity;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.mixin.SpawnPlacementsInvoker;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnUtil {
    public static void registerRestrictions() {
        SpawnPlacementsInvoker.callRegister(ModEntityTypes.WILDFIRE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
        SpawnPlacementsInvoker.callRegister(ModEntityTypes.GLUTTON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GluttonEntity::canSpawn);
        SpawnPlacementsInvoker.callRegister(ModEntityTypes.BARNACLE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, BarnacleEntity::canSpawn);
        SpawnPlacementsInvoker.callRegister(ModEntityTypes.GLARE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GlareEntity::canSpawn);
        SpawnPlacementsInvoker.callRegister(ModEntityTypes.COPPER_GOLEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, CopperGolemEntity::canSpawn);
    }
}
