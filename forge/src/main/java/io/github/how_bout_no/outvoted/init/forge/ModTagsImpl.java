package io.github.how_bout_no.outvoted.init.forge;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;

public class ModTagsImpl {
    public static void init() {
        ModTags.Blocks.HUNGER_CAN_BURROW = BlockTags.register(Outvoted.MOD_ID + ":hunger_can_burrow");
        ModTags.Blocks.PALM_LOGS = BlockTags.register(Outvoted.MOD_ID + ":palm_logs");
        ModTags.Blocks.BAOBAB_LOGS = BlockTags.register(Outvoted.MOD_ID + ":baobab_logs");

        ModTags.Items.PALM_LOGS = ItemTags.register(Outvoted.MOD_ID + ":palm_logs");
        ModTags.Items.BAOBAB_LOGS = ItemTags.register(Outvoted.MOD_ID + ":baobab_logs");
    }
}

