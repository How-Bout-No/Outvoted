package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class BarnacleModel extends AnimatedGeoModel<BarnacleEntity> {
    @Override
    public Identifier getAnimationFileLocation(BarnacleEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/barnacle.animation.json");
    }

    @Override
    public Identifier getModelLocation(BarnacleEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/barnacle.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BarnacleEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/barnacle.png");
    }

    @Override
    public void setLivingAnimations(BarnacleEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone mob = this.getBone("mob");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        mob.setRotationX((extraData.headPitch * ((float) Math.PI / 180F) - (90 * (float) Math.PI / 180)));
        mob.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
