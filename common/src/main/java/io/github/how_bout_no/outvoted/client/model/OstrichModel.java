package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.OstrichEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class OstrichModel extends AnimatedGeoModel<OstrichEntity> {
    @Override
    public Identifier getAnimationFileLocation(OstrichEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "animations/wildfirehelmet.animation.json");
    }

    @Override
    public Identifier getModelLocation(OstrichEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "geo/cyube.geo.json");
    }

    @Override
    public Identifier getTextureLocation(OstrichEntity entity) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/cyube.png");
    }
}
