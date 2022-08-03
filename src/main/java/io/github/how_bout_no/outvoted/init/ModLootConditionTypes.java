package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.loot.condition.ConfigCheckLootCondition;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModLootConditionTypes {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, Outvoted.MOD_ID);

    public static final RegistryObject<LootItemConditionType> CONFIG_CHECK = LOOT_CONDITIONS.register("config_check",
            () -> new LootItemConditionType(new ConfigCheckLootCondition.ConditionSerializer()));
}
