package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.BurrowBlock;
import io.github.how_bout_no.outvoted.block.ModBlockItems.ModBlockItem;
import io.github.how_bout_no.outvoted.block.ModBlockItems.ModDecoBlockItem;
import io.github.how_bout_no.outvoted.block.ModBlockItems.ModSignItem;
import io.github.how_bout_no.outvoted.block.ModBlockItems.ModTallBlockItem;
import io.github.how_bout_no.outvoted.block.ModReplaceBlocks;
import io.github.how_bout_no.outvoted.block.ModSaplingBlock;
import io.github.how_bout_no.outvoted.block.ModdedSignBlock.ModdedStandingSignBlock;
import io.github.how_bout_no.outvoted.block.ModdedSignBlock.ModdedWallSignBlock;
import io.github.how_bout_no.outvoted.block.trees.BaobabTree;
import io.github.how_bout_no.outvoted.block.trees.PalmTree;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Outvoted.MOD_ID, Registry.BLOCK_KEY);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(Outvoted.MOD_ID, Registry.ITEM_KEY);

    // Blocks
    public static final RegistrySupplier<Block> BURROW = BLOCKS.register("burrow", () -> new BurrowBlock(Block.Settings.copy(Blocks.SAND)));

    public static final RegistrySupplier<Block> PALM_PLANKS = BLOCKS.register("palm_planks", () -> new Block(Block.Settings.copy(Blocks.JUNGLE_PLANKS)));
    public static final RegistrySupplier<Block> PALM_LOG = BLOCKS.register("palm_log", () -> new PillarBlock(Block.Settings.copy(Blocks.JUNGLE_LOG)));
    public static final RegistrySupplier<Block> PALM_LEAVES = BLOCKS.register("palm_leaves", () -> new LeavesBlock(Block.Settings.copy(Blocks.JUNGLE_LEAVES)));
    public static final RegistrySupplier<Block> PALM_SAPLING = BLOCKS.register("palm_sapling", () -> new ModSaplingBlock(new PalmTree(), Block.Settings.copy(Blocks.JUNGLE_SAPLING), BlockTags.SAND));
    public static final RegistrySupplier<Block> PALM_WOOD = BLOCKS.register("palm_wood", () -> new PillarBlock(Block.Settings.copy(Blocks.JUNGLE_WOOD)));
    public static final RegistrySupplier<Block> STRIPPED_PALM_LOG = BLOCKS.register("stripped_palm_log", () -> new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_JUNGLE_LOG)));
    public static final RegistrySupplier<Block> STRIPPED_PALM_WOOD = BLOCKS.register("stripped_palm_wood", () -> new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_JUNGLE_WOOD)));
    public static final RegistrySupplier<Block> PALM_STAIRS = BLOCKS.register("palm_stairs", () -> new ModReplaceBlocks.Stairs(PALM_PLANKS.get().getDefaultState(), Block.Settings.copy(Blocks.JUNGLE_STAIRS)));
    public static final RegistrySupplier<Block> PALM_SLAB = BLOCKS.register("palm_slab", () -> new SlabBlock(AbstractBlock.Settings.copy(Blocks.JUNGLE_SLAB)));
    public static final RegistrySupplier<Block> PALM_BUTTON = BLOCKS.register("palm_button", () -> new ModReplaceBlocks.WoodenButton(AbstractBlock.Settings.copy(Blocks.JUNGLE_BUTTON)));
    public static final RegistrySupplier<Block> PALM_PRESSURE_PLATE = BLOCKS.register("palm_pressure_plate", () -> new ModReplaceBlocks.PressurePlate(PressurePlateBlock.ActivationRule.EVERYTHING, AbstractBlock.Settings.copy(Blocks.JUNGLE_FENCE_GATE)));
    public static final RegistrySupplier<Block> PALM_FENCE = BLOCKS.register("palm_fence", () -> new FenceBlock(Block.Settings.copy(Blocks.JUNGLE_FENCE)));
    public static final RegistrySupplier<Block> PALM_FENCE_GATE = BLOCKS.register("palm_fence_gate", () -> new FenceGateBlock(Block.Settings.copy(Blocks.JUNGLE_FENCE_GATE)));
    public static final RegistrySupplier<Block> PALM_TRAPDOOR = BLOCKS.register("palm_trapdoor", () -> new ModReplaceBlocks.Trapdoor(Block.Settings.copy(Blocks.JUNGLE_TRAPDOOR)));
    public static final RegistrySupplier<Block> PALM_DOOR = BLOCKS.register("palm_door", () -> new ModReplaceBlocks.Door(Block.Settings.copy(Blocks.JUNGLE_DOOR)));
    public static final RegistrySupplier<Block> PALM_SIGN = BLOCKS.register("palm_sign", () -> new ModdedStandingSignBlock(Block.Settings.copy(Blocks.JUNGLE_SIGN), new Identifier(Outvoted.MOD_ID, "entity/signs/palm")));
    public static final RegistrySupplier<Block> PALM_WALL_SIGN = BLOCKS.register("palm_wall_sign", () -> new ModdedWallSignBlock(Block.Settings.copy(Blocks.JUNGLE_WALL_SIGN), new Identifier(Outvoted.MOD_ID, "entity/signs/palm")));

    public static final RegistrySupplier<Block> BAOBAB_PLANKS = BLOCKS.register("baobab_planks", () -> new Block(AbstractBlock.Settings.of(Material.WOOD, MapColor.ORANGE).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
    public static final RegistrySupplier<Block> BAOBAB_LOG = BLOCKS.register("baobab_log", () -> createLogBlock(MapColor.ORANGE, MapColor.STONE_GRAY));
    public static final RegistrySupplier<Block> BAOBAB_LEAVES = BLOCKS.register("baobab_leaves", () -> new LeavesBlock(Block.Settings.copy(Blocks.ACACIA_LEAVES)));
    public static final RegistrySupplier<Block> BAOBAB_SAPLING = BLOCKS.register("baobab_sapling", () -> new ModSaplingBlock(new BaobabTree(), Block.Settings.copy(Blocks.ACACIA_SAPLING)));
    public static final RegistrySupplier<Block> BAOBAB_WOOD = BLOCKS.register("baobab_wood", () -> new PillarBlock(Block.Settings.copy(Blocks.ACACIA_WOOD)));
    public static final RegistrySupplier<Block> STRIPPED_BAOBAB_LOG = BLOCKS.register("stripped_baobab_log", () -> new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_ACACIA_LOG)));
    public static final RegistrySupplier<Block> STRIPPED_BAOBAB_WOOD = BLOCKS.register("stripped_baobab_wood", () -> new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_ACACIA_WOOD)));
    public static final RegistrySupplier<Block> BAOBAB_STAIRS = BLOCKS.register("baobab_stairs", () -> new ModReplaceBlocks.Stairs(BAOBAB_PLANKS.get().getDefaultState(), Block.Settings.copy(Blocks.ACACIA_STAIRS)));
    public static final RegistrySupplier<Block> BAOBAB_SLAB = BLOCKS.register("baobab_slab", () -> new SlabBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_SLAB)));
    public static final RegistrySupplier<Block> BAOBAB_BUTTON = BLOCKS.register("baobab_button", () -> new ModReplaceBlocks.WoodenButton(AbstractBlock.Settings.copy(Blocks.ACACIA_BUTTON)));
    public static final RegistrySupplier<Block> BAOBAB_PRESSURE_PLATE = BLOCKS.register("baobab_pressure_plate", () -> new ModReplaceBlocks.PressurePlate(PressurePlateBlock.ActivationRule.EVERYTHING, AbstractBlock.Settings.copy(Blocks.ACACIA_FENCE_GATE)));
    public static final RegistrySupplier<Block> BAOBAB_FENCE = BLOCKS.register("baobab_fence", () -> new FenceBlock(Block.Settings.copy(Blocks.ACACIA_FENCE)));
    public static final RegistrySupplier<Block> BAOBAB_FENCE_GATE = BLOCKS.register("baobab_fence_gate", () -> new FenceGateBlock(Block.Settings.copy(Blocks.ACACIA_FENCE_GATE)));
    public static final RegistrySupplier<Block> BAOBAB_TRAPDOOR = BLOCKS.register("baobab_trapdoor", () -> new ModReplaceBlocks.Trapdoor(Block.Settings.copy(Blocks.ACACIA_TRAPDOOR)));
    public static final RegistrySupplier<Block> BAOBAB_DOOR = BLOCKS.register("baobab_door", () -> new ModReplaceBlocks.Door(Block.Settings.copy(Blocks.ACACIA_DOOR)));
    public static final RegistrySupplier<Block> BAOBAB_SIGN = BLOCKS.register("baobab_sign", () -> new ModdedStandingSignBlock(Block.Settings.copy(Blocks.ACACIA_SIGN), new Identifier(Outvoted.MOD_ID, "entity/signs/baobab")));
    public static final RegistrySupplier<Block> BAOBAB_WALL_SIGN = BLOCKS.register("baobab_wall_sign", () -> new ModdedWallSignBlock(Block.Settings.copy(Blocks.ACACIA_WALL_SIGN), new Identifier(Outvoted.MOD_ID, "entity/signs/baobab")));

    // Block items
    public static final RegistrySupplier<Item> BURROW_ITEM = BLOCK_ITEMS.register("burrow", () -> new ModBlockItem(BURROW.get(), new Item.Settings()));

    public static final RegistrySupplier<Item> PALM_PLANKS_ITEM = BLOCK_ITEMS.register("palm_planks", () -> new ModBlockItem(PALM_PLANKS.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_LOG_ITEM = BLOCK_ITEMS.register("palm_log", () -> new ModBlockItem(PALM_LOG.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_LEAVES_ITEM = BLOCK_ITEMS.register("palm_leaves", () -> new ModBlockItem(PALM_LEAVES.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_SAPLING_ITEM = BLOCK_ITEMS.register("palm_sapling", () -> new ModDecoBlockItem(PALM_SAPLING.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_WOOD_ITEM = BLOCK_ITEMS.register("palm_wood", () -> new ModBlockItem(PALM_WOOD.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> STRIPPED_PALM_LOG_ITEM = BLOCK_ITEMS.register("stripped_palm_log", () -> new ModBlockItem(STRIPPED_PALM_LOG.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> STRIPPED_PALM_WOOD_ITEM = BLOCK_ITEMS.register("stripped_palm_wood", () -> new ModBlockItem(STRIPPED_PALM_WOOD.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_STAIRS_ITEM = BLOCK_ITEMS.register("palm_stairs", () -> new ModBlockItem(PALM_STAIRS.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_SLAB_ITEM = BLOCK_ITEMS.register("palm_slab", () -> new ModBlockItem(PALM_SLAB.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_BUTTON_ITEM = BLOCK_ITEMS.register("palm_button", () -> new ModBlockItem(PALM_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("palm_pressure_plate", () -> new ModBlockItem(PALM_PRESSURE_PLATE.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_FENCE_ITEM = BLOCK_ITEMS.register("palm_fence", () -> new ModBlockItem(PALM_FENCE.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_FENCE_GATE_ITEM = BLOCK_ITEMS.register("palm_fence_gate", () -> new ModBlockItem(PALM_FENCE_GATE.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_TRAPDOOR_ITEM = BLOCK_ITEMS.register("palm_trapdoor", () -> new ModBlockItem(PALM_TRAPDOOR.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_DOOR_ITEM = BLOCK_ITEMS.register("palm_door", () -> new ModTallBlockItem(PALM_DOOR.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> PALM_SIGN_ITEM = BLOCK_ITEMS.register("palm_sign", () -> new ModSignItem(new Item.Settings(), PALM_SIGN.get(), PALM_WALL_SIGN.get()));

    public static final RegistrySupplier<Item> BAOBAB_PLANKS_ITEM = BLOCK_ITEMS.register("baobab_planks", () -> new ModBlockItem(BAOBAB_PLANKS.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_LOG_ITEM = BLOCK_ITEMS.register("baobab_log", () -> new ModBlockItem(BAOBAB_LOG.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_LEAVES_ITEM = BLOCK_ITEMS.register("baobab_leaves", () -> new ModBlockItem(BAOBAB_LEAVES.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_SAPLING_ITEM = BLOCK_ITEMS.register("baobab_sapling", () -> new ModDecoBlockItem(BAOBAB_SAPLING.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_WOOD_ITEM = BLOCK_ITEMS.register("baobab_wood", () -> new ModBlockItem(BAOBAB_WOOD.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> STRIPPED_BAOBAB_LOG_ITEM = BLOCK_ITEMS.register("stripped_baobab_log", () -> new ModBlockItem(STRIPPED_BAOBAB_LOG.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> STRIPPED_BAOBAB_WOOD_ITEM = BLOCK_ITEMS.register("stripped_baobab_wood", () -> new ModBlockItem(STRIPPED_BAOBAB_WOOD.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_STAIRS_ITEM = BLOCK_ITEMS.register("baobab_stairs", () -> new ModBlockItem(BAOBAB_STAIRS.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_SLAB_ITEM = BLOCK_ITEMS.register("baobab_slab", () -> new ModBlockItem(BAOBAB_SLAB.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_BUTTON_ITEM = BLOCK_ITEMS.register("baobab_button", () -> new ModBlockItem(BAOBAB_BUTTON.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("baobab_pressure_plate", () -> new ModBlockItem(BAOBAB_PRESSURE_PLATE.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_FENCE_ITEM = BLOCK_ITEMS.register("baobab_fence", () -> new ModBlockItem(BAOBAB_FENCE.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_FENCE_GATE_ITEM = BLOCK_ITEMS.register("baobab_fence_gate", () -> new ModBlockItem(BAOBAB_FENCE_GATE.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_TRAPDOOR_ITEM = BLOCK_ITEMS.register("baobab_trapdoor", () -> new ModBlockItem(BAOBAB_TRAPDOOR.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_DOOR_ITEM = BLOCK_ITEMS.register("baobab_door", () -> new ModTallBlockItem(BAOBAB_DOOR.get(), new Item.Settings()));
    public static final RegistrySupplier<Item> BAOBAB_SIGN_ITEM = BLOCK_ITEMS.register("baobab_sign", () -> new ModSignItem(new Item.Settings(), BAOBAB_SIGN.get(), BAOBAB_WALL_SIGN.get()));

    private static PillarBlock createLogBlock(MapColor topColor, MapColor barkColor) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (state) -> {
            return state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor;
        }).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }
}
