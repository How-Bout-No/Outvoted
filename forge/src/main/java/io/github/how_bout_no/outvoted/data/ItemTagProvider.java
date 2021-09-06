package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.ItemTagsProvider;
import net.minecraft.tag.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(final DataGenerator generatorIn, BlockTagProvider blockTagProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void configure() {
        // Outvoted tags
        this.copy(ModTags.Blocks.BAOBAB_LOGS, ModTags.Items.BAOBAB_LOGS);
        this.copy(ModTags.Blocks.PALM_LOGS, ModTags.Items.PALM_LOGS);

        // Vanilla tags
        getOrCreateTagBuilder(ItemTags.LEAVES)
                .add(ModBlocks.PALM_LEAVES_ITEM.get())
                .add(ModBlocks.BAOBAB_LEAVES_ITEM.get());
        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .addTag(ModTags.Items.PALM_LOGS)
                .addTag(ModTags.Items.BAOBAB_LOGS);
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(ModBlocks.PALM_PLANKS_ITEM.get())
                .add(ModBlocks.BAOBAB_PLANKS_ITEM.get());
        getOrCreateTagBuilder(ItemTags.SAPLINGS)
                .add(ModBlocks.PALM_SAPLING_ITEM.get())
                .add(ModBlocks.BAOBAB_SAPLING_ITEM.get());
        getOrCreateTagBuilder(ItemTags.SIGNS)
                .add(ModBlocks.PALM_SIGN_ITEM.get())
                .add(ModBlocks.BAOBAB_SIGN_ITEM.get());
        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
                .add(ModBlocks.PALM_BUTTON_ITEM.get())
                .add(ModBlocks.BAOBAB_BUTTON_ITEM.get());
        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
                .add(ModBlocks.PALM_BUTTON_ITEM.get())
                .add(ModBlocks.BAOBAB_BUTTON_ITEM.get());
        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
                .add(ModBlocks.PALM_FENCE_ITEM.get())
                .add(ModBlocks.BAOBAB_FENCE_ITEM.get());
        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.PALM_PRESSURE_PLATE_ITEM.get())
                .add(ModBlocks.BAOBAB_PRESSURE_PLATE_ITEM.get());
        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
                .add(ModBlocks.PALM_SLAB_ITEM.get())
                .add(ModBlocks.BAOBAB_SLAB_ITEM.get());
        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
                .add(ModBlocks.PALM_STAIRS_ITEM.get())
                .add(ModBlocks.BAOBAB_STAIRS_ITEM.get());
        getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.PALM_TRAPDOOR_ITEM.get())
                .add(ModBlocks.BAOBAB_TRAPDOOR_ITEM.get());
    }
}
