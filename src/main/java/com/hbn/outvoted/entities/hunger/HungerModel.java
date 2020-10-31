package com.hbn.outvoted.entities.hunger;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;
import software.bernie.geckolib.entity.IAnimatedEntity;

@OnlyIn(Dist.CLIENT)
public class HungerModel<T extends HungerEntity & IAnimatedEntity> extends AnimatedEntityModel<T> {

    private final AnimatedModelRenderer all;
    private final AnimatedModelRenderer head;
    private final AnimatedModelRenderer top;
    private final AnimatedModelRenderer bottom;
    private final AnimatedModelRenderer body;
    private final AnimatedModelRenderer LegFR;
    private final AnimatedModelRenderer LegBR;
    private final AnimatedModelRenderer LegFL;
    private final AnimatedModelRenderer LegBL;

    public HungerModel() {
        textureWidth = 160;
        textureHeight = 96;
        all = new AnimatedModelRenderer(this);
        all.setRotationPoint(0.0F, 24.0F, 0.0F);

        all.setModelRendererName("all");
        this.registerModelRenderer(all);

        head = new AnimatedModelRenderer(this);
        head.setRotationPoint(0.0F, -10.0F, 0.0F);
        all.addChild(head);
        setRotationAngle(head, 0.0F, 0.0F, 0.0F);

        head.setModelRendererName("head");
        this.registerModelRenderer(head);

        top = new AnimatedModelRenderer(this);
        top.setRotationPoint(0.0F, -5.0F, 6.0F);
        head.addChild(top);
        top.setTextureOffset(0, 48).addBox(-9.0F, -4.2F, -19.0F, 18.0F, 4.0F, 20.0F, 0.0F, false);
        top.setTextureOffset(0, 0).addBox(-9.0F, -0.2F, -19.0F, 18.0F, 4.0F, 20.0F, 0.0F, false);
        top.setModelRendererName("top");
        this.registerModelRenderer(top);

        bottom = new AnimatedModelRenderer(this);
        bottom.setRotationPoint(0.0F, -6.0F, 6.0F);
        head.addChild(bottom);
        bottom.setTextureOffset(56, 56).addBox(-9.0F, 0.8F, -19.0F, 18.0F, 4.0F, 20.0F, 0.0F, false);
        bottom.setTextureOffset(0, 24).addBox(-9.0F, 4.8F, -19.0F, 18.0F, 4.0F, 20.0F, 0.0F, false);
        bottom.setModelRendererName("bottom");
        this.registerModelRenderer(bottom);

        body = new AnimatedModelRenderer(this);
        body.setRotationPoint(0.0F, -6.5F, -0.6F);
        all.addChild(body);
        body.setTextureOffset(61, 9).addBox(-6.5F, -2.5F, -7.5F, 13.0F, 5.0F, 15.0F, -0.5F, false);
        body.setModelRendererName("body");
        this.registerModelRenderer(body);

        LegFR = new AnimatedModelRenderer(this);
        LegFR.setRotationPoint(-5.25F, -4.75F, -5.75F);
        all.addChild(LegFR);
        LegFR.setTextureOffset(0, 10).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.25F, false);
        LegFR.setModelRendererName("LegFR");
        this.registerModelRenderer(LegFR);

        LegBR = new AnimatedModelRenderer(this);
        LegBR.setRotationPoint(-5.25F, -4.75F, 5.25F);
        all.addChild(LegBR);
        LegBR.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.25F, false);
        LegBR.setModelRendererName("LegBR");
        this.registerModelRenderer(LegBR);

        LegFL = new AnimatedModelRenderer(this);
        LegFL.setRotationPoint(4.75F, -4.75F, -5.75F);
        all.addChild(LegFL);
        LegFL.setTextureOffset(0, 24).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.25F, false);
        LegFL.setModelRendererName("LegFL");
        this.registerModelRenderer(LegFL);

        LegBL = new AnimatedModelRenderer(this);
        LegBL.setRotationPoint(4.75F, -4.75F, 5.25F);
        all.addChild(LegBL);
        LegBL.setTextureOffset(0, 34).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.25F, false);
        LegBL.setModelRendererName("LegBL");
        this.registerModelRenderer(LegBL);

        this.rootBones.add(all);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float) Math.PI / 330F);
        this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 330F);
        this.body.rotateAngleY = ((float) Math.PI / 2F);
        this.LegBR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.LegBL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.LegFR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.LegFL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public ResourceLocation getAnimationFileLocation() {
        return new ResourceLocation("outvoted", "animations/hunger.animation.json");
    }
}
