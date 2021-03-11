package io.github.how_bout_no.outvoted.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LootTables extends LootTableProvider {

    public LootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((id, table) -> {
            LootTableManager.validate(validationtracker, id, table);
        });
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(ModEntityLootTables::new, LootParameterSets.ENTITY));
    }

    public static class ModEntityLootTables extends EntityLootTables {

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return ForgeRegistries.ENTITIES.getValues().stream()
                    .filter(entity -> Optional.ofNullable(entity.getRegistryName())
                            .filter(registryName -> registryName.getNamespace().equals(Outvoted.MOD_ID)).isPresent()
                    ).collect(Collectors.toList());
        }

        @Override
        protected void addTables() {
            this.add(ModEntityTypes.WILDFIRE.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(net.minecraft.item.Items.BLAZE_ROD)
                                    .apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F)))
                                    .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F)))
                                    .when(KilledByPlayer.killedByPlayer())))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(ModItems.WILDFIRE_PIECE.get())
                                    .when(RandomChanceWithLooting.randomChanceAndLootingBoost(0.15F, 0.05F))
                                    .when(KilledByPlayer.killedByPlayer())))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(ModItems.WILDFIRE_HELMET.get())
                                    .when(RandomChanceWithLooting.randomChanceAndLootingBoost(0.05F, 0.025F))
                                    .when(KilledByPlayer.killedByPlayer())))
            );

            this.add(ModEntityTypes.HUNGER.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(net.minecraft.item.Items.EXPERIENCE_BOTTLE)
                                    .apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
                                    .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F)))
                                    .when(KilledByPlayer.killedByPlayer())))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(ModItems.VOID_HEART.get())
                                    .when(RandomChanceWithLooting.randomChanceAndLootingBoost(0.1F, 0.05F))
                                    .when(KilledByPlayer.killedByPlayer())))
            );

            this.add(ModEntityTypes.KRAKEN.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(ModItems.KRAKEN_TOOTH.get())
                                    .apply(SetCount.setCount(RandomValueRange.between(0.0F, 1.0F)))
                                    .apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F)))
                                    .when(KilledByPlayer.killedByPlayer())))
            );
        }
    }
}
