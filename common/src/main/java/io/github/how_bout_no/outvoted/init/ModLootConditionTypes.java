package io.github.how_bout_no.outvoted.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.loot.condition.ConfigCheckLootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.util.registry.Registry;

public class ModLootConditionTypes {
    public static final DeferredRegister<LootConditionType> CONDITIONS = DeferredRegister.create(Outvoted.MOD_ID, Registry.LOOT_CONDITION_TYPE_KEY);

    public static final RegistrySupplier<LootConditionType> CONFIG_CHECK = CONDITIONS.register("config_check", () -> new LootConditionType(new ConfigCheckLootCondition.Serializer()));
}
