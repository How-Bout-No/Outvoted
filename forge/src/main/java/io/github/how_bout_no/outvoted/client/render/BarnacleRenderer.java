package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.client.model.BarnacleModel;
import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
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
public class BarnacleRenderer extends GeoEntityRenderer<BarnacleEntity> {
    public BarnacleRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new BarnacleModel());
    }

    @Override
    public RenderLayer getRenderType(BarnacleEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }

    @Override
    public Identifier getTexture(BarnacleEntity entity) {
        return super.getTextureLocation(entity);
    }
}