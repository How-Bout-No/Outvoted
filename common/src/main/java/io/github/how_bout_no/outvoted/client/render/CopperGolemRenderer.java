package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.CopperGolemModel;
import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CopperGolemRenderer extends GeoMobRenderer<CopperGolemEntity> {
    public CopperGolemRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CopperGolemModel());
    }

    private static final Identifier UNAFFECTED = new Identifier(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem.png");
    private static final Identifier EXPOSED = new Identifier(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem2.png");
    private static final Identifier WEATHERED = new Identifier(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem3.png");
    private static final Identifier OXIDIZED = new Identifier(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem4.png");

    @Override
    public RenderLayer getRenderType(CopperGolemEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTexture(animatable));
    }

    @Override
    public Identifier getTexture(CopperGolemEntity entity) {
        return switch (entity.getOxidizationLevel()) {
            case 0 -> UNAFFECTED;
            case 1 -> EXPOSED;
            case 2 -> WEATHERED;
            case 3 -> OXIDIZED;
            default -> throw new IllegalStateException("Unexpected value: " + entity.getOxidizationLevel());
        };
    }
}
