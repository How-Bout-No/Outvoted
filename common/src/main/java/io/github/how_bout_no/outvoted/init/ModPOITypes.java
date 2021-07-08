package io.github.how_bout_no.outvoted.init;

import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.mixin.PointOfInterestTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Set;

public class ModPOITypes {
//    public static final PointOfInterestType BURROW = registerPoi("burrow", statesOf(ModBlocks.BURROW.get()));
//
//    private static Set<BlockState> statesOf(Block block) {
//        return ImmutableSet.copyOf(block.getStateManager().getStates());
//    }
//
//    private static PointOfInterestType registerPoi(String name, Set<BlockState> blocks) {
//        PointOfInterestType type = PointOfInterestTypeAccessor.create(Outvoted.MOD_ID + ":" + name, blocks, 0, 0);
//        Registry.register(Registry.POINT_OF_INTEREST_TYPE, new Identifier(Outvoted.MOD_ID, name), type);
//        return PointOfInterestTypeAccessor.callSetup(type);
//    }

    public static void init() {
        // load class
    }

    public static final DeferredRegister<PointOfInterestType> POINT_OF_INTEREST_TYPES = DeferredRegister.create(Outvoted.MOD_ID, Registry.POINT_OF_INTEREST_TYPE_KEY);

    public static final RegistrySupplier<PointOfInterestType> BURROW = registerPoi("burrow_poi", statesOf(ModBlocks.BURROW.get()));

    private static Set<BlockState> statesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }

    private static RegistrySupplier<PointOfInterestType> registerPoi(String name, Set<BlockState> blocks) {
        PointOfInterestType type = PointOfInterestTypeAccessor.create(Outvoted.MOD_ID + ":" + name, blocks, 0, 1);
        return POINT_OF_INTEREST_TYPES.register(name, () -> PointOfInterestTypeAccessor.callSetup(type));
    }
}
