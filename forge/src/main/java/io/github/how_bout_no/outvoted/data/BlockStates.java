package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.BurrowBlock;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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
        directionalBlock((BurrowBlock) ModBlocks.BURROW.get(), this.models().orientableVertical("burrow", new Identifier("block/sand"), new Identifier(Outvoted.MOD_ID, "block/burrow")));
    }

    private void registerWood(String input) {
        type = input;
        textureBase = "block/" + type;
        Identifier loc = new Identifier(Outvoted.MOD_ID, textureBase);
        buttonBlockInternal((AbstractButtonBlock) (input.equals("palm") ? ModBlocks.PALM_BUTTON.get() : ModBlocks.BAOBAB_BUTTON.get()));
        doorBlock((DoorBlock) (input.equals("palm") ? ModBlocks.PALM_DOOR.get() : ModBlocks.BAOBAB_DOOR.get()), id("_door_bottom"), id("_door_top"));
        fenceGateBlock((FenceGateBlock) (input.equals("palm") ? ModBlocks.PALM_FENCE_GATE.get() : ModBlocks.BAOBAB_FENCE_GATE.get()), id("_planks"));
        fenceBlock((FenceBlock) (input.equals("palm") ? ModBlocks.PALM_FENCE.get() : ModBlocks.BAOBAB_FENCE.get()), id("_planks"));
        simpleBlock(input.equals("palm") ? ModBlocks.PALM_LEAVES.get() : ModBlocks.BAOBAB_LEAVES.get());
        logBlock((PillarBlock) (input.equals("palm") ? ModBlocks.PALM_LOG.get() : ModBlocks.BAOBAB_LOG.get()));
        logBlock((PillarBlock) (input.equals("palm") ? ModBlocks.STRIPPED_PALM_LOG.get() : ModBlocks.STRIPPED_BAOBAB_LOG.get()));
        simpleBlock(input.equals("palm") ? ModBlocks.PALM_PLANKS.get() : ModBlocks.BAOBAB_PLANKS.get());
        pressurePlateBlockInternal((PressurePlateBlock) (input.equals("palm") ? ModBlocks.PALM_PRESSURE_PLATE.get() : ModBlocks.BAOBAB_PRESSURE_PLATE.get()));
        crossBlock(input.equals("palm") ? ModBlocks.PALM_SAPLING.get() : ModBlocks.BAOBAB_SAPLING.get());
        slabBlock((SlabBlock) (input.equals("palm") ? ModBlocks.PALM_SLAB.get() : ModBlocks.BAOBAB_SLAB.get()), id("_planks"), id("_planks"));
        stairsBlock((StairsBlock) (input.equals("palm") ? ModBlocks.PALM_STAIRS.get() : ModBlocks.BAOBAB_STAIRS.get()), id("_planks"));
        trapdoorBlock((TrapdoorBlock) (input.equals("palm") ? ModBlocks.PALM_TRAPDOOR.get() : ModBlocks.BAOBAB_TRAPDOOR.get()), id("_trapdoor"), true);
        axisBlock((PillarBlock) (input.equals("palm") ? ModBlocks.PALM_WOOD.get() : ModBlocks.BAOBAB_WOOD.get()), id("_log"), id("_log"));
        axisBlock((PillarBlock) (input.equals("palm") ? ModBlocks.STRIPPED_PALM_WOOD.get() : ModBlocks.STRIPPED_BAOBAB_WOOD.get()), id("stripped_", "_log"), id("stripped_", "_log"));
    }
    
    private Identifier id(String suffix) {
        return new Identifier(Outvoted.MOD_ID, textureBase + suffix);
    }

    private Identifier id(String prefix, String suffix) {
        String newTextureBase = "block/" + prefix + type;
        return new Identifier(Outvoted.MOD_ID, newTextureBase + suffix);
    }

    public void buttonBlockInternal(AbstractButtonBlock block) {
        String path = type + "_button";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        ModelFile button = this.models().singleTexture(path, new Identifier("block/button"), "texture", texture);
        ModelFile buttonPressed = this.models().singleTexture(path + "_pressed", new Identifier("block/button_pressed"), "texture", texture);
        this.buttonBlock(block, button, buttonPressed);
    }

    public void buttonBlock(AbstractButtonBlock block, ModelFile button, ModelFile buttonPressed) {
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            int xRot = 0;
            int yRot = (int) ((Direction) state.get(AbstractButtonBlock.FACING)).asRotation() + 180;
            WallMountLocation facing = state.get(AbstractButtonBlock.FACE);
            boolean isPowered = (Boolean) state.get(AbstractButtonBlock.POWERED);
            switch (facing) {
                case WALL:
                    xRot += 90;
                    break;
                case CEILING:
                    xRot += 180;
            }

            yRot %= 360;
            return ConfiguredModel.builder().modelFile(isPowered ? buttonPressed : button).rotationX(xRot).rotationY(yRot).uvLock(facing == WallMountLocation.WALL).build();
        });
    }

    public void pressurePlateBlockInternal(PressurePlateBlock block) {
        String path = type + "_pressure_plate";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        ModelFile pressurePlate = this.models().singleTexture(path, new Identifier("block/pressure_plate_up"), "texture", texture);
        ModelFile pressurePlateDown = this.models().singleTexture(path + "_down", new Identifier("block/pressure_plate_down"), "texture", texture);
        this.pressurePlateBlock(block, pressurePlate, pressurePlateDown);
    }

    public void pressurePlateBlock(PressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown) {
        String path = type + "_pressure_plate";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        this.getVariantBuilder(block).forAllStatesExcept((state) -> {
            boolean isPowered = (Boolean) state.get(PressurePlateBlock.POWERED);

            return ConfiguredModel.builder().modelFile(isPowered ? pressurePlateDown : pressurePlate).build();
        });
    }

    public void crossBlock(Block block) {
        simpleBlock(block, models().cross(block.getRegistryName().getPath(), blockTexture(block)));
    }
}
