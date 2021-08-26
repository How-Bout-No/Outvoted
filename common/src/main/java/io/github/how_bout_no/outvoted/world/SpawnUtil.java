package io.github.how_bout_no.outvoted.world;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.GravityField;
import net.minecraft.world.SpawnHelper;

public class SpawnUtil {
    public static boolean isBelowCap(SpawnGroup group, ServerWorld world, int multiplier) {
        ThreadedAnvilChunkStorage chunkStorage = world.getChunkManager().threadedAnvilChunkStorage;
        SpawnHelper.Info info = new SpawnHelper.Info(chunkStorage.getTicketManager().getSpawningChunkCount(), new Object2IntOpenHashMap<>(), new GravityField());
        int i = group.getCapacity() * info.getSpawningChunkCount() / SpawnHelper.CHUNK_AREA;
        return info.getGroupToCount().getInt(group) < i * Math.max(multiplier, 1);
    }
}
