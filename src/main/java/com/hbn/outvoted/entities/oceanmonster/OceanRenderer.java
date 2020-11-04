package com.hbn.outvoted.entities.oceanmonster;

import com.hbn.outvoted.Outvoted;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OceanRenderer extends MobRenderer<OceanEntity, OceanModel<OceanEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/ocean_monster.png");

    public OceanRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new OceanModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(OceanEntity entity) {
        return TEXTURE;
    }
}
