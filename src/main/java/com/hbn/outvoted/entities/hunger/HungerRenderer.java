package com.hbn.outvoted.entities.hunger;

import com.hbn.outvoted.Outvoted;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HungerRenderer extends MobRenderer<HungerEntity, HungerModel<HungerEntity>> {

    protected static final ResourceLocation SANDTEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger.png");
    protected static final ResourceLocation SWAMPTEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/hunger_swamp.png");

    public HungerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HungerModel<>(), 0.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(HungerEntity entity) {
        if (entity.variant() == 0) {
            return SANDTEXTURE;
        } else {
            return SWAMPTEXTURE;
        }
    }
}
