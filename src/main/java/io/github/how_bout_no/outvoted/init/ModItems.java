package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.InfernoHelmetItem;
import io.github.how_bout_no.outvoted.item.InfernoShieldItem;
import io.github.how_bout_no.outvoted.item.ModItem;
import io.github.how_bout_no.outvoted.item.ModdedSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    // Spawn Eggs
    public static final RegistryObject<ModdedSpawnEggItem> INFERNO_SPAWN_EGG = ITEMS.register("inferno_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.INFERNO, 0xF6B201, 0x000000, new ModItem.Properties()));
    public static final RegistryObject<ModdedSpawnEggItem> HUNGER_SPAWN_EGG = ITEMS.register("hunger_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.HUNGER, 0xF0D786, 0x000000, new ModItem.Properties()));
    public static final RegistryObject<ModdedSpawnEggItem> KRAKEN_SPAWN_EGG = ITEMS.register("kraken_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.KRAKEN, 0x5B872E, 0x000000, new ModItem.Properties()));

    // Items
    public static final RegistryObject<Item> INFERNO_HELMET = ITEMS.register("inferno_helmet", InfernoHelmetItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD = ITEMS.register("inferno_shield", InfernoShieldItem::new);
    public static final RegistryObject<Item> INFERNO_SHIELD_PART = ITEMS.register("inferno_shield_part", () -> new ModItem(new Item.Properties().isImmuneToFire()));
    public static final RegistryObject<Item> INFERNO_PIECE = ITEMS.register("inferno_piece", () -> new ModItem(new Item.Properties()));
    public static final RegistryObject<Item> VOID_HEART = ITEMS.register("void_heart", () -> new ModItem(new Item.Properties()));
    public static final RegistryObject<Item> KRAKEN_TOOTH = ITEMS.register("kraken_tooth", () -> new ModItem(new Item.Properties()));
    public static final RegistryObject<Item> PRISMARINE_ROD = ITEMS.register("prismarine_rod", () -> new ModItem(new Item.Properties()));
}
