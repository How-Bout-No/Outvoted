package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.GluttonModel;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class GluttonRenderer extends GeoEntityRenderer<GluttonEntity> {
    public GluttonRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GluttonModel());
    }

    private static final Identifier RED_SAND = new Identifier(Outvoted.MOD_ID, "textures/entity/glutton/glutton_red.png");
    private static final Identifier SWAMP = new Identifier(Outvoted.MOD_ID, "textures/entity/glutton/glutton_swamp.png");

    @Override
    public RenderLayer getRenderType(GluttonEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTexture(animatable));
    }

    @Override
    public Identifier getTexture(GluttonEntity entity) {
        switch (entity.getVariant()) {
            case 1:
                return RED_SAND;
            case 2:
                return SWAMP;
            default:
                return super.getTexture(entity);
        }
    }
}
