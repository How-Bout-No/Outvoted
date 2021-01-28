package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.ModBlockItem;
import io.github.how_bout_no.outvoted.block.ModSignItem;
import io.github.how_bout_no.outvoted.block.ModTallBlockItem;
import io.github.how_bout_no.outvoted.block.PalmSaplingBlock;
import io.github.how_bout_no.outvoted.block.trees.PalmTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Outvoted.MOD_ID);
    public static DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);


    //Blocks
    public static final RegistryObject<Block> PALM_PLANKS = BLOCKS.register("palm_planks", () -> new Block(Block.Properties.from(Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<Block> PALM_LOG = BLOCKS.register("palm_log", () -> new RotatedPillarBlock(Block.Properties.from(Blocks.JUNGLE_LOG)));
    public static final RegistryObject<Block> PALM_LEAVES = BLOCKS.register("palm_leaves", () -> new LeavesBlock(Block.Properties.from(Blocks.JUNGLE_LEAVES)));
    public static final RegistryObject<Block> PALM_SAPLING = BLOCKS.register("palm_sapling", () -> new PalmSaplingBlock(new PalmTree(), Block.Properties.from(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> PALM_WOOD = BLOCKS.register("palm_wood", () -> new RotatedPillarBlock(Block.Properties.from(Blocks.JUNGLE_WOOD)));
    public static final RegistryObject<Block> STRIPPED_PALM_LOG = BLOCKS.register("stripped_palm_log", () -> new RotatedPillarBlock(Block.Properties.from(Blocks.STRIPPED_JUNGLE_LOG)));
    public static final RegistryObject<Block> STRIPPED_PALM_WOOD = BLOCKS.register("stripped_palm_wood", () -> new RotatedPillarBlock(Block.Properties.from(Blocks.STRIPPED_JUNGLE_WOOD)));
    public static final RegistryObject<Block> PALM_STAIRS = BLOCKS.register("palm_stairs", () -> new StairsBlock(PALM_PLANKS.get().getDefaultState(), Block.Properties.from(Blocks.JUNGLE_STAIRS)));
    public static final RegistryObject<Block> PALM_SLAB = BLOCKS.register("palm_slab", () -> new SlabBlock(AbstractBlock.Properties.from(Blocks.JUNGLE_SLAB)));
    public static final RegistryObject<Block> PALM_BUTTON = BLOCKS.register("palm_button", () -> new WoodButtonBlock(AbstractBlock.Properties.from(Blocks.JUNGLE_BUTTON)));
    public static final RegistryObject<Block> PALM_PRESSURE_PLATE = BLOCKS.register("palm_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, AbstractBlock.Properties.from(Blocks.JUNGLE_FENCE_GATE)));
    public static final RegistryObject<Block> PALM_FENCE = BLOCKS.register("palm_fence", () -> new FenceBlock(Block.Properties.from(Blocks.JUNGLE_FENCE)));
    public static final RegistryObject<Block> PALM_FENCE_GATE = BLOCKS.register("palm_fence_gate", () -> new FenceGateBlock(Block.Properties.from(Blocks.JUNGLE_FENCE_GATE)));
    public static final RegistryObject<Block> PALM_TRAPDOOR = BLOCKS.register("palm_trapdoor", () -> new TrapDoorBlock(Block.Properties.from(Blocks.JUNGLE_TRAPDOOR)));
    public static final RegistryObject<Block> PALM_DOOR = BLOCKS.register("palm_door", () -> new DoorBlock(Block.Properties.from(Blocks.JUNGLE_DOOR)));
    public static final RegistryObject<Block> PALM_SIGN = BLOCKS.register("palm_sign", () -> new StandingSignBlock(Block.Properties.from(Blocks.JUNGLE_SIGN), WoodType.JUNGLE));
    public static final RegistryObject<Block> PALM_WALL_SIGN = BLOCKS.register("palm_wall_sign", () -> new WallSignBlock(Block.Properties.from(Blocks.JUNGLE_WALL_SIGN), WoodType.JUNGLE));


    //Block items
    public static final RegistryObject<Item> PALM_PLANKS_ITEM = BLOCK_ITEMS.register("palm_planks", () -> new ModBlockItem(PALM_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_LOG_ITEM = BLOCK_ITEMS.register("palm_log", () -> new ModBlockItem(PALM_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_LEAVES_ITEM = BLOCK_ITEMS.register("palm_leaves", () -> new ModBlockItem(PALM_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_SAPLING_ITEM = BLOCK_ITEMS.register("palm_sapling", () -> new ModBlockItem(PALM_SAPLING.get(), new Item.Properties(), true));
    public static final RegistryObject<Item> PALM_WOOD_ITEM = BLOCK_ITEMS.register("palm_wood", () -> new ModBlockItem(PALM_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_PALM_LOG_ITEM = BLOCK_ITEMS.register("stripped_palm_log", () -> new ModBlockItem(STRIPPED_PALM_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_PALM_WOOD_ITEM = BLOCK_ITEMS.register("stripped_palm_wood", () -> new ModBlockItem(STRIPPED_PALM_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_STAIRS_ITEM = BLOCK_ITEMS.register("palm_stairs", () -> new ModBlockItem(PALM_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_SLAB_ITEM = BLOCK_ITEMS.register("palm_slab", () -> new ModBlockItem(PALM_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_BUTTON_ITEM = BLOCK_ITEMS.register("palm_button", () -> new ModBlockItem(PALM_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("palm_pressure_plate", () -> new ModBlockItem(PALM_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_FENCE_ITEM = BLOCK_ITEMS.register("palm_fence", () -> new ModBlockItem(PALM_FENCE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_FENCE_GATE_ITEM = BLOCK_ITEMS.register("palm_fence_gate", () -> new ModBlockItem(PALM_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_TRAPDOOR_ITEM = BLOCK_ITEMS.register("palm_trapdoor", () -> new ModBlockItem(PALM_TRAPDOOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_DOOR_ITEM = BLOCK_ITEMS.register("palm_door", () -> new ModTallBlockItem(PALM_DOOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_SIGN_ITEM = BLOCK_ITEMS.register("palm_sign", () -> new ModSignItem(new Item.Properties(), PALM_SIGN.get(), PALM_WALL_SIGN.get()));


    public static Boolean allowsSpawnOnLeaves(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }

    public static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }
}
