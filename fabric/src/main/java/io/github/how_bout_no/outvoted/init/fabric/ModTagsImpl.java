package io.github.how_bout_no.outvoted.init.fabric;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTagsImpl {
    public static void init() {
        ModTags.Blocks.HUNGER_CAN_BURROW = (Tag.Identified<Block>) TagRegistry.block(new Identifier(Outvoted.MOD_ID, "hunger_can_burrow"));
        ModTags.Blocks.PALM_LOGS = (Tag.Identified<Block>) TagRegistry.block(new Identifier(Outvoted.MOD_ID, "palm_logs"));
        ModTags.Blocks.BAOBAB_LOGS = (Tag.Identified<Block>) TagRegistry.block(new Identifier(Outvoted.MOD_ID, "baobab_logs"));

        ModTags.Items.PALM_LOGS = (Tag.Identified<Item>) TagRegistry.item(new Identifier(Outvoted.MOD_ID, "palm_logs"));
        ModTags.Items.BAOBAB_LOGS = (Tag.Identified<Item>) TagRegistry.item(new Identifier(Outvoted.MOD_ID, "baobab_logs"));
    }
}
