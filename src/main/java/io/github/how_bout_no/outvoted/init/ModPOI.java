package io.github.how_bout_no.outvoted.init;

import com.google.common.collect.ImmutableSet;
import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModPOI {
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Outvoted.MOD_ID);

    public static final RegistryObject<PoiType> BURROW = POI.register("burrow_poi",
            () -> PoiType.register(Outvoted.MOD_ID + ":" + "burrow_poi", statesOf(ModBlocks.BURROW.get()), 0, 1));

    private static Set<BlockState> statesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}
