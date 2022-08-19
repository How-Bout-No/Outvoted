package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.*;
import io.github.how_bout_no.outvoted.block.grower.BaobabTreeGrower;
import io.github.how_bout_no.outvoted.block.grower.PalmTreeGrower;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Outvoted.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> BURROW = BLOCKS.register("burrow", () -> new BurrowBlock(Properties.copy(Blocks.SAND)));

    public static final RegistryObject<Block> PALM_PLANKS = BLOCKS.register("palm_planks", () -> new Block(Properties.copy(Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<RotatedPillarBlock> PALM_LOG = BLOCKS.register("palm_log", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_LOG)));
    public static final RegistryObject<LeavesBlock> PALM_LEAVES = BLOCKS.register("palm_leaves", () -> new LeavesBlock(Properties.copy(Blocks.ACACIA_LEAVES)));
    public static final RegistryObject<ModSaplingBlock> PALM_SAPLING = BLOCKS.register("palm_sapling", () -> new ModSaplingBlock(new PalmTreeGrower(), Properties.copy(Blocks.ACACIA_SAPLING), Blocks.SAND));
    public static final RegistryObject<RotatedPillarBlock> PALM_WOOD = BLOCKS.register("palm_wood", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_PALM_LOG = BLOCKS.register("stripped_palm_log", () -> new RotatedPillarBlock(Properties.copy(Blocks.STRIPPED_ACACIA_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_PALM_WOOD = BLOCKS.register("stripped_palm_wood", () -> new RotatedPillarBlock(Properties.copy(Blocks.STRIPPED_ACACIA_WOOD)));
    public static final RegistryObject<StairBlock> PALM_STAIRS = BLOCKS.register("palm_stairs", () -> new StairBlock(PALM_PLANKS.get().defaultBlockState(), Properties.copy(Blocks.ACACIA_STAIRS)));
    public static final RegistryObject<SlabBlock> PALM_SLAB = BLOCKS.register("palm_slab", () -> new SlabBlock(Properties.copy(Blocks.ACACIA_SLAB)));
    public static final RegistryObject<ButtonBlock> PALM_BUTTON = BLOCKS.register("palm_button", () -> new WoodButtonBlock(Properties.copy(Blocks.ACACIA_BUTTON)));
    public static final RegistryObject<PressurePlateBlock> PALM_PRESSURE_PLATE = BLOCKS.register("palm_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, Properties.copy(Blocks.ACACIA_FENCE_GATE)));
    public static final RegistryObject<FenceBlock> PALM_FENCE = BLOCKS.register("palm_fence", () -> new FenceBlock(Properties.copy(Blocks.ACACIA_FENCE)));
    public static final RegistryObject<FenceGateBlock> PALM_FENCE_GATE = BLOCKS.register("palm_fence_gate", () -> new FenceGateBlock(Properties.copy(Blocks.ACACIA_FENCE_GATE)));
    public static final RegistryObject<TrapDoorBlock> PALM_TRAPDOOR = BLOCKS.register("palm_trapdoor", () -> new TrapDoorBlock(Properties.copy(Blocks.ACACIA_TRAPDOOR)));
    public static final RegistryObject<DoorBlock> PALM_DOOR = BLOCKS.register("palm_door", () -> new DoorBlock(Properties.copy(Blocks.ACACIA_DOOR)));
    public static final RegistryObject<ModSignBlock.ModStandingSignBlock> PALM_SIGN = BLOCKS.register("palm_sign", () -> new ModSignBlock.ModStandingSignBlock(Properties.copy(Blocks.ACACIA_SIGN), ModWoodType.PALM));
    public static final RegistryObject<ModSignBlock.ModWallSignBlock> PALM_WALL_SIGN = BLOCKS.register("palm_wall_sign", () -> new ModSignBlock.ModWallSignBlock(Properties.copy(Blocks.ACACIA_WALL_SIGN), ModWoodType.PALM));

    public static final RegistryObject<RotatedPillarBlock> BAOBAB_LOG = BLOCKS.register("baobab_log", () -> createLogBlock(MaterialColor.COLOR_ORANGE, MaterialColor.STONE));
    public static final RegistryObject<LeavesBlock> BAOBAB_LEAVES = BLOCKS.register("baobab_leaves", () -> new LeavesBlock(Properties.copy(Blocks.ACACIA_LEAVES)));
    public static final RegistryObject<ModSaplingBlock> BAOBAB_SAPLING = BLOCKS.register("baobab_sapling", () -> new ModSaplingBlock(new BaobabTreeGrower(), Properties.copy(Blocks.ACACIA_SAPLING)));
    public static final RegistryObject<RotatedPillarBlock> BAOBAB_WOOD = BLOCKS.register("baobab_wood", () -> new RotatedPillarBlock(Properties.copy(Blocks.ACACIA_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_BAOBAB_LOG = BLOCKS.register("stripped_baobab_log", () -> new RotatedPillarBlock(Properties.copy(Blocks.STRIPPED_ACACIA_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_BAOBAB_WOOD = BLOCKS.register("stripped_baobab_wood", () -> new RotatedPillarBlock(Properties.copy(Blocks.STRIPPED_ACACIA_WOOD)));

    public static final RegistryObject<ButtonBlock> COPPER_BUTTON = BLOCKS.register("copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, Properties.copy(Blocks.COPPER_BLOCK).noCollission().strength(0.5F)));
    public static final RegistryObject<ButtonBlock> EXPOSED_COPPER_BUTTON = BLOCKS.register("exposed_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, Properties.copy(Blocks.EXPOSED_COPPER).noCollission().strength(0.5F)));
    public static final RegistryObject<ButtonBlock> WEATHERED_COPPER_BUTTON = BLOCKS.register("weathered_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, Properties.copy(Blocks.WEATHERED_COPPER).noCollission().strength(0.5F)));
    public static final RegistryObject<ButtonBlock> OXIDIZED_COPPER_BUTTON = BLOCKS.register("oxidized_copper_button", () -> new CopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, Properties.copy(Blocks.OXIDIZED_COPPER).noCollission().strength(0.5F)));
    public static final RegistryObject<ButtonBlock> WAXED_COPPER_BUTTON = BLOCKS.register("waxed_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.UNAFFECTED, Properties.copy(COPPER_BUTTON.get())));
    public static final RegistryObject<ButtonBlock> WAXED_EXPOSED_COPPER_BUTTON = BLOCKS.register("waxed_exposed_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.EXPOSED, Properties.copy(EXPOSED_COPPER_BUTTON.get())));
    public static final RegistryObject<ButtonBlock> WAXED_WEATHERED_COPPER_BUTTON = BLOCKS.register("waxed_weathered_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.WEATHERED, Properties.copy(WEATHERED_COPPER_BUTTON.get())));
    public static final RegistryObject<ButtonBlock> WAXED_OXIDIZED_COPPER_BUTTON = BLOCKS.register("waxed_oxidized_copper_button", () -> new BaseCopperButtonBlock(WeatheringCopper.WeatherState.OXIDIZED, Properties.copy(OXIDIZED_COPPER_BUTTON.get())));

    private static RotatedPillarBlock createLogBlock(MaterialColor topColor, MaterialColor barkColor) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, (state) -> {
            return state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor;
        }).strength(2.0F).sound(SoundType.WOOD));
    }

    // Block items
    public static final RegistryObject<BlockItem> BURROW_ITEM = BLOCK_ITEMS.register("burrow", () -> new BlockItem(BURROW.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_PLANKS_ITEM = BLOCK_ITEMS.register("palm_planks", () -> new BlockItem(PALM_PLANKS.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_LOG_ITEM = BLOCK_ITEMS.register("palm_log", () -> new BlockItem(PALM_LOG.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_LEAVES_ITEM = BLOCK_ITEMS.register("palm_leaves", () -> new BlockItem(PALM_LEAVES.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_SAPLING_ITEM = BLOCK_ITEMS.register("palm_sapling", () -> new BlockItem(PALM_SAPLING.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_WOOD_ITEM = BLOCK_ITEMS.register("palm_wood", () -> new BlockItem(PALM_WOOD.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> STRIPPED_PALM_LOG_ITEM = BLOCK_ITEMS.register("stripped_palm_log", () -> new BlockItem(STRIPPED_PALM_LOG.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> STRIPPED_PALM_WOOD_ITEM = BLOCK_ITEMS.register("stripped_palm_wood", () -> new BlockItem(STRIPPED_PALM_WOOD.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_STAIRS_ITEM = BLOCK_ITEMS.register("palm_stairs", () -> new BlockItem(PALM_STAIRS.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_SLAB_ITEM = BLOCK_ITEMS.register("palm_slab", () -> new BlockItem(PALM_SLAB.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_BUTTON_ITEM = BLOCK_ITEMS.register("palm_button", () -> new BlockItem(PALM_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("palm_pressure_plate", () -> new BlockItem(PALM_PRESSURE_PLATE.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_FENCE_ITEM = BLOCK_ITEMS.register("palm_fence", () -> new BlockItem(PALM_FENCE.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_FENCE_GATE_ITEM = BLOCK_ITEMS.register("palm_fence_gate", () -> new BlockItem(PALM_FENCE_GATE.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_TRAPDOOR_ITEM = BLOCK_ITEMS.register("palm_trapdoor", () -> new BlockItem(PALM_TRAPDOOR.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> PALM_DOOR_ITEM = BLOCK_ITEMS.register("palm_door", () -> new BlockItem(PALM_DOOR.get(), ModItems.ITEM_PROPERTIES.stacksTo(16)));
    public static final RegistryObject<SignItem> PALM_SIGN_ITEM = BLOCK_ITEMS.register("palm_sign", () -> new SignItem(ModItems.ITEM_PROPERTIES.stacksTo(16), PALM_SIGN.get(), PALM_WALL_SIGN.get()));
    public static final RegistryObject<BlockItem> BAOBAB_LOG_ITEM = BLOCK_ITEMS.register("baobab_log", () -> new BlockItem(BAOBAB_LOG.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> BAOBAB_LEAVES_ITEM = BLOCK_ITEMS.register("baobab_leaves", () -> new BlockItem(BAOBAB_LEAVES.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> BAOBAB_SAPLING_ITEM = BLOCK_ITEMS.register("baobab_sapling", () -> new BlockItem(BAOBAB_SAPLING.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> BAOBAB_WOOD_ITEM = BLOCK_ITEMS.register("baobab_wood", () -> new BlockItem(BAOBAB_WOOD.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> STRIPPED_BAOBAB_LOG_ITEM = BLOCK_ITEMS.register("stripped_baobab_log", () -> new BlockItem(STRIPPED_BAOBAB_LOG.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> STRIPPED_BAOBAB_WOOD_ITEM = BLOCK_ITEMS.register("stripped_baobab_wood", () -> new BlockItem(STRIPPED_BAOBAB_WOOD.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("copper_button", () -> new BlockItem(COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> EXPOSED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("exposed_copper_button", () -> new BlockItem(EXPOSED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> WEATHERED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("weathered_copper_button", () -> new BlockItem(WEATHERED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> OXIDIZED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("oxidized_copper_button", () -> new BlockItem(OXIDIZED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> WAXED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_copper_button", () -> new BlockItem(WAXED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> WAXED_EXPOSED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_exposed_copper_button", () -> new BlockItem(WAXED_EXPOSED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> WAXED_WEATHERED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_weathered_copper_button", () -> new BlockItem(WAXED_WEATHERED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
    public static final RegistryObject<BlockItem> WAXED_OXIDIZED_COPPER_BUTTON_ITEM = BLOCK_ITEMS.register("waxed_oxidized_copper_button", () -> new BlockItem(WAXED_OXIDIZED_COPPER_BUTTON.get(), ModItems.ITEM_PROPERTIES));
}
