package io.github.how_bout_no.outvoted.item;

import dev.architectury.injectables.annotations.PlatformOnly;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.OutvotedModPlatform;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import org.jetbrains.annotations.Nullable;

public class WildfireShieldItem extends ShieldItem {
    public WildfireShieldItem() {
        super(OutvotedModPlatform.setISTER(new Settings().maxDamage(750).fireproof().group(Outvoted.TAB_COMBAT)));
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isIn(group, Outvoted.TAB_COMBAT);
    }

    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.getItem() == ModItems.WILDFIRE_PIECE.get() || super.canRepair(stack, ingredient);
    }

    @PlatformOnly("forge")
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }
}
