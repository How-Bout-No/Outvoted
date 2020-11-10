package com.hbn.outvoted.entities.kraken;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;
import software.bernie.geckolib.entity.IAnimatedEntity;


@OnlyIn(Dist.CLIENT)
public class KrakenModel<T extends KrakenEntity & IAnimatedEntity> extends AnimatedEntityModel<T> {

    private final AnimatedModelRenderer Monster;
    private final AnimatedModelRenderer Body;
    private final AnimatedModelRenderer Tentacles;
    private final AnimatedModelRenderer Tent1;
    private final AnimatedModelRenderer Tent2;
    private final AnimatedModelRenderer Tent3;
    private final AnimatedModelRenderer Tent4;
    private final AnimatedModelRenderer Tongue;

    public KrakenModel() {
        textureWidth = 256;
        textureHeight = 160;
        Monster = new AnimatedModelRenderer(this);
        Monster.setRotationPoint(0.0F, 16.0F, 4.0F);
        setRotationAngle(Monster, 1.5708F, 0.0F, 0.0F);

        Monster.setModelRendererName("Monster");
        this.registerModelRenderer(Monster);

        Body = new AnimatedModelRenderer(this);
        Body.setRotationPoint(0.0F, 4.5F, 0.0F);
        Monster.addChild(Body);
        Body.setTextureOffset(0, 56).addBox(-8.0F, -16.5F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
        Body.setTextureOffset(0, 100).addBox(-8.0F, -0.5F, -8.0F, 16.0F, 20.0F, 16.0F, 0.0F, false);
        Body.setModelRendererName("Body");
        this.registerModelRenderer(Body);

        Tentacles = new AnimatedModelRenderer(this);
        Tentacles.setRotationPoint(0.0F, 12.0F, 0.0F);
        Monster.addChild(Tentacles);

        Tentacles.setModelRendererName("Tentacles");
        this.registerModelRenderer(Tentacles);

        Tent1 = new AnimatedModelRenderer(this);
        Tent1.setRotationPoint(-4.0F, -16.0F, 0.0F);
        Tentacles.addChild(Tent1);
        Tent1.setTextureOffset(48, 0).addBox(-8.0F, -52.0F, 0.0F, 12.0F, 44.0F, 12.0F, 0.0F, false);
        Tent1.setModelRendererName("Tent1");
        this.registerModelRenderer(Tent1);

        Tent2 = new AnimatedModelRenderer(this);
        Tent2.setRotationPoint(4.0F, -16.0F, 0.0F);
        Tentacles.addChild(Tent2);
        Tent2.setTextureOffset(96, 0).addBox(-4.0F, -52.0F, 0.0F, 12.0F, 44.0F, 12.0F, 0.0F, false);
        Tent2.setModelRendererName("Tent2");
        this.registerModelRenderer(Tent2);

        Tent3 = new AnimatedModelRenderer(this);
        Tent3.setRotationPoint(4.0F, -16.0F, 0.0F);
        Tentacles.addChild(Tent3);
        Tent3.setTextureOffset(0, 0).addBox(-4.0F, -52.0F, -12.0F, 12.0F, 44.0F, 12.0F, 0.0F, false);
        Tent3.setModelRendererName("Tent3");
        this.registerModelRenderer(Tent3);

        Tent4 = new AnimatedModelRenderer(this);
        Tent4.setRotationPoint(-4.0F, -16.0F, 0.0F);
        Tentacles.addChild(Tent4);
        Tent4.setTextureOffset(144, 0).addBox(-8.0F, -52.0F, -12.0F, 12.0F, 44.0F, 12.0F, 0.0F, false);
        Tent4.setModelRendererName("Tent4");
        this.registerModelRenderer(Tent4);

        Tongue = new AnimatedModelRenderer(this);
        Tongue.setRotationPoint(0.0F, -9.0F, 0.0F);
        Monster.addChild(Tongue);
        Tongue.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        Tongue.setModelRendererName("Tongue");
        this.registerModelRenderer(Tongue);

        this.rootBones.add(Monster);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Monster.rotateAngleX = headPitch * ((float) Math.PI / 180F) + 1.7F;
        this.Monster.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
    }

    @Override
    public ResourceLocation getAnimationFileLocation() {
        return new ResourceLocation("outvoted", "animations/kraken.animation.json");
    }
}