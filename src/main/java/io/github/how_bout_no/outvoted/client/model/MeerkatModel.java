package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.Meerkat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class MeerkatModel extends AnimatedGeoModel<Meerkat> {
    public static final float scale = 0.9F;
    public static final float babyScale = 0.6F;

    @Override
    public ResourceLocation getAnimationFileLocation(Meerkat entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/meerkat.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Meerkat entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/meerkat.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Meerkat entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/meerkat.png");
    }

    @Override
    public void setLivingAnimations(Meerkat entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("Head");
        IBone legBR = this.getBone("RightLeg");
        IBone legBL = this.getBone("LeftLeg");
        IBone legFR = this.getBone("ArmRight");
        IBone legFL = this.getBone("ArmLeft");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 3000F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 300F));
        float limbSwing = entity.animationPosition;
        float limbSwingAmount = entity.animationSpeed;
        legBR.setRotationX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        legBL.setRotationX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFL.setRotationX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        legFR.setRotationX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
    }
}
