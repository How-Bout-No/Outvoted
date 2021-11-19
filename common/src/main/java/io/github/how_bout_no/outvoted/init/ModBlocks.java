package io.github.how_bout_no.outvoted.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.BaseCopperButtonBlock;
import io.github.how_bout_no.outvoted.block.CopperButtonBlock;
import io.github.how_bout_no.outvoted.block.ModBlockItems.ModBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Outvoted.MOD_ID, Registry.BLOCK_KEY);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(Outvoted.MOD_ID, Registry.ITEM_KEY);

    // Blocks
    public static final RegistrySupplier<Block> COPPER_BUTTON = BLOCKS.register("copper_button", () -> new CopperButtonBlock(Oxidizable.OxidizationLevel.UNAFFECTED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).noCollision().strength(0.5F)));
    public static final RegistrySupplier<Block> EXPOSED_COPPER_BUTTON = BLOCKS.register("exposed_copper_button", () -> new CopperButtonBlock(Oxidizable.OxidizationLevel.EXPOSED, AbstractBlock.Settings.copy(Blocks.EXPOSED_COPPER).noCollision().strength(0.5F)));
    public static final RegistrySupplier<Block> WEATHERED_COPPER_BUTTON = BLOCKS.register("weathered_copper_button", () -> new CopperButtonBlock(Oxidizable.OxidizationLevel.WEATHERED, AbstractBlock.Settings.copy(Blocks.WEATHERED_COPPER).noCollision().strength(0.5F)));
    public static final RegistrySupplier<Block> OXIDIZED_COPPER_BUTTON = BLOCKS.register("oxidized_copper_button", () -> new CopperButtonBlock(Oxidizable.OxidizationLevel.OXIDIZED, AbstractBlock.Settings.copy(Blocks.OXIDIZED_COPPER).noCollision().strength(0.5F)));
    public static final RegistrySupplier<Block> WAXED_COPPER_BUTTON = BLOCKS.register("waxed_copper_button", () -> new BaseCopperButtonBlock(Oxidizable.OxidizationLevel.UNAFFECTED, AbstractBlock.Settings.copy(COPPER_BUTTON.get())));
    public static final RegistrySupplier<Block> WAXED_EXPOSED_COPPER_BUTTON = BLOCKS.register("waxed_exposed_copper_button", () -> new BaseCopperButtonBlock(Oxidizable.OxidizationLevel.EXPOSED, AbstractBlock.Settings.copy(EXPOSED_COPPER_BUTTON.get())));
    public static final RegistrySupplier<Block> WAXED_WEATHERED_COPPER_BUTTON = BLOCKS.register("waxed_weathered_copper_button", () -> new BaseCopperButtonBlock(Oxidizable.OxidizationLevel.WEATHERED, AbstractBlock.Settings.copy(WEATHERED_COPPER_BUTTON.get())));
    public static final RegistrySupplier<Block> WAXED_OXIDIZED_COPPER_BUTTON = BLOCKS.register("waxed_oxidized_copper_button", () -> new BaseCopperButtonBlock(Oxidizable.OxidizationLevel.OXIDIZED, AbstractBlock.Settings.copy(OXIDIZED_COPPER_BUTTON.get())));

    // Block items
    public static final RegistrySupplier<Item> COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("copper_button", () -> new ModBlockItem(COPPER_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> EXPOSED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("exposed_copper_button", () -> new ModBlockItem(EXPOSED_COPPER_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> WEATHERED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("weathered_copper_button", () -> new ModBlockItem(WEATHERED_COPPER_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> OXIDIZED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("oxidized_copper_button", () -> new ModBlockItem(OXIDIZED_COPPER_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> WAXED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_copper_button", () -> new ModBlockItem(WAXED_COPPER_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> WAXED_EXPOSED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_exposed_copper_button", () -> new ModBlockItem(WAXED_EXPOSED_COPPER_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> WAXED_WEATHERED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_weathered_copper_button", () -> new ModBlockItem(WAXED_WEATHERED_COPPER_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> WAXED_OXIDIZED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_oxidized_copper_button", () -> new ModBlockItem(WAXED_OXIDIZED_COPPER_BUTTON.get(), new Item.Settings()));
}
