package io.github.how_bout_no.outvoted.init;

import com.google.common.collect.ImmutableSet;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.mixin.PointOfInterestTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Set;

public class ModPOITypes {
    public static final PointOfInterestType BURROW = registerPoi("burrow", statesOf(ModBlocks.BURROW.get()));

    private static Set<BlockState> statesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }

    private static PointOfInterestType registerPoi(String name, Set<BlockState> blocks) {
        PointOfInterestType type = PointOfInterestTypeAccessor.create(Outvoted.MOD_ID + ":" + name, blocks, 0, 0);
        Registry.register(Registry.POINT_OF_INTEREST_TYPE, new Identifier(Outvoted.MOD_ID, name), type);
        return PointOfInterestTypeAccessor.callSetup(type);
    }

    public static void init() {
        // load class
    }
}
