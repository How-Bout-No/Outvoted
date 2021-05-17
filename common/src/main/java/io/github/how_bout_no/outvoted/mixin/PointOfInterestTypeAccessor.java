package io.github.how_bout_no.outvoted.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeAccessor {

    @Invoker("<init>")
    static PointOfInterestType create(String id, Set<BlockState> blocks, int ticketCount, int searchDistance) {
        throw new AssertionError();
    }

    @Invoker
    static PointOfInterestType callSetup(PointOfInterestType poiType) {
        return poiType;
    }

}
