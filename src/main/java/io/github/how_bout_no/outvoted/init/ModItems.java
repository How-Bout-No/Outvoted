package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import io.github.how_bout_no.outvoted.item.WildfireShieldItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(Outvoted.TAB);

    // Spawn Eggs
    public static final RegistryObject<ForgeSpawnEggItem> WILDFIRE_SPAWN_EGG = ITEMS.register("wildfire_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WILDFIRE, 0xF6B201, 0x000000, ITEM_PROPERTIES));
    public static final RegistryObject<ForgeSpawnEggItem> GLUTTON_SPAWN_EGG = ITEMS.register("glutton_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GLUTTON, 0xF0D786, 0x000000, ITEM_PROPERTIES));
    public static final RegistryObject<ForgeSpawnEggItem> BARNACLE_SPAWN_EGG = ITEMS.register("barnacle_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BARNACLE, 0x5B872E, 0x000000, ITEM_PROPERTIES));
    public static final RegistryObject<ForgeSpawnEggItem> GLARE_SPAWN_EGG = ITEMS.register("glare_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GLARE, 0x325F03, 0x97D756, ITEM_PROPERTIES));
    public static final RegistryObject<ForgeSpawnEggItem> MEERKAT_SPAWN_EGG = ITEMS.register("meerkat_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MEERKAT, 0xC19773, 0x000000, ITEM_PROPERTIES));
    public static final RegistryObject<ForgeSpawnEggItem> OSTRICH_SPAWN_EGG = ITEMS.register("ostrich_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.OSTRICH, 0xFED39B, 0x000000, ITEM_PROPERTIES));

    // Items
    public static final RegistryObject<Item> WILDFIRE_HELMET = ITEMS.register("wildfire_helmet", WildfireHelmetItem::new);
    public static final RegistryObject<Item> WILDFIRE_SHIELD = ITEMS.register("wildfire_shield", WildfireShieldItem::new);
    public static final RegistryObject<Item> WILDFIRE_SHIELD_PART = ITEMS.register("wildfire_shield_part", () -> new Item(ITEM_PROPERTIES.fireResistant()));
    public static final RegistryObject<Item> WILDFIRE_PIECE = ITEMS.register("wildfire_piece", () -> new Item(ITEM_PROPERTIES.fireResistant()));
    public static final RegistryObject<Item> VOID_HEART = ITEMS.register("void_heart", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> BARNACLE_TOOTH = ITEMS.register("barnacle_tooth", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> PRISMARINE_ROD = ITEMS.register("prismarine_rod", () -> new Item(ITEM_PROPERTIES));
    public static final RegistryObject<Item> BAOBAB_FRUIT = ITEMS.register("baobab_fruit", () -> new Item(ITEM_PROPERTIES.food(ModFoods.BAOBAB)));
}
