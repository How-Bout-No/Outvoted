package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.GlareEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class GlareModel extends AnimatedGeoModel<GlareEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(GlareEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "animations/glare.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GlareEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "geo/glare.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GlareEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/glare/glare.png");
    }
}
