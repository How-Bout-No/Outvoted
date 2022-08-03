package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.BarnacleModel;
import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class BarnacleRenderer extends GeoEntityRenderer<BarnacleEntity> {
    public BarnacleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BarnacleModel());
    }

    @Override
    public RenderType getRenderType(BarnacleEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }

    @Override
    public ResourceLocation getTextureLocation(BarnacleEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/barnacle.png");
    }
}