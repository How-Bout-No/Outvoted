package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
    private static String type = "";
    private static String textureBase = "";

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Outvoted.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerWood("palm");
        registerWood("baobab");
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

    private void registerWood(String input) {
        type = input;
        textureBase = "block/" + type;
        ResourceLocation loc = new ResourceLocation(Outvoted.MOD_ID, textureBase);
        buttonBlock(input.equals("palm") ? ModBlocks.PALM_BUTTON.get() : ModBlocks.BAOBAB_BUTTON.get(), id("planks"));
        doorBlock(input.equals("palm") ? ModBlocks.PALM_DOOR.get() : ModBlocks.BAOBAB_DOOR.get(), id("door_bottom"), id("door_top"));
        fenceGateBlock(input.equals("palm") ? ModBlocks.PALM_FENCE_GATE.get() : ModBlocks.BAOBAB_FENCE_GATE.get(), id("planks"));
        fenceBlock(input.equals("palm") ? ModBlocks.PALM_FENCE.get() : ModBlocks.BAOBAB_FENCE.get(), id("planks"));
        simpleBlock(input.equals("palm") ? ModBlocks.PALM_LEAVES.get() : ModBlocks.BAOBAB_LEAVES.get());
        logBlock(input.equals("palm") ? ModBlocks.PALM_LOG.get() : ModBlocks.BAOBAB_LOG.get());
        logBlock(input.equals("palm") ? ModBlocks.STRIPPED_PALM_LOG.get() : ModBlocks.STRIPPED_BAOBAB_LOG.get());
        simpleBlock(input.equals("palm") ? ModBlocks.PALM_PLANKS.get() : ModBlocks.BAOBAB_PLANKS.get());
        pressurePlateBlock(input.equals("palm") ? ModBlocks.PALM_PRESSURE_PLATE.get() : ModBlocks.BAOBAB_PRESSURE_PLATE.get(), id("planks"));
        simpleBlock(input.equals("palm") ? ModBlocks.PALM_SAPLING.get() : ModBlocks.BAOBAB_SAPLING.get(), this.models().cross(input + "_sapling", id("sapling")));
        slabBlock(input.equals("palm") ? ModBlocks.PALM_SLAB.get() : ModBlocks.BAOBAB_SLAB.get(), id("planks"), id("planks"));
        stairsBlock(input.equals("palm") ? ModBlocks.PALM_STAIRS.get() : ModBlocks.BAOBAB_STAIRS.get(), id("planks"));
        trapdoorBlock(input.equals("palm") ? ModBlocks.PALM_TRAPDOOR.get() : ModBlocks.BAOBAB_TRAPDOOR.get(), id("trapdoor"), true);
        axisBlock(input.equals("palm") ? ModBlocks.PALM_WOOD.get() : ModBlocks.BAOBAB_WOOD.get(), id("log"), id("log"));
        axisBlock(input.equals("palm") ? ModBlocks.STRIPPED_PALM_WOOD.get() : ModBlocks.STRIPPED_BAOBAB_WOOD.get(), id("stripped", "log"), id("stripped", "log"));
        signBlock(input.equals("palm") ? ModBlocks.PALM_SIGN.get() : ModBlocks.BAOBAB_SIGN.get(),
                input.equals("palm") ? ModBlocks.PALM_WALL_SIGN.get() : ModBlocks.BAOBAB_WALL_SIGN.get(), new ResourceLocation(Outvoted.MOD_ID, "entity/signs/" + type));
    }

    private ResourceLocation id(String suffix) {
        return new ResourceLocation(Outvoted.MOD_ID, textureBase + "_" + suffix);
    }

    private ResourceLocation id(String prefix, String suffix) {
        String newTextureBase = "block/" + prefix + "_" + type;
        return new ResourceLocation(Outvoted.MOD_ID, newTextureBase + "_" + suffix);
    }
}
