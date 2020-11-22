package com.hbn.outvoted.util;

import com.hbn.outvoted.items.InfernoShieldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEvents {

    /**
     * Sets entities that attack a player blocking with an Inferno Shield on fire
     */
    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() != null) {
            Entity attacker = event.getSource().getTrueSource();
            LivingEntity player = event.getEntityLiving();
            Item shield = player.getActiveItemStack().getItem();
            if (shield instanceof InfernoShieldItem) {
                if (player.isActiveItemStackBlocking() && !event.getSource().isProjectile()) {
                    if (event.getSource().isProjectile()) {
                        event.getSource().getImmediateSource().setFire(5);
                    } else {
                        attacker.setFire(5);
                    }
                }
            }
        }
    }
}