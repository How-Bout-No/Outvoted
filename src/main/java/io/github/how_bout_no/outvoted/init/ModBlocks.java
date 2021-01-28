package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.ModBlockItem;
import io.github.how_bout_no.outvoted.block.PalmTreeSaplingBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Outvoted.MOD_ID);
    public static DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Outvoted.MOD_ID);

    //Blocks
    public static final RegistryObject<Block> PALM_PLANKS = BLOCKS.register("palm_planks", () -> new Block(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_LOG = BLOCKS.register("palm_log", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> PALM_LEAVES = BLOCKS.register("palm_leaves", () -> new LeavesBlock(AbstractBlock.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid().setAllowsSpawn(Blocks::allowsSpawnOnLeaves).setSuffocates(Blocks::isntSolid).setBlocksVision(Blocks::isntSolid)));
    public static final RegistryObject<Block> PALM_SAPLING = BLOCKS.register("palm_sapling", () -> new PalmTreeSaplingBlock(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.PLANT)));
    public static final RegistryObject<Block> PALM_WOOD = BLOCKS.register("palm_wood", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_PALM_LOG = BLOCKS.register("stripped_palm_log", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_PALM_WOOD = BLOCKS.register("stripped_palm_wood", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));


    //Block items
    public static final RegistryObject<Item> PALM_PLANKS_ITEM = BLOCK_ITEMS.register("palm_planks", () -> new ModBlockItem(PALM_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_LOG_ITEM = BLOCK_ITEMS.register("palm_log", () -> new ModBlockItem(PALM_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_LEAVES_ITEM = BLOCK_ITEMS.register("palm_leaves", () -> new ModBlockItem(PALM_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> PALM_SAPLING_ITEM = BLOCK_ITEMS.register("palm_sapling", () -> new ModBlockItem(PALM_SAPLING.get(), new Item.Properties(), 1));
    public static final RegistryObject<Item> PALM_WOOD_ITEM = BLOCK_ITEMS.register("palm_wood", () -> new ModBlockItem(PALM_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_PALM_LOG_ITEM = BLOCK_ITEMS.register("stripped_palm_log", () -> new ModBlockItem(STRIPPED_PALM_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_PALM_WOOD_ITEM = BLOCK_ITEMS.register("stripped_palm_wood", () -> new ModBlockItem(STRIPPED_PALM_WOOD.get(), new Item.Properties()));
}
