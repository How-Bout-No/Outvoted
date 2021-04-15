package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {
    private static String type = "";

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generated(ModItems.WILDFIRE_SHIELD_PART.get());
        generated(ModItems.WILDFIRE_PIECE.get());
        generated(ModItems.VOID_HEART.get());
        generated(ModItems.KRAKEN_TOOTH.get());
        generated(ModItems.PRISMARINE_ROD.get());
        egg(ModItems.WILDFIRE_SPAWN_EGG.get());
        egg(ModItems.HUNGER_SPAWN_EGG.get());
        egg(ModItems.KRAKEN_SPAWN_EGG.get());

        singleTexture(ModItems.WILDFIRE_HELMET.get().getRegistryName().getPath(), new Identifier("item/generated"),
                "layer0", new Identifier(Outvoted.MOD_ID, "item/wildfire_helmet"))
                .override().predicate(new Identifier(Outvoted.MOD_ID, "soul_texture"), 1).model(new ModelFile(new Identifier(Outvoted.MOD_ID, "item/wildfire_helmet_soul")) {
            @Override
            protected boolean exists() {
                return true;
            }
        });

        generated(ModItems.WILDFIRE_HELMET.get().getRegistryName().getPath() + "_soul");

        singleTexture(ModItems.WILDFIRE_SHIELD.get().getRegistryName().getPath(), new Identifier("item/shield"),
                "particles", new Identifier("block/nether_bricks"))
                .override().predicate(new Identifier("blocking"), 1)
                .model(new ModelFile(new Identifier(Outvoted.MOD_ID, "item/wildfire_shield_blocking")) {
                    @Override
                    protected boolean exists() {
                        return true;
                    }
                });

        singleTexture(ModItems.WILDFIRE_SHIELD.get().getRegistryName().getPath() + "_blocking", new Identifier("item/shield_blocking"),
                "particles", new Identifier("block/nether_bricks"));

        registerWood("palm");
        registerWood("baobab");
    }

    private void generated(String path) {
        singleTexture(path, new Identifier("item/generated"),
                "layer0", new Identifier(Outvoted.MOD_ID, "item/" + path));
    }

    private void generated(Item item) {
        generated(item.getRegistryName().getPath());
    }

    private void egg(Item item) {
        withExistingParent(item.getRegistryName().getPath(), new Identifier("item/template_spawn_egg"));
    }

    private void registerWood(String input) {
        type = input;
        withParent("button", "inventory");
        generatedWood("door");
        withParent("fence", "inventory");
        withParent("fence_gate");
        withParent("leaves");
        withParent("log");
        withParent("planks");
        withParent("pressure_plate");
        generatedWood("sapling", "block/" + type + "/" + type + "_sapling");
        generatedWood("sign");
        withParent("slab");
        withParent("stairs");
        withParent("trapdoor", "bottom");
        withParent("wood");
        withExistingParent("stripped_" + type + "_log", new Identifier(Outvoted.MOD_ID, "block/stripped_" + type + "_log"));
        withExistingParent("stripped_" + type + "_wood", new Identifier(Outvoted.MOD_ID, "block/stripped_" + type + "_wood"));
    }

    private void generatedWood(String suffix) {
        suffix = "_" + suffix;
        singleTexture(type + suffix, new Identifier("item/generated"),
                "layer0", new Identifier(Outvoted.MOD_ID, "item/" + type + suffix));
    }

    private void generatedWood(String suffix, String texture) {
        suffix = "_" + suffix;
        singleTexture(type + suffix, new Identifier("item/generated"),
                "layer0", new Identifier(Outvoted.MOD_ID, texture));
    }

    private void withParent(String suffix) {
        suffix = "_" + suffix;
        withExistingParent(type + suffix, new Identifier(Outvoted.MOD_ID, "block/" + type + suffix));
    }

    private void withParent(String suffix, String suffix2) {
        suffix = "_" + suffix;
        suffix2 = "_" + suffix2;
        withExistingParent(type + suffix, new Identifier(Outvoted.MOD_ID, "block/" + type + suffix + suffix2));
    }
}
