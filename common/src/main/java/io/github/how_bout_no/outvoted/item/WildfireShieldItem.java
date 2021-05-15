package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.OutvotedModPlatform;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import me.shedaniel.architectury.annotations.PlatformOnly;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import org.jetbrains.annotations.Nullable;

public class WildfireShieldItem extends ShieldItem {
    public WildfireShieldItem() {
        super(OutvotedModPlatform.setISTER(new Item.Settings().maxDamage(750).fireproof().group(Outvoted.TAB_COMBAT)));
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isIn(group, Outvoted.TAB_COMBAT);
    }

    @PlatformOnly("forge")
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }
}
