package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class WildfireShield {
    public static final Material base = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(Outvoted.MOD_ID, "entity/wildfire_shield_base"));
    public static final Material base_nop = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(Outvoted.MOD_ID, "entity/wildfire_shield_base_nopattern"));
}