package com.hbn.outvoted.entities.hunger;

import com.hbn.outvoted.Outvoted;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class HungerRenderer extends MobRenderer<HungerEntity, HungerModel<HungerEntity>> {

    protected static final ResourceLocation SANDTEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger.png");
    protected static final ResourceLocation GRASSTEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger_grass.png");

    public HungerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HungerModel<>(), 0.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(HungerEntity entity) {
        if (entity.variant() == 0) {
            return SANDTEXTURE;
        } else {
            return GRASSTEXTURE;
        }
    }
}
