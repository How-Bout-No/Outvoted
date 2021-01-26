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


    /*

    //Wood stripping event
    //copied from https://forums.minecraftforge.net/topic/83842-1144-stripped-logs-help-solved/?do=findComment&comment=396477

    public static Map<Block, Block> BLOCK_STRIPPING_MAP = new HashMap<>();


    //idk how to do this part
    static {
        BLOCK_STRIPPING_MAP.put(Registry.PALM_LOG, Registry.STRIPPED_PALM_LOG);
        BLOCK_STRIPPING_MAP.put(Registry.PALM_WOOD, Registry.STRIPPED_PALM_WOOD);
    }

    @SubscribeEvent
    public static void onBlockClicked(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() instanceof AxeItem) {
            World world = event.getWorld();
            BlockPos blockpos = event.getPos();
            BlockState blockstate = world.getBlockState(blockpos);
            Block block = BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
            if (block != null) {
                PlayerEntity playerentity = event.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote) {
                    world.setBlockState(blockpos, block.getDefaultState()
                            .with(RotatedPillarBlock.AXIS, blockstate.get(RotatedPillarBlock.AXIS)), 11);
                    if (playerentity != null) {
                        event.getItemStack().damageItem(1, playerentity, (p_220040_1_) -> {
                            p_220040_1_.sendBreakAnimation(event.getHand());
                        });
                    }
                }
            }
        }

    }

     */

}