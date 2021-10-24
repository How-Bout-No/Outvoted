package io.github.how_bout_no.outvoted.forge.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Outvoted.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        buttonBlockInternal(ModBlocks.COPPER_BUTTON.get(), "copper_button", new Identifier("block/copper_block"));
        buttonBlockInternal(ModBlocks.EXPOSED_COPPER_BUTTON.get(), "exposed_copper_button", new Identifier("block/exposed_copper"));
        buttonBlockInternal(ModBlocks.WEATHERED_COPPER_BUTTON.get(), "weathered_copper_button", new Identifier("block/weathered_copper"));
        buttonBlockInternal(ModBlocks.OXIDIZED_COPPER_BUTTON.get(), "oxidized_copper_button", new Identifier("block/oxidized_copper"));
        buttonBlockInternal(ModBlocks.WAXED_COPPER_BUTTON.get(), "waxed_copper_button", new Identifier("block/copper_block"));
        buttonBlockInternal(ModBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(), "waxed_exposed_copper_button", new Identifier("block/exposed_copper"));
        buttonBlockInternal(ModBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(), "waxed_weathered_copper_button", new Identifier("block/weathered_copper"));
        buttonBlockInternal(ModBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get(), "waxed_oxidized_copper_button", new Identifier("block/oxidized_copper"));
    }

//    public void buttonBlockInternal(AbstractButtonBlock block) {
//        String path = type + "_button";
//        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
//        this.buttonBlockInternal(block, path, texture);
//    }

    public void buttonBlockInternal(Block block, String path, Identifier texture) {
        ModelFile button = this.models().singleTexture(path, new Identifier("block/button"), "texture", texture);
        ModelFile buttonPressed = this.models().singleTexture(path + "_pressed", new Identifier("block/button_pressed"), "texture", texture);
        this.buttonBlock(block, button, buttonPressed);
    }

    public void buttonBlock(Block block, ModelFile button, ModelFile buttonPressed) {
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

}
