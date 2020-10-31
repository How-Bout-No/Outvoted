package com.hbn.outvoted.client.render;

import com.hbn.outvoted.client.model.InfernoShieldModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShieldRenderer extends ItemStackTileEntityRenderer {

    @Override
    public void func_239207_a_(ItemStack stack, TransformType p_239207_2_, MatrixStack matrixStack,
                               IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.scale(1, -1, -1);
        /*RenderMaterial rendermaterial = flag ? ModelBakery.LOCATION_SHIELD_BASE
                : ModelBakery.LOCATION_SHIELD_NO_PATTERN;*/

        //Item shield = stack.getItem();
        //rendermaterial = flag ? InfernoShieldModel.base : InfernoShieldModel.basenop;
        RenderMaterial rendermaterial = InfernoShieldModel.base;

        IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(
                buffer, modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, stack.hasEffect()));
        this.modelShield.func_228294_b_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
                1.0F, 1.0F, 1.0F);
        this.modelShield.func_228293_a_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
                1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

}
