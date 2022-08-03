package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class CopperGolemModel extends AnimatedGeoModel<CopperGolemEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(CopperGolemEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/copper_golem.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(CopperGolemEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/copper_golem.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolemEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem.png");
    }

    @Override
    public void setLivingAnimations(CopperGolemEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getBone("head");
        IBone rightArm = this.getBone("rightarm");
        IBone leftArm = this.getBone("leftarm");
        IBone rightLeg = this.getBone("rightleg");
        IBone leftLeg = this.getBone("leftleg");

        if (entity.getOxidizationLevel() < 3) {
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            float limbSwing = entity.animationPosition;
            float limbSwingAmount = entity.animationSpeed;
            float oxidizeMult = entity.getOxidizationMultiplier();
            if (oxidizeMult < 1 && oxidizeMult > 0) oxidizeMult += 0.25F;
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 330F));
            rightArm.setRotationX(Mth.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount);
            leftArm.setRotationX(Mth.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount);
            rightLeg.setRotationX(Mth.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount);
            leftLeg.setRotationX(Mth.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount);
        } else {
            float[] rot = entity.getRotations();
            head.setRotationX(rot[0] * -((float) Math.PI / 180F));
            head.setRotationY(rot[1] * -((float) Math.PI / 330F));
            rightArm.setRotationX(rot[2]);
            leftArm.setRotationX(rot[3]);
            rightLeg.setRotationX(rot[4]);
            leftLeg.setRotationX(rot[5]);
        }
    }
}
