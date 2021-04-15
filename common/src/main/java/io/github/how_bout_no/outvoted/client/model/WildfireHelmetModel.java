package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class WildfireHelmetModel extends AnimatedGeoModel<WildfireHelmetItem> {
    @Override
    public Identifier getAnimationFileLocation(WildfireHelmetItem item) {
        return null;
    }

    @Override
    public Identifier getModelLocation(WildfireHelmetItem item) {
        return new Identifier(Outvoted.MOD_ID, "geo/wildfiredhelmet.geo.json");
    }

    @Override
    public Identifier getTextureLocation(WildfireHelmetItem item) {
        return new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfired.png");
    }
}