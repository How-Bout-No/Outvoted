package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.ModItem;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.item.WildfireShieldItem;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Outvoted.MOD_ID, Registry.ITEM_KEY);

    // Spawn Eggs
    public static final RegistrySupplier<ModSpawnEggItem> WILDFIRE_SPAWN_EGG = ITEMS.register("wildfire_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.WILDFIRE, 0xF6B201, 0x000000, new Item.Settings()));
    public static final RegistrySupplier<ModSpawnEggItem> HUNGER_SPAWN_EGG = ITEMS.register("hunger_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.HUNGER, 0xF0D786, 0x000000, new Item.Settings()));
    public static final RegistrySupplier<ModSpawnEggItem> KRAKEN_SPAWN_EGG = ITEMS.register("kraken_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.KRAKEN, 0x5B872E, 0x000000, new Item.Settings()));
    public static final RegistrySupplier<ModSpawnEggItem> MEERKAT_SPAWN_EGG = ITEMS.register("meerkat_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.MEERKAT, 0xC5C085, 0xFBFDD5, new Item.Settings()));

    // Items
    public static final RegistrySupplier<Item> WILDFIRE_HELMET = ITEMS.register("wildfire_helmet", WildfireHelmetItem::new);
    public static final RegistrySupplier<Item> WILDFIRE_SHIELD = ITEMS.register("wildfire_shield", WildfireShieldItem::new);
    public static final RegistrySupplier<Item> WILDFIRE_SHIELD_PART = ITEMS.register("wildfire_shield_part", () -> new ModItem(new Item.Settings().fireproof()));
    public static final RegistrySupplier<Item> WILDFIRE_PIECE = ITEMS.register("wildfire_piece", () -> new ModItem(new Item.Settings().fireproof()));
    public static final RegistrySupplier<Item> VOID_HEART = ITEMS.register("void_heart", () -> new ModItem(new Item.Settings()));
    public static final RegistrySupplier<Item> KRAKEN_TOOTH = ITEMS.register("kraken_tooth", () -> new ModItem(new Item.Settings()));
    public static final RegistrySupplier<Item> PRISMARINE_ROD = ITEMS.register("prismarine_rod", () -> new ModItem(new Item.Settings()));
}
