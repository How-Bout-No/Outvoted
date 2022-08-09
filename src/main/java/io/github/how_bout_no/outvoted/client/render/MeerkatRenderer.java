package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.MeerkatModel;
import io.github.how_bout_no.outvoted.entity.Meerkat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MeerkatRenderer extends GeoMobRenderer<Meerkat> {
    public MeerkatRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MeerkatModel());
    }

    @Override
    public RenderType getRenderType(Meerkat animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }

    @Override
    public ResourceLocation getTextureLocation(Meerkat entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/meerkat.png");
    }

    @Override
    public void renderEarly(Meerkat animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        if (animatable.isBaby()) stackIn.scale(MeerkatModel.babyScale, MeerkatModel.babyScale, MeerkatModel.babyScale);
        else stackIn.scale(MeerkatModel.scale, MeerkatModel.scale, MeerkatModel.scale);
    }
}