package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.Ostrich;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class OstrichSaddleLayer extends GeoLayerRenderer<Ostrich> {
    private static final ResourceLocation SADDLE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/ostrich/ostrich_saddle.png");

    public OstrichSaddleLayer(IGeoRenderer<Ostrich> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Ostrich entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isSaddled()) {
            matrixStackIn.pushPose();
            matrixStackIn.scale(1.01f, 1.01f, 1.01f);
            renderCopyModel((GeoModelProvider<Ostrich>) this.getEntityModel(), SADDLE, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, partialTicks, 1f, 1f, 1f);
            matrixStackIn.popPose();
        }
    }
}
