package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Outvoted.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // palm
        buttonBlock(ModBlocks.PALM_BUTTON.get(), id("block/palm_planks"));
        doorBlock(ModBlocks.PALM_DOOR.get(), id("block/palm_door_bottom"), id("block/palm_door_top"));
        fenceGateBlock(ModBlocks.PALM_FENCE_GATE.get(), id("block/palm_planks"));
        fenceBlock(ModBlocks.PALM_FENCE.get(), id("block/palm_planks"));
        simpleBlock(ModBlocks.PALM_LEAVES.get());
        logBlock(ModBlocks.PALM_LOG.get());
        logBlock(ModBlocks.STRIPPED_PALM_LOG.get());
        axisBlock(ModBlocks.PALM_WOOD.get(), id("block/palm_log"), id("block/palm_log"));
        axisBlock(ModBlocks.STRIPPED_PALM_WOOD.get(), id("block/stripped_palm_log"), id("block/stripped_palm_log"));
        simpleBlock(ModBlocks.PALM_PLANKS.get());
        pressurePlateBlock(ModBlocks.PALM_PRESSURE_PLATE.get(), id("block/palm_planks"));
        simpleBlock(ModBlocks.PALM_SAPLING.get(), this.models().cross("palm_sapling", id("block/palm_sapling")));
        slabBlock(ModBlocks.PALM_SLAB.get(), id("block/palm_planks"), id("block/palm_planks"));
        stairsBlock(ModBlocks.PALM_STAIRS.get(), id("block/palm_planks"));
        trapdoorBlock(ModBlocks.PALM_TRAPDOOR.get(), id("block/palm_trapdoor"), true);
        signBlock(ModBlocks.PALM_SIGN.get(),
                ModBlocks.PALM_WALL_SIGN.get(), id("block/palm_planks"));
        // baobab
        simpleBlock(ModBlocks.BAOBAB_LEAVES.get());
        logBlock(ModBlocks.BAOBAB_LOG.get());
        logBlock(ModBlocks.STRIPPED_BAOBAB_LOG.get());
        axisBlock(ModBlocks.BAOBAB_WOOD.get(), id("block/baobab_log"), id("block/baobab_log"));
        axisBlock(ModBlocks.STRIPPED_BAOBAB_WOOD.get(), id("block/stripped_baobab_log"), id("block/stripped_baobab_log"));
        simpleBlock(ModBlocks.BAOBAB_SAPLING.get(), this.models().cross("baobab_sapling", id("block/baobab_sapling")));
        directionalBlock(ModBlocks.BURROW.get(), this.models().orientableVertical("burrow", new ResourceLocation("block/sand"), new ResourceLocation(Outvoted.MOD_ID, "block/burrow")));
        buttonBlock(ModBlocks.COPPER_BUTTON.get(), new ResourceLocation("block/copper_block"));
        buttonBlock(ModBlocks.EXPOSED_COPPER_BUTTON.get(), new ResourceLocation("block/exposed_copper"));
        buttonBlock(ModBlocks.WEATHERED_COPPER_BUTTON.get(), new ResourceLocation("block/weathered_copper"));
        buttonBlock(ModBlocks.OXIDIZED_COPPER_BUTTON.get(), new ResourceLocation("block/oxidized_copper"));
        buttonBlock(ModBlocks.WAXED_COPPER_BUTTON.get(), new ResourceLocation("block/copper_block"));
        buttonBlock(ModBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(), new ResourceLocation("block/exposed_copper"));
        buttonBlock(ModBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(), new ResourceLocation("block/weathered_copper"));
        buttonBlock(ModBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get(), new ResourceLocation("block/oxidized_copper"));
    }

    private ResourceLocation id(String path) {
        return new ResourceLocation(Outvoted.MOD_ID, path);
    }
}
