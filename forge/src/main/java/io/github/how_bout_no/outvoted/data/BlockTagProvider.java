package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.tag.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(final DataGenerator generatorIn, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generatorIn, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void configure() {
        getOrCreateTagBuilder(ModTags.Blocks.GLUTTON_CAN_BURROW).addTag(BlockTags.SAND)
                .add(Blocks.GRAVEL, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.MYCELIUM);

        // Outvoted tags
        getOrCreateTagBuilder(ModTags.Blocks.PALM_LOGS)
                .add(ModBlocks.PALM_LOG.get(), ModBlocks.PALM_WOOD.get(), ModBlocks.STRIPPED_PALM_LOG.get(), ModBlocks.STRIPPED_PALM_WOOD.get());
        getOrCreateTagBuilder(ModTags.Blocks.BAOBAB_LOGS)
                .add(ModBlocks.BAOBAB_LOG.get(), ModBlocks.BAOBAB_WOOD.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get(), ModBlocks.STRIPPED_BAOBAB_WOOD.get());

        // Vanilla tags
        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(ModBlocks.PALM_FENCE_GATE.get())
                .add(ModBlocks.BAOBAB_FENCE_GATE.get());
        getOrCreateTagBuilder(BlockTags.LEAVES)
                .add(ModBlocks.PALM_LEAVES.get())
                .add(ModBlocks.BAOBAB_LEAVES.get());
        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .addTag(ModTags.Blocks.PALM_LOGS)
                .addTag(ModTags.Blocks.BAOBAB_LOGS);
        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(ModBlocks.PALM_PLANKS.get())
                .add(ModBlocks.BAOBAB_PLANKS.get());
        getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(ModBlocks.PALM_SAPLING.get())
                .add(ModBlocks.BAOBAB_SAPLING.get());
        getOrCreateTagBuilder(BlockTags.STANDING_SIGNS)
                .add(ModBlocks.PALM_SIGN.get())
                .add(ModBlocks.BAOBAB_SIGN.get());
        getOrCreateTagBuilder(BlockTags.WALL_SIGNS)
                .add(ModBlocks.PALM_WALL_SIGN.get())
                .add(ModBlocks.BAOBAB_WALL_SIGN.get());
        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.PALM_BUTTON.get())
                .add(ModBlocks.BAOBAB_BUTTON.get());
        getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.PALM_BUTTON.get())
                .add(ModBlocks.BAOBAB_BUTTON.get());
        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.PALM_FENCE.get())
                .add(ModBlocks.BAOBAB_FENCE.get());
        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.PALM_PRESSURE_PLATE.get())
                .add(ModBlocks.BAOBAB_PRESSURE_PLATE.get());
        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.PALM_SLAB.get())
                .add(ModBlocks.BAOBAB_SLAB.get());
        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.PALM_STAIRS.get())
                .add(ModBlocks.BAOBAB_STAIRS.get());
        getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.PALM_TRAPDOOR.get())
                .add(ModBlocks.BAOBAB_TRAPDOOR.get());

        // Forge tags
        getOrCreateTagBuilder(BlockTags.register("forge:logs"))
                .addTag(ModTags.Blocks.PALM_LOGS)
                .addTag(ModTags.Blocks.BAOBAB_LOGS);
        getOrCreateTagBuilder(Tags.Blocks.FENCES_WOODEN)
                .add(ModBlocks.PALM_FENCE.get())
                .add(ModBlocks.BAOBAB_FENCE.get());
        getOrCreateTagBuilder(Tags.Blocks.FENCE_GATES_WOODEN)
                .add(ModBlocks.PALM_FENCE_GATE.get())
                .add(ModBlocks.BAOBAB_FENCE_GATE.get());
    }
}
