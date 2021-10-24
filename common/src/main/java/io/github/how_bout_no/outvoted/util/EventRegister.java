package io.github.how_bout_no.outvoted.util;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class EventRegister {
    public static void init() {
        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (!entity.world.isClient()) {
                if (entity instanceof PlayerEntity player) {
                    if (amount > 0.0F && player.isBlocking() && player.getActiveItem().getItem() == ModItems.WILDFIRE_SHIELD.get()) {
                        if (source.getSource() != null) {
                            if (source.isProjectile()) {
                                source.getSource().setOnFireFor(5);
                            } else if (source.getAttacker() != null) {
                                source.getAttacker().setOnFireFor(5);
                            }
                        }
                    }
                }
            }
            return EventResult.pass();
        });

//        PlayerEvent.PLAYER_JOIN.register((PatchouliCompat::giveBook));
//        BlockEvent.PLACE.register((level, pos, state, placer) -> {
//            if (!level.isClient()) {
//                if (state.getBlock() == Blocks.LIGHTNING_ROD) {
//                    BlockState head = level.getBlockState(pos.down());
//                    BlockState body = level.getBlockState(pos.down(2));
//                    if (head.getBlock() == Blocks.CARVED_PUMPKIN && body.getBlock() instanceof OxidizableBlock) {
//                        CopperGolemEntity entity = ModEntityTypes.COPPER_GOLEM.get().create(level);
//                        entity.refreshPositionAndAngles(pos.down(2), head.get(CarvedPumpkinBlock.FACING).asRotation(), entity.getPitch());
//                        level.removeBlock(pos, false);
//                        level.removeBlock(pos.down(), false);
//                        level.removeBlock(pos.down(2), false);
//                        level.spawnEntity(entity);
//                        entity.setOxidizationLevel(((OxidizableBlock) body.getBlock()).getDegradationLevel().ordinal());
//                    }
//                }
//            }
//            return EventResult.pass();
//        });
    }

    static class CopperScraping {
        public static Map<Block, Block> BLOCK_SCRAPING_MAP = new HashMap<>();

        static {
            BLOCK_SCRAPING_MAP.put(ModBlocks.COPPER_BUTTON.get(), ModBlocks.WAXED_COPPER_BUTTON.get());
            BLOCK_SCRAPING_MAP.put(ModBlocks.EXPOSED_COPPER_BUTTON.get(), ModBlocks.WAXED_EXPOSED_COPPER_BUTTON.get());
            BLOCK_SCRAPING_MAP.put(ModBlocks.WEATHERED_COPPER_BUTTON.get(), ModBlocks.WAXED_WEATHERED_COPPER_BUTTON.get());
            BLOCK_SCRAPING_MAP.put(ModBlocks.OXIDIZED_COPPER_BUTTON.get(), ModBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
        }
    }
}
