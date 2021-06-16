package io.github.how_bout_no.outvoted.client.render;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

/**
 * This renderer extends GeoEntityRenderer to render leashes for Geckolib Entities by using vanillas code from MobEntityRenderer.
 * All rights for the leash code belong to Mojang/original authors.
 * Rights to this workaround belongs to Paspartout ;)
 * @param <T> The entity type to render
 */

public class GeoMobRenderer<T extends MobEntity & IAnimatable> extends GeoEntityRenderer<T> {
    protected GeoMobRenderer(EntityRenderDispatcher renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public Identifier getTexture(T entity) {
        return this.getTextureLocation(entity);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider buffer, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, buffer, packedLightIn);
        Entity leashHolder = entity.getHoldingEntity();
        if (leashHolder != null) {
            this.method_4073(entity, partialTicks, stack, buffer, leashHolder);
        }
    }

    private <E extends Entity> void method_4073(T entity, float partialTicks, MatrixStack stack, VertexConsumerProvider buffer, E leashHolder) {
        stack.push();
        Vec3d lv = leashHolder.method_30951(partialTicks);
        double d = (double) (MathHelper.lerp(partialTicks, entity.bodyYaw, entity.prevBodyYaw) * 0.017453292F) + 1.5707963267948966D;
        Vec3d lv2 = entity.method_29919();
        double e = Math.cos(d) * lv2.z + Math.sin(d) * lv2.x;
        double g = Math.sin(d) * lv2.z - Math.cos(d) * lv2.x;
        double h = MathHelper.lerp(partialTicks, entity.prevX, entity.getX()) + e;
        double i = MathHelper.lerp(partialTicks, entity.prevY, entity.getY()) + lv2.y;
        double j = MathHelper.lerp(partialTicks, entity.prevZ, entity.getZ()) + g;
        stack.translate(e, lv2.y, g);
        float k = (float) (lv.x - h);
        float l = (float) (lv.y - i);
        float m = (float) (lv.z - j);
        VertexConsumer lv3 = buffer.getBuffer(RenderLayer.getLeash());
        Matrix4f lv4 = stack.peek().getModel();
        float o = MathHelper.fastInverseSqrt(k * k + m * m) * 0.025F / 2.0F;
        float p = m * o;
        float q = k * o;
        BlockPos entityPos = new BlockPos(entity.getCameraPosVec(partialTicks));
        BlockPos leashHolderPos = new BlockPos(leashHolder.getCameraPosVec(partialTicks));
        int r = this.getBlockLight(entity, entityPos);
        int s = leashHolder.isOnFire() ? 15 : leashHolder.world.getLightLevel(LightType.BLOCK, leashHolderPos);
        int t = entity.world.getLightLevel(LightType.SKY, entityPos);
        int u = entity.world.getLightLevel(LightType.SKY, leashHolderPos);
        renderSide(lv3, lv4, k, l, m, r, s, t, u, 0.025F, 0.025F, p, q);
        renderSide(lv3, lv4, k, l, m, r, s, t, u, 0.025F, 0.0F, p, q);
        stack.pop();
    }

    public static void renderSide(VertexConsumer buffer, Matrix4f matrix, float f, float g, float h, int i, int j, int k, int l, float m, float n, float o, float p) {
        for(int r = 0; r < 24; ++r) {
            float s = (float)r / 23.0F;
            int t = (int)MathHelper.lerp(s, (float)i, (float)j);
            int u = (int) MathHelper.lerp(s, (float)k, (float)l);
            int packedLight = LightmapTextureManager.pack(t, u);
            addVertexPair(buffer, matrix, packedLight, f, g, h, m, n, 24, r, false, o, p);
            addVertexPair(buffer, matrix, packedLight, f, g, h, m, n, 24, r + 1, true, o, p);
        }
    }

    public static void addVertexPair(VertexConsumer buffer, Matrix4f matrix, int packedLight, float f, float g, float h, float j, float k, int l, int m, boolean bl, float n, float o) {
        float red = 0.5F;
        float green = 0.4F;
        float blue = 0.3F;
        if (m % 2 == 0) {
            red *= 0.7F;
            green *= 0.7F;
            blue *= 0.7F;
        }

        float s = (float)m / (float)l;
        float t = f * s;
        float u = g > 0.0F ? g * s * s : g - g * (1.0F - s) * (1.0F - s);
        float v = h * s;
        if (!bl) {
            buffer.vertex(matrix, t + n, u + j - k, v - o).color(red, green, blue, 1.0F).light(packedLight).next();
        }

        buffer.vertex(matrix, t - n, u + k, v + o).color(red, green, blue, 1.0F).light(packedLight).next();
        if (bl) {
            buffer.vertex(matrix, t + n, u + j - k, v - o).color(red, green, blue, 1.0F).light(packedLight).next();
        }
    }
}
