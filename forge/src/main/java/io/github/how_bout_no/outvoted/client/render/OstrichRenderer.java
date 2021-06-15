package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.client.model.OstrichModel;
import io.github.how_bout_no.outvoted.entity.OstrichEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class OstrichRenderer<E> extends GeoEntityRenderer<OstrichEntity> {
    public OstrichRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new OstrichModel());
    }

    @Override
    public RenderLayer getRenderType(OstrichEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTexture(animatable));
    }

    @Override
    public Identifier getTexture(OstrichEntity entity) {
        return super.getTextureLocation(entity);
    }

    @Override
    public void renderEarly(OstrichEntity animatable, MatrixStack stackIn, float ticks,
                            VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                            int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        if (animatable.isBaby()) stackIn.scale(0.6F, 0.6F, 0.6F);
    }
}