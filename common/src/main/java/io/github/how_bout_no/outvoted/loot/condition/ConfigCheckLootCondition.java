package io.github.how_bout_no.outvoted.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModLootConditionTypes;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import org.jetbrains.annotations.Nullable;

public class ConfigCheckLootCondition implements LootCondition {
    @Nullable
    final Boolean patchouli;

    public ConfigCheckLootCondition(@Nullable Boolean boolean_) {
        this.patchouli = boolean_;
    }

    public LootConditionType getType() {
        return ModLootConditionTypes.CONFIG_CHECK;
    }

    public boolean test(LootContext lootContext) {
        return this.patchouli == null || this.patchouli == Outvoted.commonConfig.misc.givePatchouliBookOnLogin;
    }

    public static ConfigCheckLootCondition.Builder create() {
        return new ConfigCheckLootCondition.Builder();
    }

    public static class Builder implements net.minecraft.loot.condition.LootCondition.Builder {
        @Nullable
        private Boolean patchouli;

        public Builder() {
        }

        public ConfigCheckLootCondition.Builder patchouli(@Nullable Boolean patchouli) {
            this.patchouli = patchouli;
            return this;
        }

        public ConfigCheckLootCondition build() {
            return new ConfigCheckLootCondition(this.patchouli);
        }
    }

    public static class Serializer implements JsonSerializer<ConfigCheckLootCondition> {
        public Serializer() {
        }

        public void toJson(JsonObject jsonObject, ConfigCheckLootCondition variantCheckLootCondition, JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("patchouli", variantCheckLootCondition.patchouli);
        }

        public ConfigCheckLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            Boolean boolean_ = jsonObject.has("patchouli") ? JsonHelper.getBoolean(jsonObject, "patchouli") : null;
            return new ConfigCheckLootCondition(boolean_);
        }
    }
}
