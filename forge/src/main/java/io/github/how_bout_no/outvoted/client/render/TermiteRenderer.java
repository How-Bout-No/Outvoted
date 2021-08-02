package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.client.model.TermiteModel;
import io.github.how_bout_no.outvoted.entity.TermiteEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TermiteRenderer extends GeoMobRenderer<TermiteEntity> {
    public TermiteRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new TermiteModel());
    }

    @Override
    public RenderLayer getRenderType(TermiteEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTexture(animatable));
    }

    @Override
    public void renderEarly(TermiteEntity animatable, MatrixStack stackIn, float ticks,
                            VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                            int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(TermiteModel.scale, TermiteModel.scale, TermiteModel.scale);
    }
}