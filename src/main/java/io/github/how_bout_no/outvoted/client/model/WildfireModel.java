package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.Wildfire;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class WildfireModel extends AnimatedGeoModel<Wildfire> {
    @Override
    public ResourceLocation getAnimationFileLocation(Wildfire entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/wildfire.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Wildfire entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/wildfire.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Wildfire entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png");
    }

    @Override
    public void setLivingAnimations(Wildfire entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
