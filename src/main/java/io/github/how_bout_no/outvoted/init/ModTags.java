package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> GLUTTON_CAN_BURROW = BlockTags.create(new ResourceLocation(Outvoted.MOD_ID, "glutton_can_burrow"));
//        public static final Tag.Identified<Block> PALM_LOGS = BlockTags.register(Outvoted.MOD_ID + ":palm_logs");
//        public static final Tag.Identified<Block> BAOBAB_LOGS = BlockTags.register(Outvoted.MOD_ID + ":baobab_logs");
    }

    public static class Items {
//        public static final Tag.Identified<Item> PALM_LOGS = ItemTags.register(Outvoted.MOD_ID + ":palm_logs");
//        public static final Tag.Identified<Item> BAOBAB_LOGS = ItemTags.register(Outvoted.MOD_ID + ":baobab_logs");
    }

    public static void init() {
        new Blocks();
        new Items();
    }
}

