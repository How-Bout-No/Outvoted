package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.OutvotedModPlatform;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShieldItem;

public class WildfireShieldItem extends ShieldItem {
    public WildfireShieldItem() {
        super(OutvotedModPlatform.setISTER(new Item.Settings().maxDamage(750).fireproof()));
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isIn(group, Outvoted.TAB_COMBAT);
    }
}
