package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.HungerModel;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
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

    private static final ResourceLocation SAND = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger.png");
    private static final ResourceLocation RED_SAND = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger_red.png");
    private static final ResourceLocation SWAMP = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger_swamp.png");

    @Override
    public RenderType getRenderType(HungerEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(this.getEntityTexture(animatable));
    }

    @Override
    public ResourceLocation getEntityTexture(HungerEntity entity) {
        if (entity.getVariant() == 1) {
            return RED_SAND;
        } else if (entity.getVariant() == 2) {
            return SWAMP;
        } else {
            return SAND;
        }
    }
}
