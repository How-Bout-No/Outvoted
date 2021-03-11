package io.github.how_bout_no.outvoted.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WildfireHelmetModel<T extends LivingEntity> extends BipedModel<T> {

    public ModelRenderer head;

    public WildfireHelmetModel() {
        super(1.0F);
        this.texWidth = 64;
        this.texHeight = 64;

        this.head = new ModelRenderer(this);
        float offset = -1;
        this.head.setPos(0.0F, 0.5F, 0.0F);
        this.head.texOffs(33, 14).addBox(3.5F, -9.0F - offset, -4.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 17).addBox(3.5F, -8.0F - offset, -3.5F, 1.0F, 4.0F, 7.0F, 0.1F, false);
        this.head.texOffs(24, 17).addBox(-4.5F, -8.0F - offset, -3.5F, 1.0F, 4.0F, 7.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(3.5F, -9.0F - offset, -2.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(3.5F, -9.0F - offset, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(3.5F, -9.0F - offset, 1.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(33, 14).addBox(3.5F, -9.0F - offset, 3.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.texOffs(33, 14).addBox(-4.5F, -9.0F - offset, -4.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(-4.5F, -9.0F - offset, -2.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(-4.5F, -9.0F - offset, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(-4.5F, -9.0F - offset, 1.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(33, 14).addBox(-4.5F, -9.0F - offset, 3.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(-2.5F, -9.0F - offset, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(-0.5F, -9.0F - offset, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 0).addBox(1.5F, -9.0F - offset, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(4, 2).addBox(-3.5F, -4.0F - offset, -4.5F, 1.0F, 4.0F, 1.0F, 0.1F, false);
        this.head.texOffs(0, 2).addBox(2.5F, -4.0F - offset, -4.5F, 1.0F, 4.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 2).addBox(-3.5F, -8.0F - offset, -4.5F, 7.0F, 2.0F, 1.0F, 0.1F, false);
        this.head.texOffs(32, 8).addBox(-1.0F, -8.5F - offset, -4.75F, 2.0F, 2.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 28).addBox(-3.5F, -8.0F - offset, 3.5F, 7.0F, 7.0F, 1.0F, 0.1F, false);
        this.head.texOffs(24, 6).addBox(-2.5F, -9.0F - offset, -4.5F, 5.0F, 1.0F, 1.0F, 0.1F, false);
        this.head.texOffs(0, 0).addBox(-1.5F, -10.0F - offset, -4.5F, 3.0F, 1.0F, 1.0F, 0.1F, false);
    }

    /**
     * Hacky fix cause it's broken
     * <p>
     * Adapted from https://gitlab.com/modding-legacy/mining-helmet/
     */
    @Override
    protected Iterable<ModelRenderer> headParts() {
        // TODO SUPER HACKY FIX UNTIL FORGE CAN FIX setRotationAngles
        float offset = this.head.y;

        this.head.copyFrom(this.head);

        this.head.y = offset;

        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of();
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}