package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.client.model.WildfireHelmetModel;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.mixin.GeoArmorRendererAccessor;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeoArmorRendererFactory;

public class WildfireHelmetRenderer extends GeoArmorRenderer<WildfireHelmetItem> {
    public WildfireHelmetRenderer(GeoArmorRendererFactory.Context ctx) {
        super(new WildfireHelmetModel(), ctx, EntityModelLayers.PLAYER_OUTER_ARMOR);

        this.headBone = "helmet";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }

    @Override
    public void renderEarly(WildfireHelmetItem animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        if (((GeoArmorRendererAccessor) this).getEntityLiving().isBaby()) {
            stackIn.scale(0.7F, 0.7F, 0.7F);
            stackIn.translate(0F, -0.4F, 0F);
        }
    }
}
