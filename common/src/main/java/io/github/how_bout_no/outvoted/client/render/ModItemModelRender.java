//package io.github.how_bout_no.outvoted.client.render;
//
//import io.github.how_bout_no.outvoted.init.ModItems;
//import net.minecraft.client.render.VertexConsumerProvider;
//import net.minecraft.client.render.item.BuiltinModelItemRenderer;
//import net.minecraft.client.render.model.json.ModelTransformation;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.item.ItemStack;
//
//public class ModItemModelRender extends BuiltinModelItemRenderer {
//    @Override
//    public void render(ItemStack itemStack, ModelTransformation.Mode transformType, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i, int j) {
//        if (itemStack.getItem() == ModItems.WILDFIRE_SHIELD.get()) {
//            ShieldRenderer.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
//        } else {
//            super.render(itemStack, transformType, poseStack, multiBufferSource, i, j);
//        }
//    }
//}
