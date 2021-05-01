package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.item.WildfireShieldItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {
    /**
     * Sets entities that attack a player blocking with a Wildfire Shield on fire
     */
    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        LivingEntity player = event.getEntityLiving();
        if (event.getAmount() > 0.0F && player.isBlocking() && player.getActiveItem().getItem() instanceof WildfireShieldItem) {
            if (event.getSource() != null) {
                if (event.getSource().isProjectile()) {
                    event.getSource().getSource().setOnFireFor(5);
                } else {
                    event.getSource().getAttacker().setOnFireFor(5);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockClicked(PlayerInteractEvent.RightClickBlock event) {
        /* Allows stripping of logs */
        if (event.getItemStack().getItem() instanceof AxeItem) {
            World world = event.getWorld();
            BlockPos blockpos = event.getPos();
            BlockState blockstate = world.getBlockState(blockpos);
            Block block = WoodStripping.BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
            if (block != null) {
                PlayerEntity playerentity = event.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClient) {
                    world.setBlockState(blockpos, block.getDefaultState()
                            .with(PillarBlock.AXIS, blockstate.get(PillarBlock.AXIS)), 11);
                    System.out.println(playerentity);
                    if (playerentity != null) {
                        System.out.println(event.getItemStack());
                        System.out.println(event.getHand());
                        event.getItemStack().damage(1, playerentity, (player) -> {
                            player.sendToolBreakStatus(event.getHand());
                        });
                    }
                }
            }
        }
    }

    static class WoodStripping {
        public static Map<Block, Block> BLOCK_STRIPPING_MAP = new HashMap<>();

        static {
            BLOCK_STRIPPING_MAP.put(ModBlocks.PALM_LOG.get(), ModBlocks.STRIPPED_PALM_LOG.get());
            BLOCK_STRIPPING_MAP.put(ModBlocks.PALM_WOOD.get(), ModBlocks.STRIPPED_PALM_WOOD.get());
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