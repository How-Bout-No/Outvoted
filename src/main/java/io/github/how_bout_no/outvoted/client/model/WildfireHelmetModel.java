package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class WildfireHelmetModel extends AnimatedGeoModel<WildfireHelmetItem> {
    @Override
    public ResourceLocation getAnimationFileLocation(WildfireHelmetItem item) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(WildfireHelmetItem item) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/wildfirehelmet.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(WildfireHelmetItem item) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire.png");
    }
}