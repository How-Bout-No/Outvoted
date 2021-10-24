package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.WildfireHelmetModel;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.mixin.GeoArmorRendererAccessor;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class WildfireHelmetRenderer extends GeoArmorRenderer<WildfireHelmetItem> {
    private static final Identifier HELMET_TEXTURE = new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png");
    private static final Identifier HELMET_TEXTURE_SOUL = new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire_soul.png");

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
    public void renderEarly(WildfireHelmetItem animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        if (((GeoArmorRendererAccessor) this).getEntityLiving().isBaby()) {
            stackIn.scale(0.7F, 0.7F, 0.7F);
            stackIn.translate(0F, -0.4F, 0F);
        }
    }

    @Override
    public Identifier getTextureLocation(WildfireHelmetItem instance) {
        ItemStack stack = ((GeoArmorRendererAccessor) this).getEntityLiving().getEquippedStack(EquipmentSlot.HEAD);
        if (Outvoted.clientConfig.wildfireVariants) {
            if (stack.getNbt() != null && stack.getNbt().getFloat("SoulTexture") == 1.0F) {
                return HELMET_TEXTURE_SOUL;
            }
        }
        return HELMET_TEXTURE;
    }
}
