package com.hbn.outvoted.entities.inferno;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;
import software.bernie.geckolib.entity.IAnimatedEntity;

public class InfernoModel<T extends InfernoEntity & IAnimatedEntity> extends AnimatedEntityModel<T> {
    private final AnimatedModelRenderer head;
    private final AnimatedModelRenderer helmet;
    private final AnimatedModelRenderer body;
    private final AnimatedModelRenderer panels;
    private final AnimatedModelRenderer one;
    private final AnimatedModelRenderer two;
    private final AnimatedModelRenderer three;
    private final AnimatedModelRenderer four;
    private final AnimatedModelRenderer center;

    public InfernoModel() {
        textureWidth = 64;
        textureHeight = 64;
        head = new AnimatedModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.setModelRendererName("head");
        this.registerModelRenderer(head);

        helmet = new AnimatedModelRenderer(this);
        helmet.setRotationPoint(6.0F, 0.0F, -6.0F);
        head.addChild(helmet);
        helmet.setTextureOffset(33, 14).addBox(-2.5F, -8.5F, 1.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 17).addBox(-2.5F, -7.5F, 2.5F, 1.0F, 4.0F, 7.0F, 0.1F, false);
        helmet.setTextureOffset(24, 17).addBox(-10.5F, -7.5F, 2.5F, 1.0F, 4.0F, 7.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-2.5F, -8.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-2.5F, -8.5F, 5.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-2.5F, -8.5F, 7.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(33, 14).addBox(-2.5F, -8.5F, 9.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(33, 14).addBox(-10.5F, -8.5F, 1.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-10.5F, -8.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-10.5F, -8.5F, 5.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-10.5F, -8.5F, 7.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(33, 14).addBox(-10.5F, -8.5F, 9.5F, 1.0F, 9.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-8.5F, -8.5F, 9.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-6.5F, -8.5F, 9.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 0).addBox(-4.5F, -8.5F, 9.5F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(4, 2).addBox(-9.5F, -3.5F, 1.5F, 1.0F, 4.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(0, 2).addBox(-3.5F, -3.5F, 1.5F, 1.0F, 4.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 2).addBox(-9.5F, -7.5F, 1.5F, 7.0F, 2.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(32, 8).addBox(-7.0F, -8.0F, 1.25F, 2.0F, 2.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 28).addBox(-9.5F, -7.5F, 9.5F, 7.0F, 7.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(24, 6).addBox(-8.5F, -8.5F, 1.5F, 5.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setTextureOffset(0, 0).addBox(-7.5F, -9.5F, 1.5F, 3.0F, 1.0F, 1.0F, 0.1F, false);
        helmet.setModelRendererName("helmet");
        this.registerModelRenderer(helmet);

        body = new AnimatedModelRenderer(this);
        body.setRotationPoint(0.0F, 24.0F, 0.0F);

        body.setModelRendererName("body");
        this.registerModelRenderer(body);

        panels = new AnimatedModelRenderer(this);
        panels.setRotationPoint(0.0F, -12.0F, 0.0F);
        body.addChild(panels);

        panels.setModelRendererName("panels");
        this.registerModelRenderer(panels);

        one = new AnimatedModelRenderer(this);
        one.setRotationPoint(0.0F, 0.0F, 0.0F);
        panels.addChild(one);
        setRotationAngle(one, -0.3491F, -0.7854F, 0.0F);
        one.setTextureOffset(0, 16).addBox(-5.0F, -7.0F, -15.0F, 10.0F, 18.0F, 2.0F, 0.0F, false);
        one.setModelRendererName("one");
        this.registerModelRenderer(one);

        two = new AnimatedModelRenderer(this);
        two.setRotationPoint(0.0F, 0.0F, 0.0F);
        panels.addChild(two);
        setRotationAngle(two, -0.3491F, -2.3562F, 0.0F);
        two.setTextureOffset(0, 16).addBox(-5.0F, -7.0F, -15.0F, 10.0F, 18.0F, 2.0F, 0.0F, false);
        two.setModelRendererName("two");
        this.registerModelRenderer(two);

        three = new AnimatedModelRenderer(this);
        three.setRotationPoint(0.0F, 0.0F, 0.0F);
        panels.addChild(three);
        setRotationAngle(three, -0.3491F, 0.7854F, 0.0F);
        three.setTextureOffset(0, 16).addBox(-5.0F, -7.0F, -15.0F, 10.0F, 18.0F, 2.0F, 0.0F, false);
        three.setModelRendererName("three");
        this.registerModelRenderer(three);

        four = new AnimatedModelRenderer(this);
        four.setRotationPoint(0.0F, 0.0F, 0.0F);
        panels.addChild(four);
        setRotationAngle(four, -0.3491F, 2.3562F, 0.0F);
        four.setTextureOffset(0, 16).addBox(-5.0F, -7.0F, -15.0F, 10.0F, 18.0F, 2.0F, 0.0F, false);
        four.setModelRendererName("four");
        this.registerModelRenderer(four);

        center = new AnimatedModelRenderer(this);
        center.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(center);
        center.setTextureOffset(0, 36).addBox(-2.0F, -22.0F, -2.0F, 4.0F, 22.0F, 4.0F, 0.0F, false);
        center.setModelRendererName("center");
        this.registerModelRenderer(center);

        this.rootBones.add(head);
        this.rootBones.add(body);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
    }


    @Override
    public ResourceLocation getAnimationFileLocation() {
        return new ResourceLocation("outvoted", "animations/inferno.animation.json");
    }
}
