package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.item.WildfireShieldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEvents {

    /**
     * Sets entities that attack a player blocking with an Wildfire Shield on fire
     */
    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() != null) {
            Entity attacker = event.getSource().getTrueSource();
            LivingEntity player = event.getEntityLiving();
            Item shield = player.getActiveItemStack().getItem();
            if (shield instanceof WildfireShieldItem) {
                if (player.isActiveItemStackBlocking()) {
                    if (event.getSource().isProjectile()) {
                        event.getSource().getImmediateSource().setFire(5);
                    } else {
                        attacker.setFire(5);
                    }
                }
            }
        }
    }

    /**
     * Sets variant texture tags for Wildfire Helmet
     */
    @SubscribeEvent
    public void onEntityDrops(LivingDropsEvent event) {
        if (event.getEntity().getType() == ModEntityTypes.WILDFIRE.get()) {
            WildfireEntity entity = (WildfireEntity) event.getEntityLiving();
            ItemEntity helmet = event.getDrops().stream().filter(item -> item.getItem().getItem() == ModItems.WILDFIRE_HELMET.get()).findFirst().orElse(null);
            if (helmet != null) {
                if (entity.getVariant() == 1) {
                    helmet.getItem().getOrCreateTag().putFloat("CustomModelData", 1.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public void fixItemMappings(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            if (entry.key.getNamespace().equals(Outvoted.MOD_ID)) {
                if (entry.key.getPath().equals("inferno_helmet")) {
                    entry.remap(ModItems.WILDFIRE_HELMET.get());
                }
                if (entry.key.getPath().equals("inferno_shield")) {
                    entry.remap(ModItems.WILDFIRE_SHIELD.get());
                }
                if (entry.key.getPath().equals("inferno_shield_part")) {
                    entry.remap(ModItems.WILDFIRE_SHIELD_PART.get());
                }
                if (entry.key.getPath().equals("inferno_piece")) {
                    entry.remap(ModItems.WILDFIRE_PIECE.get());
                }
                if (entry.key.getPath().equals("inferno_spawn_egg")) {
                    entry.remap(ModItems.WILDFIRE_SPAWN_EGG.get());
                }
            }
        }
    }

    @SubscribeEvent
    public void fixEntityMappings(RegistryEvent.MissingMappings<EntityType<?>> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityType<?>> entry : event.getAllMappings()) {
            if (entry.key.getNamespace().equals(Outvoted.MOD_ID)) {
                if (entry.key.getPath().equals("inferno")) {
                    entry.remap(ModEntityTypes.WILDFIRE.get());
                }
            }
        }
    }
}