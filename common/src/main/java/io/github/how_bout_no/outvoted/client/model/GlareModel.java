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
        return new Identifier(Outvoted.MOD_ID, "animations/glare.animation.json");
    }

    @Override
    public Identifier getModelLocation(GlareEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/glare.geo.json");
    }

    @Override
    public Identifier getTextureLocation(GlareEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/glare/glare.png");
    }
}
