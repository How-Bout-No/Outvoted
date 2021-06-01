package io.github.how_bout_no.outvoted.util;

import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModItems;
import io.github.how_bout_no.outvoted.util.compat.PatchouliCompat;
import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.event.events.InteractionEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class EventRegister {
    public static void init() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register((PlayerEntity player, Hand hand, BlockPos pos, Direction face) -> {
            ItemStack itemStack = player.getStackInHand(hand);
            if (itemStack.getItem() instanceof AxeItem) {
                World world = player.getEntityWorld();
                BlockState blockstate = world.getBlockState(pos);
                Block block = WoodStripping.BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
                if (block != null) {
                    world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    player.swingHand(hand, true);
                    if (!world.isClient) {
                        world.setBlockState(pos, block.getDefaultState()
                                .with(PillarBlock.AXIS, blockstate.get(PillarBlock.AXIS)), 11);
                        itemStack.damage(1, player, (player1) -> {
                            player1.sendToolBreakStatus(hand);
                        });
//                        return ActionResult.SUCCESS;
                    }
                }
            }
            return ActionResult.PASS;
        });

        EntityEvent.LIVING_ATTACK.register((entity, source, amount) -> {
            if (!entity.world.isClient()) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    if (amount > 0.0F && player.isBlocking() && player.getActiveItem().getItem() == ModItems.WILDFIRE_SHIELD.get()) {
                        if (source.getSource() != null) {
                            if (source.isProjectile()) {
                                source.getSource().setOnFireFor(5);
                            } else {
                                source.getAttacker().setOnFireFor(5);
                            }
                        }
                    }
                }
            }
            return ActionResult.PASS;
        });

        PlayerEvent.PLAYER_JOIN.register((PatchouliCompat::giveBook));
    }

    static class WoodStripping {
        public static Map<Block, Block> BLOCK_STRIPPING_MAP = new HashMap<>();

        static {
            BLOCK_STRIPPING_MAP.put(ModBlocks.PALM_LOG.get(), ModBlocks.STRIPPED_PALM_LOG.get());
            BLOCK_STRIPPING_MAP.put(ModBlocks.PALM_WOOD.get(), ModBlocks.STRIPPED_PALM_WOOD.get());
            BLOCK_STRIPPING_MAP.put(ModBlocks.BAOBAB_LOG.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get());
            BLOCK_STRIPPING_MAP.put(ModBlocks.BAOBAB_WOOD.get(), ModBlocks.STRIPPED_BAOBAB_WOOD.get());
        }
    }
}
