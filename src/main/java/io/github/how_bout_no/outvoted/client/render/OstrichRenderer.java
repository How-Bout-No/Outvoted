package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.how_bout_no.outvoted.client.model.OstrichModel;
import io.github.how_bout_no.outvoted.entity.Ostrich;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class OstrichRenderer<E> extends GeoEntityRenderer<Ostrich> {
    public OstrichRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new OstrichModel());
        this.addLayer(new OstrichSaddleLayer(this));
    }

    @Override
    public RenderType getRenderType(Ostrich ostrich, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(ostrich));
    }

    @Override
    public void renderEarly(Ostrich ostrich, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(ostrich, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        if (ostrich.isBaby()) stackIn.scale(0.6F, 0.6F, 0.6F);
    }
}