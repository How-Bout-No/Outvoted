package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.InfernoModel;
import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.entity.InfernoEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class InfernoRenderer extends GeoEntityRenderer<InfernoEntity> {
    public InfernoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new InfernoModel());
    }

    private static final ResourceLocation NETHER = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/inferno/inferno.png");
    private static final ResourceLocation SOUL = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/inferno/inferno_soul.png");

    @Override
    public RenderType getRenderType(InfernoEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(this.getEntityTexture(animatable));
    }

    @Override
    protected int getBlockLight(InfernoEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    @Override
    public ResourceLocation getEntityTexture(InfernoEntity entity) {
        if (entity.getVariant() == 0 || !OutvotedConfig.COMMON.infernovariant.get()) {
            return NETHER;
        } else {
            return SOUL;
        }
    }
}