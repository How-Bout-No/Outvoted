package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class WildfireModel extends AnimatedGeoModel<WildfireEntity> {
    @Override
    public Identifier getAnimationFileLocation(WildfireEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/wildfire.animation.json");
    }

    @Override
    public Identifier getModelLocation(WildfireEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/wildfire.geo.json");
    }

    @Override
    public Identifier getTextureLocation(WildfireEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png");
    }

    @Override
    public void setLivingAnimations(WildfireEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
