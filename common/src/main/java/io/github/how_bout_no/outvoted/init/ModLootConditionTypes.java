package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.loot.condition.ConfigCheckLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class ModLootConditionTypes {
    public static final LootConditionType CONFIG_CHECK = register("config_check", new ConfigCheckLootCondition.Serializer());

    public static void register() {
        // No-op method to ensure that this class is loaded and its static initialisers are run
    }

    private static LootConditionType register(final String name, final JsonSerializer<? extends LootCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(Outvoted.MOD_ID, name), new LootConditionType(serializer));
    }
}
