package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.Ostrich;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class OstrichModel extends AnimatedGeoModel<Ostrich> {
    @Override
    public ResourceLocation getAnimationFileLocation(Ostrich entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/empty.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Ostrich entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/ostrich.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Ostrich entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/ostrich/ostrich.png");
    }

    @Override
    public void setLivingAnimations(Ostrich entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone legBR = this.getBone("right");
        IBone legBL = this.getBone("left");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 300F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 300F));
        float limbSwing = entity.animationPosition;
        float limbSwingAmount = entity.animationSpeed;
        legBR.setRotationX(Mth.cos(limbSwing * 0.6662F) * limbSwingAmount);
        legBL.setRotationX(Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount);
    }
}
