package com.hbn.outvoted.data;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.init.ModItems;
import com.hbn.outvoted.items.ModItem;
import com.hbn.outvoted.items.ModdedSpawnEggItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(item -> {
            if (item.get() instanceof ModItem) {
                singleTexture(item.get().getRegistryName().getPath(), new ResourceLocation("item/generated"),
                        "layer0", new ResourceLocation(Outvoted.MOD_ID, "items/" + item.get().getRegistryName().getPath()));
            } else if (item.get() instanceof ModdedSpawnEggItem) {
                withExistingParent(item.get().getRegistryName().getPath(), new ResourceLocation("item/template_spawn_egg"));
            }
        });

        singleTexture(ModItems.INFERNO_HELMET.get().getRegistryName().getPath(), new ResourceLocation("item/generated"),
                "layer0", new ResourceLocation(Outvoted.MOD_ID, "items/" + ModItems.INFERNO_HELMET.get().getRegistryName().getPath()));

        singleTexture(ModItems.INFERNO_SHIELD.get().getRegistryName().getPath(), new ResourceLocation("item/shield"),
                "particles", new ResourceLocation("block/nether_bricks"))
                .override().predicate(new ResourceLocation(Outvoted.MOD_ID, "blocking"), 1)
                .model(new ModelFile(new ResourceLocation(Outvoted.MOD_ID, "item/inferno_shield_blocking")) {
                    @Override
                    protected boolean exists() {
                        return true;
                    }
                });

        singleTexture(ModItems.INFERNO_SHIELD.get().getRegistryName().getPath() + "_blocking", new ResourceLocation("item/shield_blocking"),
                "particles", new ResourceLocation("block/nether_bricks"));
    }
}
