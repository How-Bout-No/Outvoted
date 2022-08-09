package io.github.how_bout_no.outvoted.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import io.github.how_bout_no.outvoted.client.model.WildfireShield;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public abstract class MixinBlockEntityWithoutLevelRenderer {
    @Shadow
    private ShieldModel shieldModel;

    @Inject(method = "renderByItem", at = @At("HEAD"))
    public void setWFShieldModel(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int i, int j, CallbackInfo ci) {
        if (stack.is(ModItems.WILDFIRE_SHIELD.get())) {
            boolean bl = BlockItem.getBlockEntityData(stack) != null;
            matrices.pushPose();
            matrices.scale(1.0F, -1.0F, -1.0F);
            Material material = bl ? WildfireShield.base : WildfireShield.base_nop;
            VertexConsumer vertexConsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(vertexConsumers, this.shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));
            this.shieldModel.handle().render(matrices, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
            if (bl) {
                List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
                BannerRenderer.renderPatterns(matrices, vertexConsumers, i, j, this.shieldModel.plate(), material, false, list, stack.hasFoil());
            } else {
                this.shieldModel.plate().render(matrices, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            matrices.popPose();
        }
    }
}
