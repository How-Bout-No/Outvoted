package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.GlareModel;
import io.github.how_bout_no.outvoted.entity.GlareEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GlareRenderer extends GeoMobRenderer<GlareEntity> {
    public GlareRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GlareModel());
    }

    private static final Identifier DEFAULT = new Identifier(Outvoted.MOD_ID, "textures/entity/glare/glare.png");
    private static final Identifier ANGRY = new Identifier(Outvoted.MOD_ID, "textures/entity/glare/glare_angry.png");

    @Override
    public RenderLayer getRenderType(GlareEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTexture(animatable));
    }

    @Override
    public Identifier getTexture(GlareEntity entity) {
        return entity.isAngry() ? ANGRY : DEFAULT;
    }
}