package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class MeerkatModel extends AnimatedGeoModel<MeerkatEntity> {
    @Override
    public Identifier getAnimationFileLocation(MeerkatEntity entity) {
        return null;
    }

    @Override
    public Identifier getModelLocation(MeerkatEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/meerkat.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MeerkatEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/meerkat.png");
    }

    @Override
    public void setLivingAnimations(MeerkatEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
