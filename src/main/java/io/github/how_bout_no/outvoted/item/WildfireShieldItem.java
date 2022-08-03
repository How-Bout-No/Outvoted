package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Outvoted.MOD_ID)
public class WildfireShieldItem extends ShieldItem {
    public WildfireShieldItem() {
        super(ModItems.ITEM_PROPERTIES.fireResistant().durability(750));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return ingredient.getItem() == ModItems.WILDFIRE_PIECE.get() || super.isValidRepairItem(stack, ingredient);
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent e) {
        if (e.getEntityLiving().getUseItem().is(ModItems.WILDFIRE_SHIELD.get()) && e.getBlockedDamage() > 0.0F) {
            if (e.getDamageSource().getDirectEntity() != null) {
                if (e.getDamageSource().isProjectile()) {
                    e.getDamageSource().getDirectEntity().setSecondsOnFire(5);
                } else if (e.getDamageSource().getEntity() != null) {
                    e.getDamageSource().getEntity().setSecondsOnFire(5);
                }
            }
        }
    }
}
