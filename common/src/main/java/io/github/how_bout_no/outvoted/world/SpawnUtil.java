package io.github.how_bout_no.outvoted.world;

import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import io.github.how_bout_no.outvoted.entity.GlareEntity;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.mixin.SpawnRestrictionAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.GravityField;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;

public class SpawnUtil {
    public static boolean isBelowCap(SpawnGroup group, ServerWorld world, int multiplier) {
        ThreadedAnvilChunkStorage chunkStorage = world.getChunkManager().threadedAnvilChunkStorage;
        int scc = chunkStorage.getTicketManager().getSpawningChunkCount();
        SpawnHelper.Info info = new SpawnHelper.Info(scc, new Object2IntOpenHashMap<>(), new GravityField());
        int i = group.getCapacity() * scc / SpawnHelper.CHUNK_AREA;
        return info.getGroupToCount().getInt(group) < i * Math.max(multiplier, 1);
    }

    public static void registerRestrictions() {
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.WILDFIRE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.GLUTTON.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GluttonEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.BARNACLE.get(), SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING, BarnacleEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.GLARE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GlareEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(ModEntityTypes.COPPER_GOLEM.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, CopperGolemEntity::canSpawn);
    }
}
