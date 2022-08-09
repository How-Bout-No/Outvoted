package io.github.how_bout_no.outvoted.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public abstract class ModSignBlock {
    public static class ModStandingSignBlock extends StandingSignBlock implements IModdedSign {
        private final ResourceLocation texture;

        public ModStandingSignBlock(Properties properties, ResourceLocation texture) {
            super(properties, WoodType.OAK);
            this.texture = texture;
        }

        @Override
        public ResourceLocation getTexture() {
            return texture;
        }
    }

    public static class ModWallSignBlock extends WallSignBlock implements IModdedSign {
        private final ResourceLocation texture;

        public ModWallSignBlock(Properties properties, ResourceLocation texture) {
            super(properties, WoodType.OAK);
            this.texture = texture;
        }

        @Override
        public ResourceLocation getTexture() {
            return texture;
        }
    }
}

