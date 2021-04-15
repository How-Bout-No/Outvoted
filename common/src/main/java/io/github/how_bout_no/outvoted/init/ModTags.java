package io.github.how_bout_no.outvoted.init;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class ModTags {
    public static class Blocks {
        public static Tag.Identified<Block> HUNGER_CAN_BURROW = null;
        public static Tag.Identified<Block> PALM_LOGS = null;
        public static Tag.Identified<Block> BAOBAB_LOGS = null;
    }

    public static class Items {
        public static Tag.Identified<Item> PALM_LOGS = null;
        public static Tag.Identified<Item> BAOBAB_LOGS = null;
    }

    @ExpectPlatform
    public static void init() {
        throw new AssertionError();
    }
}

