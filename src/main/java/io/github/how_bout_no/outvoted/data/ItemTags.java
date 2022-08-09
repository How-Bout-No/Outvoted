package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(final DataGenerator generatorIn, BlockTags blockTagProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.copy(ModTags.BAOBAB_LOGS, ModTags.BAOBAB_LOGS_ITEM);
        this.copy(ModTags.PALM_LOGS, ModTags.PALM_LOGS_ITEM);
    }
}
