package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpawnRestriction.class)
public abstract class MixinSpawnRestriction {

    @Shadow
    public static <T extends MobEntity> void register(EntityType<T> type, SpawnRestriction.Location location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate) {
    }

    static {
        register(ModEntityTypes.WILDFIRE.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        register(ModEntityTypes.HUNGER.get(), SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HungerEntity::canSpawn);
        register(ModEntityTypes.KRAKEN.get(), SpawnRestriction.Location.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING, KrakenEntity::canSpawn);
    }
}
