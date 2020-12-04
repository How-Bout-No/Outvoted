package com.hbn.outvoted.data;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.init.ModItems;
import com.hbn.outvoted.items.InfernoHelmetItem;
import com.hbn.outvoted.items.ModItem;
import com.hbn.outvoted.items.ModdedSpawnEggItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
//        ModItems.ITEMS.getEntries().forEach(item -> singleTexture(item.get().getRegistryName().getPath(), new ResourceLocation("item/generated"), "layer0",
//                new ResourceLocation(Outvoted.MOD_ID, "item/" + item.get().getRegistryName())));
        withExistingParent(ModItems.INFERNO_SPAWN_EGG.get().getRegistryName().getPath(), new ResourceLocation("item/template_spawn_egg"));
        withExistingParent(ModItems.HUNGER_SPAWN_EGG.get().getRegistryName().getPath(), new ResourceLocation("item/template_spawn_egg"));
        withExistingParent(ModItems.KRAKEN_SPAWN_EGG.get().getRegistryName().getPath(), new ResourceLocation("item/template_spawn_egg"));
        withExistingParent(ModItems.SOUL_BLAZE_SPAWN_EGG.get().getRegistryName().getPath(), new ResourceLocation("item/template_spawn_egg"));

        singleTexture(ModItems.INFERNO_HELMET.get().getRegistryName().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(Outvoted.MOD_ID, "items/inferno_helmet"));
//        singleTexture(ModItems.INFERNO_SHIELD.get().getRegistryName().getPath(), new ResourceLocation("item/generated"),
//                "particle", new ResourceLocation("block/polished_blackstone"));

        ModItems.ITEMS.getEntries().forEach(item -> {
            if (item.get() instanceof ModItem) {
                singleTexture(item.get().getRegistryName().getPath(), new ResourceLocation("item/generated"),
                        "layer0", new ResourceLocation(Outvoted.MOD_ID, "items/" + item.get().getRegistryName().getPath()));
            } else if (item.get() instanceof ModdedSpawnEggItem) {
                withExistingParent(item.get().getRegistryName().getPath(), new ResourceLocation("item/template_spawn_egg"));
            } else if (item.get() instanceof InfernoHelmetItem) {
                singleTexture(item.get().getRegistryName().getPath(), new ResourceLocation("item/generated"),
                        "layer0", new ResourceLocation(Outvoted.MOD_ID, "items/" + item.get().getRegistryName().getPath()));
            }
        });
    }
}
