package io.github.how_bout_no.outvoted.client.render;

import io.github.how_bout_no.outvoted.client.model.WildfireHelmetModel;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class WildfireHelmetRenderer extends GeoArmorRenderer<WildfireHelmetItem> {
    public WildfireHelmetRenderer() {
        super(new WildfireHelmetModel());

        this.headBone = "helmet";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }
}
