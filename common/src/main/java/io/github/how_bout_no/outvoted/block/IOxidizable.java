package io.github.how_bout_no.outvoted.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface IOxidizable extends Oxidizable {
    Supplier<BiMap<Block, Block>> OXIDATION_LEVEL_INCREASES = Suppliers.memoize(() -> {
        return (BiMap) ImmutableBiMap.builder().putAll(Oxidizable.OXIDATION_LEVEL_INCREASES.get())
                .put(ModBlocks.COPPER_BUTTON.get(), ModBlocks.EXPOSED_COPPER_BUTTON.get())
                .put(ModBlocks.EXPOSED_COPPER_BUTTON.get(), ModBlocks.WEATHERED_COPPER_BUTTON.get())
                .put(ModBlocks.WEATHERED_COPPER_BUTTON.get(), ModBlocks.OXIDIZED_COPPER_BUTTON.get()).build();
    });
    Supplier<BiMap<Block, Block>> OXIDATION_LEVEL_DECREASES = Suppliers.memoize(() -> {
        return ((BiMap) OXIDATION_LEVEL_INCREASES.get()).inverse();
    });

    static Optional<Block> getDecreasedOxidationBlock(Block block) {
        return Optional.ofNullable((Block) ((BiMap) OXIDATION_LEVEL_DECREASES.get()).get(block));
    }

    static Optional<BlockState> getDecreasedOxidationState(BlockState state) {
        return getDecreasedOxidationBlock(state.getBlock()).map((block) -> {
            return block.getStateWithProperties(state);
        });
    }

    static Optional<Block> getIncreasedOxidationBlock(Block block) {
        return Optional.ofNullable((Block)((BiMap)OXIDATION_LEVEL_INCREASES.get()).get(block));
    }
}
