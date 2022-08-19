package io.github.how_bout_no.outvoted.block;

import io.github.how_bout_no.outvoted.block.entity.ModSignBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlock {
    public static class ModStandingSignBlock extends StandingSignBlock {
        public ModStandingSignBlock(Properties properties, ModWoodType woodType) {
            super(properties, woodType);
        }

        @Override
        public String getDescriptionId() {
            return Util.makeDescriptionId("item", this.getRegistryName());
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new ModSignBlockEntity(pos, state);
        }
    }

    public static class ModWallSignBlock extends WallSignBlock {
        public ModWallSignBlock(Properties properties, ModWoodType woodType) {
            super(properties, woodType);
        }

        @Override
        public String getDescriptionId() {
            return Util.makeDescriptionId("item", this.getRegistryName());
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new ModSignBlockEntity(pos, state);
        }
    }
}

