package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class HungerModel extends AnimatedGeoModel<HungerEntity> {
    @Override
    public Identifier getAnimationFileLocation(HungerEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/hunger.animation.json");
    }

    @Override
    public Identifier getModelLocation(HungerEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/hunger.geo.json");
    }

    @Override
    public Identifier getTextureLocation(HungerEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/hunger/hunger.png");
    }

    @Override
    public void setLivingAnimations(HungerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone legBR = this.getBone("LegBR");
        IBone legBL = this.getBone("LegBL");
        IBone legFR = this.getBone("LegFR");
        IBone legFL = this.getBone("LegFL");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 330F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 330F));
        float limbSwing = entity.limbAngle;
        float limbSwingAmount = entity.limbDistance;
        legBR.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        legBL.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFR.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFL.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
    }
}
