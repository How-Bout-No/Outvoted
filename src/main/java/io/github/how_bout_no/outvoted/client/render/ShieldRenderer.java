// Adapted from https://github.com/ToMe25/Better-Shields/

package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.how_bout_no.outvoted.client.model.WildfireShieldModel;
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
    public void renderByItem(ItemStack stack, TransformType p_239207_2_, MatrixStack matrixStack,
                               IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.scale(1, -1, -1);
        /*RenderMaterial rendermaterial = flag ? ModelBakery.LOCATION_SHIELD_BASE
                : ModelBakery.LOCATION_SHIELD_NO_PATTERN;*/

        //Item shield = stack.getItem();
        //rendermaterial = flag ? WildfireShieldModel.base : WildfireShieldModel.basenop;
        RenderMaterial rendermaterial = WildfireShieldModel.base;

        IVertexBuilder ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(
                buffer, shieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
        this.shieldModel.handle().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
                1.0F, 1.0F, 1.0F);
        this.shieldModel.plate().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
                1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

}
