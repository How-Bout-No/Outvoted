package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GeoMobRenderer<T extends Mob & IAnimatable> extends GeoEntityRenderer<T> {
    protected GeoMobRenderer(EntityRendererProvider.Context ctx, AnimatedGeoModel<T> modelProvider) {
        super(ctx, modelProvider);
    }

    public void render(T mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        Entity entity = mobEntity.getLeashHolder();
        if (entity != null) {
            this.renderLeash(mobEntity, g, matrixStack, vertexConsumerProvider, entity);
        }
    }

    private <E extends Entity> void renderLeash(T entity, float tickDelta, PoseStack matrices, MultiBufferSource provider, E holdingEntity) {
        matrices.pushPose();
        Vec3 vec3d = holdingEntity.getRopeHoldPosition(tickDelta);
        double d = (double) (Mth.lerp(tickDelta, entity.yBodyRot, entity.yBodyRotO) * 0.017453292F) + 1.5707963267948966D;
        Vec3 vec3d2 = entity.getLeashOffset();
        double e = Math.cos(d) * vec3d2.z + Math.sin(d) * vec3d2.x;
        double f = Math.sin(d) * vec3d2.z - Math.cos(d) * vec3d2.x;
        double g = Mth.lerp((double) tickDelta, entity.xo, entity.getX()) + e;
        double h = Mth.lerp((double) tickDelta, entity.yo, entity.getY()) + vec3d2.y;
        double i = Mth.lerp((double) tickDelta, entity.zo, entity.getZ()) + f;
        matrices.translate(e, vec3d2.y, f);
        float j = (float) (vec3d.x - g);
        float k = (float) (vec3d.y - h);
        float l = (float) (vec3d.z - i);
        float m = 0.025F;
        VertexConsumer vertexConsumer = provider.getBuffer(RenderType.leash());
        Matrix4f matrix4f = matrices.last().pose();
        float n = Mth.fastInvSqrt(j * j + l * l) * 0.025F / 2.0F;
        float o = l * n;
        float p = j * n;
        BlockPos blockPos = new BlockPos(entity.getEyePosition(tickDelta));
        BlockPos blockPos2 = new BlockPos(holdingEntity.getEyePosition(tickDelta));
        int q = this.getBlockLightLevel(entity, blockPos);
        int r = holdingEntity.isOnFire() ? 15 : holdingEntity.level.getBrightness(LightLayer.BLOCK, blockPos2);
        ;
        int s = entity.level.getBrightness(LightLayer.SKY, blockPos);
        int t = entity.level.getBrightness(LightLayer.SKY, blockPos2);

        int v;
        for (v = 0; v <= 24; ++v) {
            renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025F, 0.025F, o, p, v, false);
        }

        for (v = 24; v >= 0; --v) {
            renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025F, 0.0F, o, p, v, true);
        }

        matrices.popPose();
    }

    private static void renderLeashPiece(VertexConsumer vertexConsumer, Matrix4f modelMatrix, float f, float g, float h, int leashedEntityBlockLight, int holdingEntityBlockLight, int leashedEntitySkyLight, int holdingEntitySkyLight, float i, float j, float k, float l, int pieceIndex, boolean isLeashKnot) {
        float m = (float) pieceIndex / 24.0F;
        int n = (int) Mth.lerp(m, (float) leashedEntityBlockLight, (float) holdingEntityBlockLight);
        int o = (int) Mth.lerp(m, (float) leashedEntitySkyLight, (float) holdingEntitySkyLight);
        int p = LightTexture.pack(n, o);
        float q = pieceIndex % 2 == (isLeashKnot ? 1 : 0) ? 0.7F : 1.0F;
        float r = 0.5F * q;
        float s = 0.4F * q;
        float t = 0.3F * q;
        float u = f * m;
        float v = g > 0.0F ? g * m * m : g - g * (1.0F - m) * (1.0F - m);
        float w = h * m;
        vertexConsumer.vertex(modelMatrix, u - k, v + j, w + l).color(r, s, t, 1.0F).uv2(p).endVertex();
        vertexConsumer.vertex(modelMatrix, u + k, v + i - j, w - l).color(r, s, t, 1.0F).uv2(p).endVertex();
    }
}
