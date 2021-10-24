package io.github.how_bout_no.outvoted.forge.data;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(final DataGenerator generatorIn, BlockTagProvider blockTagProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void configure() {
//        this.copy(ModTags.Blocks.BAOBAB_LOGS, ModTags.Items.BAOBAB_LOGS);
//        this.copy(ModTags.Blocks.PALM_LOGS, ModTags.Items.PALM_LOGS);
    }
}
