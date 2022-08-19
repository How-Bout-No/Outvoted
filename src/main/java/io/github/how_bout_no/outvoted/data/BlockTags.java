package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTags extends BlockTagsProvider {
    public BlockTags(final DataGenerator generatorIn, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.GLUTTON_CAN_BURROW).addTag(net.minecraft.tags.BlockTags.SAND)
                .add(Blocks.GRAVEL, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.MYCELIUM);
        tag(net.minecraft.tags.BlockTags.BUTTONS)
                .add(ModBlocks.COPPER_BUTTON.get());
    }
}
