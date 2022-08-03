package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.BaseCopperButtonBlock;
import io.github.how_bout_no.outvoted.block.CopperButtonBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Outvoted.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> COPPER_BUTTON = BLOCKS.register("copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).noCollission().strength(0.5F)));
    public static final RegistryObject<Block> EXPOSED_COPPER_BUTTON = BLOCKS.register("exposed_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER).noCollission().strength(0.5F)));
    public static final RegistryObject<Block> WEATHERED_COPPER_BUTTON = BLOCKS.register("weathered_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER).noCollission().strength(0.5F)));
    public static final RegistryObject<Block> OXIDIZED_COPPER_BUTTON = BLOCKS.register("oxidized_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER).noCollission().strength(0.5F)));
    public static final RegistryObject<Block> WAXED_COPPER_BUTTON = BLOCKS.register("waxed_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(COPPER_BUTTON.get())));
    public static final RegistryObject<Block> WAXED_EXPOSED_COPPER_BUTTON = BLOCKS.register("waxed_exposed_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(EXPOSED_COPPER_BUTTON.get())));
    public static final RegistryObject<Block> WAXED_WEATHERED_COPPER_BUTTON = BLOCKS.register("waxed_weathered_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(WEATHERED_COPPER_BUTTON.get())));
    public static final RegistryObject<Block> WAXED_OXIDIZED_COPPER_BUTTON = BLOCKS.register("waxed_oxidized_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(OXIDIZED_COPPER_BUTTON.get())));

    // Block items
    public static final RegistryObject<Item> COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("copper_button", () -> new BlockItem(COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<Item> EXPOSED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("exposed_copper_button", () -> new BlockItem(EXPOSED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<Item> WEATHERED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("weathered_copper_button", () -> new BlockItem(WEATHERED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<Item> OXIDIZED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("oxidized_copper_button", () -> new BlockItem(OXIDIZED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<Item> WAXED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_copper_button", () -> new BlockItem(WAXED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<Item> WAXED_EXPOSED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_exposed_copper_button", () -> new BlockItem(WAXED_EXPOSED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<Item> WAXED_WEATHERED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_weathered_copper_button", () -> new BlockItem(WAXED_WEATHERED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<Item> WAXED_OXIDIZED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_oxidized_copper_button", () -> new BlockItem(WAXED_OXIDIZED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
}
