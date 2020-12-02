package com.hbn.outvoted.client.render;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.model.InfernoModel;
import com.hbn.outvoted.entities.InfernoEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class InfernoRenderer extends GeoEntityRenderer<InfernoEntity> {
    public InfernoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new InfernoModel());
    }

    @Override
    public RenderType getRenderType(InfernoEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(this.getEntityTexture(animatable));
    }

    protected int getBlockLight(InfernoEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    @Override
    public ResourceLocation getEntityTexture(InfernoEntity entity) {
        /*if (OutvotedConfig.COMMON.infernovariant.get()) {
            return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/inferno" + (entity.variant() == 0 ? "" : "_soul") + ".png");
        }*/
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/inferno.png");
    }
}