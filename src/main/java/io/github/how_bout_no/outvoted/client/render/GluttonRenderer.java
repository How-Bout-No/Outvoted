package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.GluttonModel;
import io.github.how_bout_no.outvoted.entity.Glutton;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GluttonRenderer extends GeoMobRenderer<Glutton> {
    public GluttonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new GluttonModel());
    }

    private static final ResourceLocation SAND = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/glutton/glutton.png");
    private static final ResourceLocation RED_SAND = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/glutton/glutton_red.png");
    private static final ResourceLocation SWAMP = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/glutton/glutton_swamp.png");

    @Override
    public RenderType getRenderType(Glutton glutton, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(glutton));
    }

    @Override
    public ResourceLocation getTextureLocation(Glutton entity) {
        return switch (entity.getVariant()) {
            case 1 -> RED_SAND;
            case 2 -> SWAMP;
            default -> SAND;
        };
    }
}
