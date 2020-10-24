package com.hbn.outvoted.client.render;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.model.InfernoModel;
import com.hbn.outvoted.entities.InfernoEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class InfernoRenderer extends MobRenderer<InfernoEntity, InfernoModel<InfernoEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/inferno.png");

    public InfernoRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new InfernoModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(InfernoEntity entity) {
        return TEXTURE;
    }
}
