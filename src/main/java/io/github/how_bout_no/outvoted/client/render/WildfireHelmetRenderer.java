package io.github.how_bout_no.outvoted.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.WildfireHelmetModel;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.mixin.GeoArmorRendererAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class WildfireHelmetRenderer extends GeoArmorRenderer<WildfireHelmetItem> {
    private static final ResourceLocation HELMET_TEXTURE = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png");
    private static final ResourceLocation HELMET_TEXTURE_SOUL = new ResourceLocation(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire_soul.png");

    public WildfireHelmetRenderer() {
        super(new WildfireHelmetModel());

        this.headBone = "helmet";
        this.bodyBone = "chestplate";
        this.rightArmBone = "rightArm";
        this.leftArmBone = "leftArm";
        this.rightLegBone = "rightLeg";
        this.leftLegBone = "leftLeg";
        this.rightBootBone = "rightBoot";
        this.leftBootBone = "leftBoot";
    }

    @Override
    public void renderEarly(WildfireHelmetItem item, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(item, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        if (((GeoArmorRendererAccessor) this).getEntityLiving().isBaby()) {
            stackIn.scale(0.7F, 0.7F, 0.7F);
            stackIn.translate(0F, -0.4F, 0F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(WildfireHelmetItem instance) {
        ItemStack stack = ((GeoArmorRendererAccessor) this).getEntityLiving().getItemBySlot(EquipmentSlot.HEAD);
        if (Config.wildfireVariants.get()) {
            if (stack.getTag() != null && stack.getTag().getFloat("SoulTexture") == 1.0F) {
                return HELMET_TEXTURE_SOUL;
            }
        }
        return HELMET_TEXTURE;
    }
}
