package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.OstrichEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class OstrichModel extends AnimatedGeoModel<OstrichEntity> {
    @Override
    public Identifier getAnimationFileLocation(OstrichEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/empty.animation.json");
    }

    @Override
    public Identifier getModelLocation(OstrichEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/ostrich.geo.json");
    }

    @Override
    public Identifier getTextureLocation(OstrichEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/ostrich.png");
    }

    @Override
    public void setLivingAnimations(OstrichEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone legBR = this.getBone("right");
        IBone legBL = this.getBone("left");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 300F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 300F));
        float limbSwing = entity.limbAngle;
        float limbSwingAmount = entity.limbDistance;
        legBR.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount);
        legBL.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount);
    }
}
