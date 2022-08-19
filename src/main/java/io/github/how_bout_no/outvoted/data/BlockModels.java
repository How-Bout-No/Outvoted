package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModels extends BlockModelProvider {
    private static String type = "";
    private static String textureBase = "";

    public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        tree("palm");
        ResourceLocation planks = id("block/palm_planks");
        cubeAll("palm_planks", planks);
        buttonModel("palm_button", planks);
        doorModel("palm_door");
        fenceGateModel("palm_fence_gate", planks);
        fenceModel("palm_fence", planks);
        pressurePlateModel("palm_pressure_plate", planks);
        sign("palm_sign", planks);
        slabModel("palm_slab", planks);
        stairsModel("palm_stairs", planks);
        trapdoorModel("palm_trapdoor", id("block/palm_trapdoor"));

        tree("baobab");

        cubeTop("burrow", new ResourceLocation("block/sand"), id("block/burrow"));
        buttonModel("copper_button", new ResourceLocation("block/copper_block"));
        buttonModel("exposed_copper_button", new ResourceLocation("block/exposed_copper"));
        buttonModel("weathered_copper_button", new ResourceLocation("block/weathered_copper"));
        buttonModel("oxidized_copper_button", new ResourceLocation("block/oxidized_copper"));
        buttonModel("waxed_copper_button", new ResourceLocation("block/copper_block"));
        buttonModel("waxed_exposed_copper_button", new ResourceLocation("block/exposed_copper"));
        buttonModel("waxed_weathered_copper_button", new ResourceLocation("block/weathered_copper"));
        buttonModel("waxed_oxidized_copper_button", new ResourceLocation("block/oxidized_copper"));
    }

    private ResourceLocation id(String path) {
        return new ResourceLocation(Outvoted.MOD_ID, path);
    }

    private void tree(String path) {
        String log = path + "_log";
        cross(path + "_sapling", id("block/" + path + "_sapling"));
        singleTexture(path + "_leaves", new ResourceLocation("block/leaves"), "all", id("block/" + path + "_leaves"));
        cubeColumn(log, id("block/" + log), id("block/" + log + "_top"));
        cubeColumn(path + "_wood", id("block/" + log), id("block/" + log));
        strippedModels(path, id("block/" + log));
    }

    private void strippedModels(String path, ResourceLocation texture) {
        cubeColumn(path + "_log", texture, id(texture.getPath() + "_top"));
        cubeColumnHorizontal(path + "_log_horizontal", texture, id(texture.getPath() + "_top"));
        cubeColumn(path + "_wood", texture, texture);
    }

    private void buttonModel(String path, ResourceLocation texture) {
        button(path, texture);
        buttonInventory(path + "_inventory", texture);
        buttonPressed(path + "_pressed", texture);
    }

    private void doorModel(String path) {
        ResourceLocation bottom = id("block/" + path + "_bottom");
        ResourceLocation top = id("block/" + path + "_top");
        doorTopLeft(path + "_top", bottom, top);
        doorTopRight(path + "_top_hinge", bottom, top);
        doorBottomLeft(path + "_bottom", bottom, top);
        doorBottomRight(path + "_bottom_hinge", bottom, top);
    }

    private void fenceGateModel(String path, ResourceLocation texture) {
        fenceGate(path, texture);
        fenceGateOpen(path + "_open", texture);
        fenceGateWall(path + "_wall", texture);
        fenceGateWallOpen(path + "_wall_open", texture);
    }

    private void fenceModel(String path, ResourceLocation texture) {
        fencePost(path + "_post", texture);
        fenceSide(path + "_side", texture);
        fenceInventory(path + "_inventory", texture);
    }

    private void pressurePlateModel(String path, ResourceLocation texture) {
        pressurePlate(path, texture);
        pressurePlateDown(path + "_down", texture);
    }

    private void slabModel(String path, ResourceLocation texture) {
        slab(path, texture, texture, texture);
        slabTop(path + "_top", texture, texture, texture);
    }

    private void stairsModel(String path, ResourceLocation texture) {
        stairs(path, texture, texture, texture);
        stairsInner(path + "_inner", texture, texture, texture);
        stairsOuter(path + "_outer", texture, texture, texture);
    }

    private void trapdoorModel(String path, ResourceLocation texture) {
        trapdoorBottom(path + "_bottom", texture);
        trapdoorOpen(path + "_open", texture);
        trapdoorTop(path + "_top", texture);
    }
}
