package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class CopperGolemModel extends AnimatedGeoModel<CopperGolemEntity> {
    @Override
    public Identifier getAnimationFileLocation(CopperGolemEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/copper_golem.animation.json");
    }

    @Override
    public Identifier getModelLocation(CopperGolemEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/copper_golem.geo.json");
    }

    @Override
    public Identifier getTextureLocation(CopperGolemEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/copper_golem/copper_golem.png");
    }

    @Override
    public void setLivingAnimations(CopperGolemEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.getOxidizationLevel() < 3) {
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            float limbSwing = entity.limbAngle;
            float limbSwingAmount = entity.limbDistance;
            float oxidizeMult = entity.getOxidizationMultiplier();
            if (oxidizeMult < 1 && oxidizeMult > 0) oxidizeMult += 0.25F;
            entity.cachedVals[0] = extraData.headPitch * ((float) Math.PI / 180F);
            entity.cachedVals[1] = extraData.netHeadYaw * ((float) Math.PI / 330F);
            entity.cachedVals[2] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount;
            entity.cachedVals[3] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount;
            entity.cachedVals[4] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult + (float) Math.PI) * 2.0F * limbSwingAmount;
            entity.cachedVals[5] = MathHelper.cos(limbSwing * 1.0F * oxidizeMult) * 2.0F * limbSwingAmount;
        }
        IBone head = this.getBone("head");
        IBone rightArm = this.getBone("rightarm");
        IBone leftArm = this.getBone("leftarm");
        IBone rightLeg = this.getBone("rightleg");
        IBone leftLeg = this.getBone("leftleg");

        head.setRotationX(entity.cachedVals[0]);
        head.setRotationY(entity.cachedVals[1]);
        rightArm.setRotationX(entity.cachedVals[2]);
        leftArm.setRotationX(entity.cachedVals[3]);
        rightLeg.setRotationX(entity.cachedVals[4]);
        leftLeg.setRotationX(entity.cachedVals[5]);
    }
}
