package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;

public class ModTags {
    public static class Blocks {
        public static final Tag.Identified<Block> GLUTTON_CAN_BURROW = BlockTags.register(Outvoted.MOD_ID + ":glutton_can_burrow");
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

