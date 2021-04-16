package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShieldItem;

public class WildfireShieldItem extends ShieldItem {
    public WildfireShieldItem(Settings settings) {
        super(settings.maxDamage(750).fireproof());
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isInCombat(group);
    }
}
