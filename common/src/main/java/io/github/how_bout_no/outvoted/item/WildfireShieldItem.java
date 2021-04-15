package io.github.how_bout_no.outvoted.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ShieldItem;

public class WildfireShieldItem extends ShieldItem {
    public WildfireShieldItem(Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }
}
