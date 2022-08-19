package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static io.github.how_bout_no.outvoted.Outvoted.id;

public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        wood("palm");
        tree("baobab");

        generatedItem("book");
        generatedItem("baobab_fruit");
        generatedItem(ModItems.WILDFIRE_SHIELD_PART);
        generatedItem(ModItems.WILDFIRE_PIECE);
        generatedItem(ModItems.VOID_HEART);
        generatedItem(ModItems.BARNACLE_TOOTH);
        generatedItem(ModItems.PRISMARINE_ROD);
        egg(ModItems.WILDFIRE_SPAWN_EGG);
        egg(ModItems.GLUTTON_SPAWN_EGG);
        egg(ModItems.BARNACLE_SPAWN_EGG);
        egg(ModItems.GLARE_SPAWN_EGG);
        egg(ModItems.MEERKAT_SPAWN_EGG);
        egg(ModItems.OSTRICH_SPAWN_EGG);
        withExistingParent("burrow", id("block/burrow"));
        withExistingParent("copper_button", id("block/copper_button_inventory"));
        withExistingParent("exposed_copper_button", id("block/exposed_copper_button_inventory"));
        withExistingParent("weathered_copper_button", id("block/weathered_copper_button_inventory"));
        withExistingParent("oxidized_copper_button", id("block/oxidized_copper_button_inventory"));
        withExistingParent("waxed_copper_button", id("block/waxed_copper_button_inventory"));
        withExistingParent("waxed_exposed_copper_button", id("block/waxed_exposed_copper_button_inventory"));
        withExistingParent("waxed_weathered_copper_button", id("block/waxed_weathered_copper_button_inventory"));
        withExistingParent("waxed_oxidized_copper_button", id("block/waxed_oxidized_copper_button_inventory"));

        singleTexture(ModItems.WILDFIRE_HELMET.getId().getPath(), new ResourceLocation("item/generated"),
                "layer0", id( "item/wildfire_helmet"))
                .override().predicate(id("soul_texture"), 1).model(new ModelFile(id("item/wildfire_helmet_soul")) {
                    @Override
                    protected boolean exists() {
                        return true;
                    }
                });

        generatedItem(ModItems.WILDFIRE_HELMET.getId().getPath() + "_soul");

        singleTexture(ModItems.WILDFIRE_SHIELD.getId().getPath(), new ResourceLocation("item/shield"),
                "particles", new ResourceLocation("block/nether_bricks"))
                .override().predicate(new ResourceLocation("blocking"), 1)
                .model(new ModelFile(id("item/wildfire_shield_blocking")) {
                    @Override
                    protected boolean exists() {
                        return true;
                    }
                });

        singleTexture(ModItems.WILDFIRE_SHIELD.getId().getPath() + "_blocking", new ResourceLocation("item/shield_blocking"),
                "particles", new ResourceLocation("block/nether_bricks"));
    }

    private void generatedItem(String path) {
        singleTexture(path, new ResourceLocation("item/generated"),
                "layer0", id("item/" + path));
    }

    private void generatedBlock(String path) {
        singleTexture(path, new ResourceLocation("item/generated"),
                "layer0", id("block/" + path));
    }

    private void generatedItem(RegistryObject<? extends Item> item) {
        generatedItem(item.getId().getPath());
    }

    private void egg(RegistryObject<? extends SpawnEggItem> obj) {
        String path = obj.getId().getPath();
        withExistingParent(path, new ResourceLocation("item/template_spawn_egg"));
    }

    private void wood(String path) {
        tree(path);
        generatedItem(path + "_door");
        withExistingParent(path + "_button", id("block/" + path + "_button_inventory"));
        withExistingParent(path + "_fence", id("block/" + path + "_fence_inventory"));
        withExistingParent(path + "_fence_gate", id("block/" + path + "_fence_gate"));
        withExistingParent(path + "_planks", id("block/" + path + "_planks"));
        withExistingParent(path + "_pressure_plate", id("block/" + path + "_pressure_plate"));
        withExistingParent(path + "_slab", id("block/" + path + "_slab"));
        withExistingParent(path + "_stairs", id("block/" + path + "_stairs"));
        withExistingParent(path + "_trapdoor", id("block/" + path + "_trapdoor_bottom"));
        singleTexture(path + "_sign", new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation("item/acacia_sign"));
//        generatedItem(path + "_sign");
    }

    private void tree(String path) {
        generatedBlock(path + "_sapling");
        withExistingParent(path + "_leaves", id("block/" + path + "_leaves"));
        withExistingParent(path + "_log", id("block/" + path + "_log"));
        withExistingParent(path + "_wood", id("block/" + path + "_wood"));
        withExistingParent("stripped_" + path + "_log", id("block/stripped_" + path + "_log"));
        withExistingParent("stripped_" + path + "_wood", id("block/stripped_" + path + "_wood"));
    }
}
