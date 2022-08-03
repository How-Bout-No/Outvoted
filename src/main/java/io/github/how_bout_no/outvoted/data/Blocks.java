package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Blocks extends BlockModelProvider {
    private static String type = "";
    private static String textureBase = "";

    public Blocks(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        buttonModel("copper_button", new ResourceLocation("block/copper_block"));
        buttonModel("exposed_copper_button", new ResourceLocation("block/exposed_copper"));
        buttonModel("weathered_copper_button", new ResourceLocation("block/weathered_copper"));
        buttonModel("oxidized_copper_button", new ResourceLocation("block/oxidized_copper"));
        buttonModel("waxed_copper_button", new ResourceLocation("block/copper_block"));
        buttonModel("waxed_exposed_copper_button", new ResourceLocation("block/exposed_copper"));
        buttonModel("waxed_weathered_copper_button", new ResourceLocation("block/weathered_copper"));
        buttonModel("waxed_oxidized_copper_button", new ResourceLocation("block/oxidized_copper"));
    }

    private void registerWood(String input) {
        type = input;
        textureBase = "block/" + type;
        buttonModel();
        doorModel();
        fenceGateModel();
        fenceModel();
        singleTexture(type + "_leaves", new ResourceLocation("block/leaves"), "all", id("_leaves"));
        cubeColumn(type + "_log", id("_log"), id("_log_top"));
        cubeAll(type + "_planks", id("_planks"));
        pressurePlateModel();
//        cross(type + "_sapling", id("_sapling"));
//        Can't make sign jsons with this...
//        singleTexture(type + "_sign", new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks"));
        slabModel();
        stairsModel();
        trapdoorModel();
        cubeColumn(type + "_wood", id("_log"), id("_log"));
        strippedModels();
    }

    private ResourceLocation id(String suffix) {
        return new ResourceLocation(Outvoted.MOD_ID, textureBase + suffix);
    }

    private void buttonModel() {
        buttonModel(type + "_button", new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks"));
    }

    private void buttonModel(String path, ResourceLocation texture) {
        singleTexture(path, new ResourceLocation("block/button"),
                "texture", texture);
        singleTexture(path + "_inventory", new ResourceLocation("block/button_inventory"),
                "texture", texture);
        singleTexture(path + "_pressed", new ResourceLocation("block/button_pressed"),
                "texture", texture);
    }

    private void doorModel() {
        String path = type + "_door";
        ResourceLocation bottom = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_door_bottom");
        ResourceLocation top = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_door_top");
        doorTopLeft(path + "_top", bottom, top);
        doorTopRight(path + "_top_hinge", bottom, top);
        doorBottomLeft(path + "_bottom", bottom, top);
        doorBottomRight(path + "_bottom_hinge", bottom, top);
    }

    private void fenceGateModel() {
        String path = type + "_fence_gate";
        ResourceLocation texture = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks");
        fenceGate(path, texture);
        fenceGateOpen(path + "_open", texture);
        fenceGateWall(path + "_wall", texture);
        fenceGateWallOpen(path + "_wall_open", texture);
    }

    private void fenceModel() {
        String path = type + "_fence";
        ResourceLocation texture = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks");
        fenceInventory(path + "_inventory", texture);
        fencePost(path + "_post", texture);
        fenceSide(path + "_side", texture);
    }

    private void pressurePlateModel() {
        String path = type + "_pressure_plate";
        ResourceLocation texture = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks");
        singleTexture(path, new ResourceLocation("block/pressure_plate_up"),
                "texture", texture);
        singleTexture(path + "_down", new ResourceLocation("block/pressure_plate_down"),
                "texture", texture);
    }

    private void slabModel() {
        String path = type + "_slab";
        ResourceLocation texture = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks");
        slab(path, texture, texture, texture);
        slabTop(path + "_top", texture, texture, texture);
    }

    private void stairsModel() {
        String path = type + "_stairs";
        ResourceLocation texture = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks");
        stairs(path, texture, texture, texture);
        stairsInner(path + "_inner", texture, texture, texture);
        stairsOuter(path + "_outer", texture, texture, texture);
    }

    private void trapdoorModel() {
        String path = type + "_trapdoor";
        ResourceLocation texture = new ResourceLocation(Outvoted.MOD_ID, textureBase + "_trapdoor");
        trapdoorBottom(path + "_bottom", texture);
        trapdoorOpen(path + "_open", texture);
        trapdoorTop(path + "_top", texture);
    }

    private void strippedModels() {
        String path = "stripped_" + type;
        String newTextureBase = "block/" + path;
        cubeColumn(path + "_log", new ResourceLocation(Outvoted.MOD_ID, newTextureBase + "_log"), new ResourceLocation(Outvoted.MOD_ID, newTextureBase + "_log_top"));
        cubeColumnHorizontal(path + "_log_horizontal", new ResourceLocation(Outvoted.MOD_ID, newTextureBase + "_log"), new ResourceLocation(Outvoted.MOD_ID, newTextureBase + "_log_top"));
        cubeColumn(path + "_wood", new ResourceLocation(Outvoted.MOD_ID, newTextureBase + "_log"), new ResourceLocation(Outvoted.MOD_ID, newTextureBase + "_log"));
    }
}
