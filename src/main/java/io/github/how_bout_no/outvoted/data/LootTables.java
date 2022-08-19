package io.github.how_bout_no.outvoted.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import io.github.how_bout_no.outvoted.init.ModEntities;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {
    public LootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((id, table) -> {
            net.minecraft.world.level.storage.loot.LootTables.validate(validationtracker, id, table);
        });
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(ModEntityLootTables::new, LootContextParamSets.ENTITY));
    }

    public static class ModEntityLootTables extends EntityLoot {
        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            List<EntityType<?>> list = new ArrayList<>();
            list.add(ModEntities.WILDFIRE.get());
            list.add(ModEntities.GLUTTON.get());
            list.add(ModEntities.BARNACLE.get());
            list.add(ModEntities.COPPER_GOLEM.get());
            list.add(ModEntities.GLARE.get());
            list.add(ModEntities.MEERKAT.get());
            list.add(ModEntities.OSTRICH.get());
            return list;
        }

        @Override
        protected void addTables() {
            this.add(ModEntities.WILDFIRE.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.BLAZE_ROD)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ModItems.WILDFIRE_PIECE.get())
                                    .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.15F, 0.05F))
                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ModItems.WILDFIRE_HELMET.get())
                                    .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.025F))
                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())))
            );
            this.add(ModEntities.GLUTTON.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ModItems.VOID_HEART.get())
                                    .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.05F))
                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())))
            );
            this.add(ModEntities.BARNACLE.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ModItems.BARNACLE_TOOTH.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())))
            );
            this.add(ModEntities.COPPER_GOLEM.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Blocks.ALLIUM)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.COPPER_INGOT)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                            )
                    )
            );
            this.add(ModEntities.GLARE.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.GLOW_BERRIES)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                            )
                    )
            );
            this.add(ModEntities.MEERKAT.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.STICK)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))))
                            .add(LootItem.lootTableItem(Items.CACTUS)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))))
                            .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))))
                            .add(LootItem.lootTableItem(Items.STRING)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))))
                    )
            );
            this.add(ModEntities.OSTRICH.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.EGG)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))))
                    )
            );
        }

        static {
            SPECIAL_LOOT_TABLE_TYPES = ImmutableSet.of(EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER, ModEntities.COPPER_GOLEM.get());
        }
    }
}
