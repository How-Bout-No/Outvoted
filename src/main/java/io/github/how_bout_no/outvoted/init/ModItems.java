package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.item.WildfireShieldItem;
import io.github.how_bout_no.outvoted.item.ModItem;
import io.github.how_bout_no.outvoted.item.ModdedSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    // Spawn Eggs
    public static final RegistryObject<ModdedSpawnEggItem> WILDFIRE_SPAWN_EGG = ITEMS.register("wildfire_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.WILDFIRE, 0xF6B201, 0x000000, new ModItem.Properties()));
    public static final RegistryObject<ModdedSpawnEggItem> HUNGER_SPAWN_EGG = ITEMS.register("hunger_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.HUNGER, 0xF0D786, 0x000000, new ModItem.Properties()));
    public static final RegistryObject<ModdedSpawnEggItem> KRAKEN_SPAWN_EGG = ITEMS.register("kraken_spawn_egg",
            () -> new ModdedSpawnEggItem(ModEntityTypes.KRAKEN, 0x5B872E, 0x000000, new ModItem.Properties()));

    // Items
    public static final RegistryObject<Item> WILDFIRE_HELMET = ITEMS.register("wildfire_helmet", WildfireHelmetItem::new);
    public static final RegistryObject<Item> WILDFIRE_SHIELD = ITEMS.register("wildfire_shield", WildfireShieldItem::new);
    public static final RegistryObject<Item> WILDFIRE_SHIELD_PART = ITEMS.register("wildfire_shield_part", () -> new ModItem(new Item.Properties().isImmuneToFire()));
    public static final RegistryObject<Item> WILDFIRE_PIECE = ITEMS.register("wildfire_piece", () -> new ModItem(new Item.Properties()));
    public static final RegistryObject<Item> VOID_HEART = ITEMS.register("void_heart", () -> new ModItem(new Item.Properties()));
    public static final RegistryObject<Item> KRAKEN_TOOTH = ITEMS.register("kraken_tooth", () -> new ModItem(new Item.Properties()));
    public static final RegistryObject<Item> PRISMARINE_ROD = ITEMS.register("prismarine_rod", () -> new ModItem(new Item.Properties()));
}
