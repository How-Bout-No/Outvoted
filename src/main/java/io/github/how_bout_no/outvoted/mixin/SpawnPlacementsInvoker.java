package io.github.how_bout_no.outvoted.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Stolen from Fabric API but this just helps make things neater, even if it's not required on Forge
 */
@Mixin(SpawnPlacements.class)
public interface SpawnPlacementsInvoker {
    @Invoker
    static <T extends Mob> void callRegister(EntityType<T> type, SpawnPlacements.Type location, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        throw new AssertionError();
    }
}
