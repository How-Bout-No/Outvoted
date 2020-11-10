package com.hbn.outvoted.entities.inferno;

import com.hbn.outvoted.Outvoted;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfernoRenderer extends MobRenderer<InfernoEntity, InfernoModel<InfernoEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/inferno.png");

    protected int getBlockLight(InfernoEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public InfernoRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new InfernoModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(InfernoEntity entity) {
        return TEXTURE;
    }
}
