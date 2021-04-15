// Adapted from noobutil https://maven.blamejared.com/noobanidus/libs/noobutil/1.16.4-0.0.7.62/

package io.github.how_bout_no.outvoted.block;

import net.minecraft.block.WallSignBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

public class ModdedWallSignBlock extends WallSignBlock implements IModdedSign {
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
