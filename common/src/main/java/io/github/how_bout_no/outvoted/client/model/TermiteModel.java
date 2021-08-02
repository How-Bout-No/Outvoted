package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.TermiteEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class TermiteModel extends AnimatedGeoModel<TermiteEntity> {
    public static final float scale = 0.75F;

    @Override
    public Identifier getAnimationFileLocation(TermiteEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/empty.animation.json");
    }

    @Override
    public Identifier getModelLocation(TermiteEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/termite.geo.json");
    }

    @Override
    public Identifier getTextureLocation(TermiteEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/termite.png");
    }

    @Override
    public void setLivingAnimations(TermiteEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone legFR = this.getBone("fr");
        IBone legFL = this.getBone("fl");
        IBone legMR = this.getBone("mr");
        IBone legML = this.getBone("ml");
        IBone legBR = this.getBone("br");
        IBone legBL = this.getBone("bl");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 330F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 300F));
        float limbSwing = entity.limbAngle;
        float limbSwingAmount = entity.limbDistance;
        legBR.setRotationX(-(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.9F) * limbSwingAmount);
        legBL.setRotationX(Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.9F) * limbSwingAmount);
        legMR.setRotationX(-(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 3.1415927F) * 0.9F) * limbSwingAmount);
        legML.setRotationX(Math.abs(MathHelper.sin(limbSwing * 0.6662F + 3.1415927F) * 0.9F) * limbSwingAmount);
        legFR.setRotationX(-(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 1.5707964F) * 0.9F) * limbSwingAmount);
        legFL.setRotationX(Math.abs(MathHelper.sin(limbSwing * 0.6662F + 1.5707964F) * 0.9F) * limbSwingAmount);
    }
}
