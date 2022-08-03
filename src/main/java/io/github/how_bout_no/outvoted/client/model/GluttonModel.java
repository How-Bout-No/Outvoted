package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class GluttonModel extends AnimatedGeoModel<GluttonEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GluttonEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/glutton.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GluttonEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/glutton.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GluttonEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/glutton/glutton.png");
    }

    @Override
    public void setLivingAnimations(GluttonEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone legBR = this.getBone("LegBR");
        IBone legBL = this.getBone("LegBL");
        IBone legFR = this.getBone("LegFR");
        IBone legFL = this.getBone("LegFL");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 330F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 330F));
        float limbSwing = entity.animationPosition;
        float limbSwingAmount = entity.animationSpeed;
        legBR.setRotationX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        legBL.setRotationX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFR.setRotationX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
        legFL.setRotationX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
    }
}
