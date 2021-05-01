package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.WildfireModel;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class WildfireRenderer extends GeoEntityRenderer<WildfireEntity> {
    public WildfireRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new WildfireModel());
    }

    private static final Identifier SOUL = new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire_soul.png");

    @Override
    public RenderLayer getRenderType(WildfireEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }

    @Override
    protected int getBlockLight(WildfireEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    @Override
    public Identifier getTexture(WildfireEntity entity) {
        if (entity.getVariant() == 0 || !Outvoted.clientConfig.wildfireVariants) {
            return super.getTexture(entity);
        }
        return SOUL;
    }
}