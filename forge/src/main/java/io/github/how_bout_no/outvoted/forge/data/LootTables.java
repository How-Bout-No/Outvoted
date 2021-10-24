package io.github.how_bout_no.outvoted.forge.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.EntityLootTableGenerator;
import net.minecraft.data.server.LootTablesProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
        return ImmutableList.of(Pair.of(ModEntityLootTables::new, LootContextTypes.ENTITY));
    }

    public static class ModEntityLootTables extends EntityLootTableGenerator {
        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            List<EntityType<?>> list = new ArrayList<>();
            list.add(ModEntityTypes.WILDFIRE.get());
            list.add(ModEntityTypes.GLUTTON.get());
            list.add(ModEntityTypes.BARNACLE.get());
            return list;
        }

        @Override
        protected void addTables() {
            this.register(ModEntityTypes.WILDFIRE.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(net.minecraft.item.Items.BLAZE_ROD)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F)))
                                    .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
                    .pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ModItems.WILDFIRE_PIECE.get())
                                    .conditionally(RandomChanceWithLootingLootCondition.builder(0.15F, 0.05F))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
                    .pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ModItems.WILDFIRE_HELMET.get())
                                    .conditionally(RandomChanceWithLootingLootCondition.builder(0.05F, 0.025F))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
            );

            this.register(ModEntityTypes.GLUTTON.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(net.minecraft.item.Items.EXPERIENCE_BOTTLE)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
                                    .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
                    .pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ModItems.VOID_HEART.get())
                                    .conditionally(RandomChanceWithLootingLootCondition.builder(0.1F, 0.05F))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
            );

            this.register(ModEntityTypes.BARNACLE.get(), LootTable.builder()
                    .pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ModItems.BARNACLE_TOOTH.get())
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
                                    .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0F, 1.0F)))
                                    .conditionally(KilledByPlayerLootCondition.builder())))
            );
        }
    }
}
