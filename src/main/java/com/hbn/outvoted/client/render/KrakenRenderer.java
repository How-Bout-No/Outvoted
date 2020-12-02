package com.hbn.outvoted.client.render;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.model.KrakenModel;
import com.hbn.outvoted.entities.KrakenEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class KrakenRenderer extends GeoEntityRenderer<KrakenEntity> {
    public KrakenRenderer(EntityRendererManager renderManager) {
        super(renderManager, new KrakenModel());
    }

    @Override
    public RenderType getRenderType(KrakenEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(this.getEntityTexture(animatable));
    }

    @Override
    public ResourceLocation getEntityTexture(KrakenEntity entity) {
        /*if (OutvotedConfig.COMMON.krakenvariant.get()) {
            if (entity.variant() == 1) {
                return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/kraken_warm.png");
            } else if (entity.variant() == 2) {
                return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/kraken_cold.png");
            }
        }*/
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/kraken.png");
    }
}