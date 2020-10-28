package com.hbn.outvoted.entities.hunger;

import com.hbn.outvoted.Outvoted;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class HungerRenderer extends MobRenderer<HungerEntity, HungerModel<HungerEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger.png");

    public HungerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HungerModel<>(), 0.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(HungerEntity entity) {
        return TEXTURE;
    }
}
