package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.WildfireModel;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class WildfireRenderer extends GeoEntityRenderer<WildfireEntity> {
    public WildfireRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WildfireModel());
    }

    private static final ResourceLocation DEFAULT = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png");
    private static final ResourceLocation SOUL = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire_soul.png");

    @Override
    public RenderType getRenderType(WildfireEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }

    @Override
    protected int getBlockLightLevel(WildfireEntity arg, BlockPos arg2) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(WildfireEntity entity) {
        if (entity.getVariant() == 0 || !Config.wildfireVariants.get()) {
            return DEFAULT;
        }
        return SOUL;
    }
}