package com.hbn.outvoted.entities.kraken;

import com.hbn.outvoted.Outvoted;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KrakenRenderer extends MobRenderer<KrakenEntity, KrakenModel<KrakenEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/kraken.png");

    public KrakenRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new KrakenModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(KrakenEntity entity) {
        return TEXTURE;
    }
}
