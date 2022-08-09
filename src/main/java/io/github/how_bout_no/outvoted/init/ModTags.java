package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public final class ModTags {
    private ModTags() {}

    //Blocks
    public static final TagKey<Block> GLUTTON_CAN_BURROW = BlockTags.create(new ResourceLocation(Outvoted.MOD_ID, "glutton_can_burrow"));
    public static final TagKey<Block> PALM_LOGS = BlockTags.create(new ResourceLocation(Outvoted.MOD_ID, "palm_logs"));
    public static final TagKey<Block> BAOBAB_LOGS = BlockTags.create(new ResourceLocation(Outvoted.MOD_ID, "baobab_logs"));

    //Items
    public static final TagKey<Item> PALM_LOGS_ITEM = ItemTags.create(new ResourceLocation(Outvoted.MOD_ID, "palm_logs"));
    public static final TagKey<Item> BAOBAB_LOGS_ITEM = ItemTags.create(new ResourceLocation(Outvoted.MOD_ID, "baobab_logs"));

    //Structures
    public static final TagKey<ConfiguredStructureFeature<?, ?>> IN_DESERT = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(Outvoted.MOD_ID, "in_desert"));
}

