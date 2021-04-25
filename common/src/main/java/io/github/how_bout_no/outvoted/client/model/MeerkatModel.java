package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class MeerkatModel extends AnimatedGeoModel<MeerkatEntity> {
    @Override
    public Identifier getAnimationFileLocation(MeerkatEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/meerkat.animation.json");
    }

    @Override
    public Identifier getModelLocation(MeerkatEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/meerkat.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MeerkatEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/meerkat.png");
    }

    @Override
    public void setLivingAnimations(MeerkatEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone legBR = this.getBone("legr");
        IBone legBL = this.getBone("legl");
        IBone legFR = this.getBone("armr");
        IBone legFL = this.getBone("arml");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        float limbSwing = entity.limbAngle;
        float limbSwingAmount = entity.limbDistance;
        legBR.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        legBL.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFR.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFL.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
    }
}
