package com.hbn.outvoted.client.model;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.entity.HungerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class HungerModel extends AnimatedGeoModel<HungerEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(HungerEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/hunger.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(HungerEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/hunger.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HungerEntity entity) {
        return null;
    }

    @Override
    public void setLivingAnimations(HungerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone legBR = this.getAnimationProcessor().getBone("LegBR");
        IBone legBL = this.getAnimationProcessor().getBone("LegBL");
        IBone legFR = this.getAnimationProcessor().getBone("LegFR");
        IBone legFL = this.getAnimationProcessor().getBone("LegFL");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 330F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 330F));
        float limbSwing = entity.limbSwing;
        float limbSwingAmount = entity.limbSwingAmount;
        legBR.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        legBL.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFR.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFL.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
    }
}
