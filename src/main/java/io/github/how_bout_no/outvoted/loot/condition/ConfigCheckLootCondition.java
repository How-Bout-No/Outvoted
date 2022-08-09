package io.github.how_bout_no.outvoted.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModLootConditions;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigCheckLootCondition implements LootItemCondition {
    @Nullable
    final Boolean patchouli;

    public ConfigCheckLootCondition(@Nullable Boolean boolean_) {
        this.patchouli = boolean_;
    }

    public LootItemConditionType getType() {
        return ModLootConditions.CONFIG_CHECK.get();
    }

    public boolean test(LootContext lootContext) {
        return this.patchouli == null || this.patchouli == Config.giveBook.get();
    }

    public static ConditionBuilder create() {
        return new ConditionBuilder();
    }

    public static class ConditionBuilder implements Builder {
        @Nullable
        private Boolean patchouli;

        public ConditionBuilder patchouli(@Nullable Boolean patchouli) {
            this.patchouli = patchouli;
            return this;
        }

        public ConfigCheckLootCondition build() {
            return new ConfigCheckLootCondition(this.patchouli);
        }
    }

    public static class ConditionSerializer implements Serializer<ConfigCheckLootCondition> {
        public void serialize(JsonObject jsonObject, ConfigCheckLootCondition variantCheckLootCondition, @NotNull JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("patchouli", variantCheckLootCondition.patchouli);
        }

        @NotNull
        public ConfigCheckLootCondition deserialize(JsonObject jsonObject, @NotNull JsonDeserializationContext jsonDeserializationContext) {
            return new ConfigCheckLootCondition(jsonObject.has("patchouli") ? GsonHelper.getAsBoolean(jsonObject, "patchouli") : null);
        }
    }
}
