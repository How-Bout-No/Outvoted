package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class KrakenModel extends AnimatedGeoModel<KrakenEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(KrakenEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/kraken.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(KrakenEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/kraken.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(KrakenEntity entity) {
        return null;
    }

    @Override
    public void setLivingAnimations(KrakenEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone mob = this.getBone("mob");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        mob.setRotationX((extraData.headPitch * ((float) Math.PI / 180F) - 1.7F));
        mob.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
