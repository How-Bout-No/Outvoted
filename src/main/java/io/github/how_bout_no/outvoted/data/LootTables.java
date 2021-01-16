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
            LootTableManager.validateLootTable(validationtracker, id, table);
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
            this.registerLootTable(ModEntityTypes.INFERNO.get(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(net.minecraft.item.Items.BLAZE_ROD)
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 3.0F)))
                                    .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
                                    .acceptCondition(KilledByPlayer.builder())))
                    .addLootPool(LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(ModItems.INFERNO_PIECE.get())
                                    .acceptCondition(RandomChanceWithLooting.builder(0.15F, 0.05F))
                                    .acceptCondition(KilledByPlayer.builder())))
                    .addLootPool(LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(ModItems.INFERNO_HELMET.get())
                                    .acceptCondition(RandomChanceWithLooting.builder(0.05F, 0.025F))
                                    .acceptCondition(KilledByPlayer.builder())))
            );

            this.registerLootTable(ModEntityTypes.HUNGER.get(), LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(net.minecraft.item.Items.EXPERIENCE_BOTTLE)
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F)))
                                    .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
                                    .acceptCondition(KilledByPlayer.builder())))
                    .addLootPool(LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(ModItems.VOID_HEART.get())
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
        }
    }
}
