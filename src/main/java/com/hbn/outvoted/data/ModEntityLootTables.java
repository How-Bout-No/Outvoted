package com.hbn.outvoted.data;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.init.ModEntityTypes;
import com.hbn.outvoted.init.ModItems;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.stream.Collectors;

public class ModEntityLootTables extends EntityLootTables {

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ForgeRegistries.ENTITIES.getValues().stream()
                .filter(entity -> Optional.ofNullable(entity.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Outvoted.MOD_ID)).isPresent()
                ).collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        this.registerLootTable(ModEntityTypes.INFERNO.get(), LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(Items.BLAZE_ROD)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 3.0F)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
                                .acceptCondition(KilledByPlayer.builder())))
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(ModItems.INFERNO_PIECE.get())
                                .acceptCondition(RandomChanceWithLooting.builder(0.125F, 0.05F))
                                .acceptCondition(KilledByPlayer.builder())))
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(ModItems.INFERNO_HELMET.get())
                                .acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.025F))
                                .acceptCondition(KilledByPlayer.builder())))
        );

        this.registerLootTable(ModEntityTypes.HUNGER.get(), LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(Items.EXPERIENCE_BOTTLE)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
                                .acceptCondition(KilledByPlayer.builder())))
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(ModItems.INFERNO_PIECE.get())
                                .acceptCondition(RandomChanceWithLooting.builder(0.1F, 0.05F))
                                .acceptCondition(KilledByPlayer.builder())))
        );

        this.registerLootTable(ModEntityTypes.KRAKEN.get(), LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(ModItems.KRAKEN_TOOTH.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
                                .acceptCondition(KilledByPlayer.builder())))
        );

        this.registerLootTable(ModEntityTypes.SOUL_BLAZE.get(), LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(Items.BLAZE_ROD)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F)))
                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
                                .acceptCondition(KilledByPlayer.builder())))
        );
    }

}