package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.entity.InfernoEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.item.InfernoShieldItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

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
     * Sets variant texture tags for Inferno Helmet
     */
    @SubscribeEvent
    public void onEntityDrops(LivingDropsEvent event) {
        if (event.getEntity().getType() == ModEntityTypes.INFERNO.get()) {
            InfernoEntity entity = (InfernoEntity) event.getEntityLiving();
            ItemEntity helmet = event.getDrops().stream().filter(item -> item.getItem().getItem() == ModItems.INFERNO_HELMET.get()).findFirst().orElse(null);
            if (helmet != null) {
                if (entity.getVariant() == 1) {
                    helmet.getItem().getOrCreateTag().putFloat("CustomModelData", 1.0F);
                }
            }
        }
    }
}