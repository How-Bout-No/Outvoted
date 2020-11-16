package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.config.OutvotedConfig;
import com.hbn.outvoted.items.InfernoHelmetItem;
import com.hbn.outvoted.items.InfernoShieldItem;
import com.hbn.outvoted.items.ModdedSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);
    public static ItemGroup tab = OutvotedConfig.COMMON.creativetab.get() ? Outvoted.TAB : ItemGroup.MISC;

    public static final RegistryObject<ModdedSpawnEggItem> INFERNO_SPAWN_EGG = ITEMS.register("inferno_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.INFERNO, 16167425, 0x000000, new Item.Properties().group(tab)));
    public static final RegistryObject<ModdedSpawnEggItem> HUNGER_SPAWN_EGG = ITEMS.register("hunger_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.HUNGER, 0xF0D786, 0x000000, new Item.Properties().group(tab)));
    public static final RegistryObject<ModdedSpawnEggItem> KRAKEN_SPAWN_EGG = ITEMS.register("kraken_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.KRAKEN, 0x5B872E, 0x000000, new Item.Properties().group(tab)));

    public static final RegistryObject<Item> INFERNO_HELMET = ITEMS.register("inferno_helmet", InfernoHelmetItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD = ITEMS.register("inferno_shield", InfernoShieldItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD_PART = ITEMS.register("inferno_shield_part", () -> new Item(new Item.Properties().group(tab).isImmuneToFire()));
    public static final RegistryObject<Item> INFERNO_PIECE = ITEMS.register("inferno_piece", () -> new Item(new Item.Properties().group(tab).isImmuneToFire()));
    public static final RegistryObject<Item> VOID_HEART = ITEMS.register("void_heart", () -> new Item(new Item.Properties().group(tab)));
    //public static final RegistryObject<Item> KRAKEN_TOOTH = ITEMS.register("kraken_tooth", () -> new Item(new Item.Properties().group(tab)));
    //public static final RegistryObject<Item> PRISMARINE_ROD = ITEMS.register("prismarine_rod", () -> new Item(new Item.Properties().group(tab)));
}
