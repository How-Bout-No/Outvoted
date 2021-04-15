package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.client.model.KrakenModel;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
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
public class KrakenRenderer extends GeoEntityRenderer<KrakenEntity> {
    public KrakenRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new KrakenModel());
    }

    @Override
    public RenderLayer getRenderType(KrakenEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }

    @Override
    public Identifier getTexture(KrakenEntity entity) {
        return super.getTextureLocation(entity);
    }
}