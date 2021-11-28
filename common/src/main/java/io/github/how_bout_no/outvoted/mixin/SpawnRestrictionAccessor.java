package io.github.how_bout_no.outvoted.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Stolen from Fabric API but this just helps make things neater, even if it's not required on Forge
 */
@Mixin(SpawnRestriction.class)
public interface SpawnRestrictionAccessor {
    @Invoker
    static <T extends MobEntity> void callRegister(EntityType<T> type, SpawnRestriction.Location location, Heightmap.Type heightmap, SpawnRestriction.SpawnPredicate<T> spawnPredicate) {
        throw new AssertionError();
    }
}
