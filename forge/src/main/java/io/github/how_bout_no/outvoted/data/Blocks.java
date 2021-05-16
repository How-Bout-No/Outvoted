package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;
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
        registerWood("palm");
        registerWood("baobab");
        orientable("burrow", new Identifier("block/sand"), new Identifier(Outvoted.MOD_ID, "block/burrow"), new Identifier("block/sand"));
    }

    private void registerWood(String input) {
        type = input;
        textureBase = "block/" + type;
        buttonModel();
        doorModel();
        fenceGateModel();
        fenceModel();
        singleTexture(type + "_leaves", new Identifier("block/leaves"), "all", id("_leaves"));
        cubeColumn(type + "_log", id("_log"), id("_log_top"));
        cubeAll(type + "_planks", id("_planks"));
        pressurePlateModel();
        cross(type + "_sapling", id("_sapling"));
//        Can't make sign jsons with this...
//        singleTexture(type + "_sign", new ResourceLocation(Outvoted.MOD_ID, textureBase + "_planks"));
        slabModel();
        stairsModel();
        trapdoorModel();
        cubeColumn(type + "_wood", id("_log"), id("_log"));
        strippedModels();
    }
    
    private Identifier id(String suffix) {
        return new Identifier(Outvoted.MOD_ID, textureBase + suffix);
    }

    private void buttonModel() {
        String path = type + "_button";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        singleTexture(path, new Identifier("block/button"),
                "texture", texture);
        singleTexture(path + "_inventory", new Identifier("block/button_inventory"),
                "texture", texture);
        singleTexture(path + "_pressed", new Identifier("block/button_pressed"),
                "texture", texture);
    }

    private void doorModel() {
        String path = type + "_door";
        Identifier bottom = new Identifier(Outvoted.MOD_ID, textureBase + "_door_bottom");
        Identifier top = new Identifier(Outvoted.MOD_ID, textureBase + "_door_top");
        doorTopLeft(path + "_top", bottom, top);
        doorTopRight(path + "_top_hinge", bottom, top);
        doorBottomLeft(path + "_bottom", bottom, top);
        doorBottomRight(path + "_bottom_hinge", bottom, top);
    }

    private void fenceGateModel() {
        String path = type + "_fence_gate";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        fenceGate(path, texture);
        fenceGateOpen(path + "_open", texture);
        fenceGateWall(path + "_wall", texture);
        fenceGateWallOpen(path + "_wall_open", texture);
    }

    private void fenceModel() {
        String path = type + "_fence";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        fenceInventory(path + "_inventory", texture);
        fencePost(path + "_post", texture);
        fenceSide(path + "_side", texture);
    }

    private void pressurePlateModel() {
        String path = type + "_pressure_plate";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        singleTexture(path, new Identifier("block/pressure_plate_up"),
                "texture", texture);
        singleTexture(path + "_down", new Identifier("block/pressure_plate_down"),
                "texture", texture);
    }

    private void slabModel() {
        String path = type + "_slab";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        slab(path, texture, texture, texture);
        slabTop(path + "_top", texture, texture, texture);
    }

    private void stairsModel() {
        String path = type + "_stairs";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_planks");
        stairs(path, texture, texture, texture);
        stairsInner(path + "_inner", texture, texture, texture);
        stairsOuter(path + "_outer", texture, texture, texture);
    }

    private void trapdoorModel() {
        String path = type + "_trapdoor";
        Identifier texture = new Identifier(Outvoted.MOD_ID, textureBase + "_trapdoor");
        trapdoorBottom(path + "_bottom", texture);
        trapdoorOpen(path + "_open", texture);
        trapdoorTop(path + "_top", texture);
    }

    private void strippedModels() {
        String path = "stripped_" + type;
        String newTextureBase = "block/" + path;
        cubeColumn(path + "_log", new Identifier(Outvoted.MOD_ID, newTextureBase + "_log"), new Identifier(Outvoted.MOD_ID, newTextureBase + "_log_top"));
        cubeColumnHorizontal(path + "_log_horizontal", new Identifier(Outvoted.MOD_ID, newTextureBase + "_log"), new Identifier(Outvoted.MOD_ID, newTextureBase + "_log_top"));
        cubeColumn(path + "_wood", new Identifier(Outvoted.MOD_ID, newTextureBase + "_log"), new Identifier(Outvoted.MOD_ID, newTextureBase + "_log"));
    }
}
