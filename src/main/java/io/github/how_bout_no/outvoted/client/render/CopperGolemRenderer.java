package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.CopperGolemModel;
import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;

@OnlyIn(Dist.CLIENT)
public class CopperGolemRenderer extends GeoMobRenderer<CopperGolemEntity> {
    public CopperGolemRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CopperGolemModel());
    }

    private static final ResourceLocation UNAFFECTED = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem.png");
    private static final ResourceLocation EXPOSED = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem2.png");
    private static final ResourceLocation WEATHERED = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem3.png");
    private static final ResourceLocation OXIDIZED = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem4.png");
    @Override
    public RenderType getRenderType(CopperGolemEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
    @Override
    public ResourceLocation getTextureLocation(CopperGolemEntity entity) {
        return switch (entity.getOxidizationLevel()) {
            case 0 -> UNAFFECTED;
            case 1 -> EXPOSED;
            case 2 -> WEATHERED;
            case 3 -> OXIDIZED;
            default -> throw new IllegalStateException("Unexpected value: " + entity.getOxidizationLevel());
        };
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("righthand")) {
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            stack.mulPose(Vector3f.YP.rotationDegrees(0));
            stack.mulPose(Vector3f.ZP.rotationDegrees(0));
            stack.translate(0.48D, 0.0D, 0.0D);
            stack.scale(1.0f, 1.0f, 1.0f);
            Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }else if (bone.getName().equals("lefthand")) {
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(0));
            stack.mulPose(Vector3f.YP.rotationDegrees(0));
            stack.mulPose(Vector3f.ZP.rotationDegrees(0));
            stack.translate(-0.48D, -0.225D, -0.275D);
            stack.scale(1.0f, 1.0f, 1.0f);
            Minecraft.getInstance().getItemRenderer().renderStatic(offHand, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
