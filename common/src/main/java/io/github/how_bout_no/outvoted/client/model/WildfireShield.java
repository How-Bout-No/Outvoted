package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class WildfireShield {
    public static final SpriteIdentifier base = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/wildfire_shield_base"));
    public static final SpriteIdentifier base_nop = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/wildfire_shield_base_nopattern"));
}