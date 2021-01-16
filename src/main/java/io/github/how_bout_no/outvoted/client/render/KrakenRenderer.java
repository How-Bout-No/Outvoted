package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.KrakenModel;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
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
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/kraken.png");
    }
}