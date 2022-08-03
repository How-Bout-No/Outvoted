package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class WildfireHelmetModel extends AnimatedGeoModel<WildfireHelmetItem> {
    @Override
    public ResourceLocation getAnimationFileLocation(WildfireHelmetItem item) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/empty.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(WildfireHelmetItem item) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/wildfirehelmet.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(WildfireHelmetItem item) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png");
    }
}