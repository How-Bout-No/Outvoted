package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.Barnacle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class BarnacleModel extends AnimatedGeoModel<Barnacle> {
    @Override
    public ResourceLocation getAnimationFileLocation(Barnacle entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/barnacle.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Barnacle entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/barnacle.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Barnacle entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/barnacle.png");
    }

    @Override
    public void setLivingAnimations(Barnacle entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone mob = this.getBone("mob");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        mob.setRotationX((extraData.headPitch * ((float) Math.PI / 180F) - (90 * (float) Math.PI / 180)));
        mob.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
