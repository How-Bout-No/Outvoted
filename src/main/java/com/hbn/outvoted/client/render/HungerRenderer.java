package com.hbn.outvoted.client.render;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.model.HungerModel;
import com.hbn.outvoted.entity.HungerEntity;
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
public class HungerRenderer extends GeoEntityRenderer<HungerEntity> {
    public HungerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new HungerModel());
    }

    @Override
    public RenderType getRenderType(HungerEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(this.getEntityTexture(animatable));
    }

    @Override
    public ResourceLocation getEntityTexture(HungerEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger" + (entity.variant() == 0 ? "" : "_swamp") + ".png");
    }
}
