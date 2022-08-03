package io.github.how_bout_no.outvoted.mixin;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PoiType.class)
public interface PointOfInterestTypeAccessor {
    @Invoker("<init>")
    static PoiType create(String id, Set<BlockState> blocks, int ticketCount, int searchDistance) {
        throw new AssertionError();
    }
}
