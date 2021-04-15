package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

public class ModdedSignBlock {
    public static class ModdedStandingSignBlock extends SignBlock implements IModdedSign {
        private final Identifier texture;

        public ModdedStandingSignBlock(Settings properties, Identifier texture) {
            super(properties, SignType.OAK);
            this.texture = texture;
        }

        @Override
        public Identifier getTexture() {
            return texture;
        }
    }

    public static class ModdedWallSignBlock extends WallSignBlock implements IModdedSign {
        private final Identifier texture;

        public ModdedWallSignBlock(Settings properties, Identifier texture) {
            super(properties, SignType.OAK);
            this.texture = texture;
        }

        @Override
        public Identifier getTexture() {
            return texture;
        }
    }
}
