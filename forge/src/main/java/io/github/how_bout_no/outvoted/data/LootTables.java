package io.github.how_bout_no.outvoted.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.data.server.EntityLootTableGenerator;
import net.minecraft.data.server.LootTablesProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LootTables extends LootTablesProvider {
    public LootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void validate(Map<Identifier, LootTable> map, LootTableReporter validationtracker) {
        map.forEach((id, table) -> {
            LootManager.validate(validationtracker, id, table);
        });
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<Identifier, LootTable.Builder>>>, LootContextType>> getTables() {
        return ImmutableList.of(Pair.of(ModEntityLootTables::new, LootContextTypes.ENTITY), Pair.of(ModBlockLootTables::new, LootContextTypes.BLOCK));
    }

    public static class ModEntityLootTables extends EntityLootTableGenerator {
        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return StreamSupport
                    .stream(ForgeRegistries.ENTITIES.spliterator(), false)
                    .filter(
                            entry -> entry.getRegistryName() != null &&
                                    entry.getRegistryName().getNamespace().equals(Outvoted.MOD_ID)
                    ).collect(Collectors.toSet());
        }

        @Override
        protected void addTables() {
            this.register(ModEntityTypes.WILDFIRE.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
                            .with(ItemEntry.builder(ModItems.WILDFIRE_PIECE.get())
                                    .conditionally(RandomChanceWithLootingLootCondition.builder(0.2F, 0.1F))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
                            .with(ItemEntry.builder(ModItems.WILDFIRE_HELMET.get())
                                    .conditionally(RandomChanceWithLootingLootCondition.builder(0.05F, 0.025F))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
            );
            this.register(ModEntityTypes.GLUTTON.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
                            .with(ItemEntry.builder(net.minecraft.item.Items.EXPERIENCE_BOTTLE)
                                    .apply(SetCountLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F)))
                                    .apply(LootingEnchantLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F)))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
                            .with(ItemEntry.builder(ModItems.VOID_HEART.get())
                                    .conditionally(RandomChanceWithLootingLootCondition.builder(0.1F, 0.05F))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
            );
            this.register(ModEntityTypes.BARNACLE.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
                            .with(ItemEntry.builder(ModItems.BARNACLE_TOOTH.get())
                                    .apply(SetCountLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F)))
                                    .apply(LootingEnchantLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F)))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
            );
            this.register(ModEntityTypes.MEERKAT.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
                            .with(ItemEntry.builder(Items.STICK)
                                    .apply(SetCountLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F))))
                            .with(ItemEntry.builder(Items.CACTUS)
                                    .apply(SetCountLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F))))
                            .with(ItemEntry.builder(Items.WHEAT_SEEDS)
                                    .apply(SetCountLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F))))
                            .with(ItemEntry.builder(Items.STRING)
                                    .apply(SetCountLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F))))
            ));
            this.register(ModEntityTypes.OSTRICH.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
                            .with(ItemEntry.builder(Items.EGG)
                                    .apply(SetCountLootFunction.builder(UniformLootTableRange.between(0.0F, 1.0F))))
            ));
            this.register(ModEntityTypes.TERMITE.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootTableRange.create(1))
            ));
        }
    }

    public static class ModBlockLootTables extends BlockLootTableGenerator {
        private static final float[] SAPLING_DROP_CHANCE = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return StreamSupport
                    .stream(ForgeRegistries.BLOCKS.spliterator(), false)
                    .filter(
                            entry -> entry.getRegistryName() != null &&
                                    entry.getRegistryName().getNamespace().equals(Outvoted.MOD_ID)
                    ).collect(Collectors.toSet());
        }

        @Override
        protected void addTables() {
            this.addDrop(ModBlocks.BURROW.get());
            this.addDrop(ModBlocks.PALM_PLANKS.get());
            this.addDrop(ModBlocks.PALM_LOG.get());
            this.addDrop(ModBlocks.PALM_LEAVES.get(), (leaves) -> leavesDrop(leaves, ModBlocks.PALM_SAPLING.get(), SAPLING_DROP_CHANCE));
            this.addDrop(ModBlocks.PALM_SAPLING.get());
            this.addDrop(ModBlocks.PALM_WOOD.get());
            this.addDrop(ModBlocks.STRIPPED_PALM_LOG.get());
            this.addDrop(ModBlocks.STRIPPED_PALM_WOOD.get());
            this.addDrop(ModBlocks.PALM_STAIRS.get());
            this.addDrop(ModBlocks.PALM_SLAB.get());
            this.addDrop(ModBlocks.PALM_BUTTON.get());
            this.addDrop(ModBlocks.PALM_PRESSURE_PLATE.get());
            this.addDrop(ModBlocks.PALM_FENCE.get());
            this.addDrop(ModBlocks.PALM_FENCE_GATE.get());
            this.addDrop(ModBlocks.PALM_TRAPDOOR.get());
            this.addDrop(ModBlocks.PALM_DOOR.get());
            this.addDrop(ModBlocks.PALM_SIGN.get());
            this.addDrop(ModBlocks.PALM_WALL_SIGN.get());
            this.addDrop(ModBlocks.BAOBAB_PLANKS.get());
            this.addDrop(ModBlocks.BAOBAB_LOG.get());
            this.addDrop(ModBlocks.BAOBAB_LEAVES.get(), (leaves) -> leavesDrop(leaves, ModBlocks.BAOBAB_SAPLING.get(), SAPLING_DROP_CHANCE));
            this.addDrop(ModBlocks.BAOBAB_SAPLING.get());
            this.addDrop(ModBlocks.BAOBAB_WOOD.get());
            this.addDrop(ModBlocks.STRIPPED_BAOBAB_LOG.get());
            this.addDrop(ModBlocks.STRIPPED_BAOBAB_WOOD.get());
            this.addDrop(ModBlocks.BAOBAB_STAIRS.get());
            this.addDrop(ModBlocks.BAOBAB_SLAB.get());
            this.addDrop(ModBlocks.BAOBAB_BUTTON.get());
            this.addDrop(ModBlocks.BAOBAB_PRESSURE_PLATE.get());
            this.addDrop(ModBlocks.BAOBAB_FENCE.get());
            this.addDrop(ModBlocks.BAOBAB_FENCE_GATE.get());
            this.addDrop(ModBlocks.BAOBAB_TRAPDOOR.get());
            this.addDrop(ModBlocks.BAOBAB_DOOR.get());
            this.addDrop(ModBlocks.BAOBAB_SIGN.get());
            this.addDrop(ModBlocks.BAOBAB_WALL_SIGN.get());
        }
    }
}
