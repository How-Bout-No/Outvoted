package io.github.how_bout_no.outvoted.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface IOxidizable extends WeatheringCopper {
    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> {
        return (BiMap) ImmutableBiMap.builder().putAll(WeatheringCopper.NEXT_BY_BLOCK.get())
                .put(ModBlocks.COPPER_BUTTON.get(), ModBlocks.EXPOSED_COPPER_BUTTON.get())
                .put(ModBlocks.EXPOSED_COPPER_BUTTON.get(), ModBlocks.WEATHERED_COPPER_BUTTON.get())
                .put(ModBlocks.WEATHERED_COPPER_BUTTON.get(), ModBlocks.OXIDIZED_COPPER_BUTTON.get()).build();
    });
    Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> {
        return ((BiMap) NEXT_BY_BLOCK.get()).inverse();
    });

    static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable((Block) ((BiMap) PREVIOUS_BY_BLOCK.get()).get(block));
    }

    static Block getFirst(Block block) {
        Block block2 = block;

        for (Block block3 = (Block) ((BiMap) PREVIOUS_BY_BLOCK.get()).get(block); block3 != null; block3 = (Block) ((BiMap) PREVIOUS_BY_BLOCK.get()).get(block3)) {
            block2 = block3;
        }

        return block2;
    }

    static Optional<BlockState> getPrevious(BlockState state) {
        return getPrevious(state.getBlock()).map((block) -> {
            return ((BaseCopperButtonBlock) block).getStateWithPropertiesNoPower(state);
        });
    }

    static Optional<Block> getNext(Block block) {
        return Optional.ofNullable((Block) ((BiMap) NEXT_BY_BLOCK.get()).get(block));
    }

    static BlockState getFirst(BlockState state) {
        return ((BaseCopperButtonBlock) getFirst(state.getBlock())).getStateWithPropertiesNoPower(state);
    }

    default Optional<BlockState> getNext(BlockState state) {
        return getNext(state.getBlock()).map((block) -> {
            return ((BaseCopperButtonBlock) block).getStateWithPropertiesNoPower(state);
        });
    }

    default float getChanceModifier() {
        return this.getAge() == WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }
}
