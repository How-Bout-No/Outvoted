package com.hbn.outvoted.client.render;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.entities.SoulBlazeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BlazeModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulBlazeRenderer extends MobRenderer<SoulBlazeEntity, BlazeModel<SoulBlazeEntity>> {

    public SoulBlazeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BlazeModel<>(), 0.5F);
    }

    protected int getBlockLight(SoulBlazeEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getEntityTexture(SoulBlazeEntity entity) {
        return new ResourceLocation(Outvoted.MOD_ID, "textures/entity/soul_blaze.png");
    }
}