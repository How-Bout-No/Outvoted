package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.GlareEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class GlareModel extends AnimatedGeoModel<GlareEntity> {
    @Override
    public Identifier getAnimationFileLocation(GlareEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/cyube.animation.json");
    }

    @Override
    public Identifier getModelLocation(GlareEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/glare.geo.json");
    }

    @Override
    public Identifier getTextureLocation(GlareEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/glare.png");
    }

    @Override
    public void setLivingAnimations(GlareEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
//        IBone mob = this.getBone("mob");
//
//        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
//        mob.setRotationX((extraData.headPitch * ((float) Math.PI / 180F) - (90 * (float) Math.PI / 180)));
//        mob.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
