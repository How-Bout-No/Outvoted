package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class KrakenModel extends AnimatedGeoModel<KrakenEntity> {
    @Override
    public Identifier getAnimationFileLocation(KrakenEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/kraken.animation.json");
    }

    @Override
    public Identifier getModelLocation(KrakenEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/kraken.geo.json");
    }

    @Override
    public Identifier getTextureLocation(KrakenEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/kraken.png");
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
