package io.github.how_bout_no.outvoted.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfernoHelmetModel<T extends LivingEntity> extends BipedModel<T> {

    public ModelRenderer head;

    public InfernoHelmetModel() {
        super(1.0F);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.head = new ModelRenderer(this);
        float offset = -1;
        this.head.setRotationPoint(0.0F, 0.5F, 0.0F);
        this.head.setTextureOffset(33, 14).addBox(3.5F, -9.0F - offset, -4.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 17).addBox(3.5F, -8.0F - offset, -3.5F, 1.0F, 4.0F, 7.0F, 0.1F, false);
        this.head.setTextureOffset(24, 17).addBox(-4.5F, -8.0F - offset, -3.5F, 1.0F, 4.0F, 7.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(3.5F, -9.0F - offset, -2.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(3.5F, -9.0F - offset, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(3.5F, -9.0F - offset, 1.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(33, 14).addBox(3.5F, -9.0F - offset, 3.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(33, 14).addBox(-4.5F, -9.0F - offset, -4.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(-4.5F, -9.0F - offset, -2.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(-4.5F, -9.0F - offset, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(-4.5F, -9.0F - offset, 1.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(33, 14).addBox(-4.5F, -9.0F - offset, 3.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(-2.5F, -9.0F - offset, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(-0.5F, -9.0F - offset, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 0).addBox(1.5F, -9.0F - offset, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(4, 2).addBox(-3.5F, -4.0F - offset, -4.5F, 1.0F, 4.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(0, 2).addBox(2.5F, -4.0F - offset, -4.5F, 1.0F, 4.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 2).addBox(-3.5F, -8.0F - offset, -4.5F, 7.0F, 2.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(32, 8).addBox(-1.0F, -8.5F - offset, -4.75F, 2.0F, 2.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 28).addBox(-3.5F, -8.0F - offset, 3.5F, 7.0F, 7.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(24, 6).addBox(-2.5F, -9.0F - offset, -4.5F, 5.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.setTextureOffset(0, 0).addBox(-1.5F, -10.0F - offset, -4.5F, 3.0F, 1.0F, 1.0F, 0.1F, false);
    }

    /**
     * Hacky fix cause it's broken
     * <p>
     * Adapted from https://gitlab.com/modding-legacy/mining-helmet/
     */
    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        // TODO SUPER HACKY FIX UNTIL FORGE CAN FIX setRotationAngles
        float offset = this.bipedHead.rotationPointY;

        this.head.copyModelAngles(this.bipedHead);

        this.head.rotationPointY = offset;

        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of();
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}