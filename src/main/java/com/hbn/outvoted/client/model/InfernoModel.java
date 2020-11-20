package com.hbn.outvoted.client.model;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.entities.InfernoEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class InfernoModel extends AnimatedGeoModel<InfernoEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(InfernoEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/inferno.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(InfernoEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/inferno.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(InfernoEntity entity) {
        return null;
    }

    @Override
    public void setLivingAnimations(InfernoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
