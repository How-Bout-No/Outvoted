package com.hbn.outvoted.config;

import com.hbn.outvoted.Outvoted;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OutvotedConfig {
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> max_enchants;
        public final ForgeConfigSpec.BooleanValue spawninferno;
        public final ForgeConfigSpec.BooleanValue spawnhunger;
        public final ForgeConfigSpec.BooleanValue spawnkraken;
        public final ForgeConfigSpec.DoubleValue healthinferno;
        public final ForgeConfigSpec.DoubleValue healthhunger;
        public final ForgeConfigSpec.DoubleValue healthkraken;
        public final ForgeConfigSpec.DoubleValue rateinferno;
        public final ForgeConfigSpec.IntValue rateblaze;
        public final ForgeConfigSpec.IntValue ratehunger;
        public final ForgeConfigSpec.IntValue ratekraken;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Hovering Inferno").push("inferno");
            spawninferno = builder.define("Natural Spawning", true);

            rateblaze= builder.defineInRange("Blaze Group Spawn Weight", 2, 1, 100);

            rateinferno = builder.comment("Percent chance an Inferno will spawn in a group of Blazes, default 20. The chance from a mob spawner will be half of whatever value is set.")
                    .defineInRange("Inferno % Spawn Chance", 20.0D, 1.0D, 100.0D);

            healthinferno = builder.defineInRange("Max Health", 50.0D, 1.0D, 1000.0D);

            builder.pop();
            builder.comment("Great Hunger").push("hunger");
            spawnhunger = builder.define("Natural Spawning", true);

            ratehunger = builder.defineInRange("Spawn Weight", 90, 1, 100);

            healthhunger = builder.defineInRange("Max Health", 20.0D, 1.0D, 1000.0D);

            max_enchants = builder.define("Maximum Stored Enchantments", 5);

            builder.pop();
            builder.comment("Monster of the Ocean Depths").push("kraken");
            spawnkraken = builder.define("Natural Spawning", true);

            ratekraken = builder.defineInRange("Spawn Weight", 24, 1, 100);

            healthkraken = builder.defineInRange("Max Health", 40.0D, 1.0D, 1000.0D);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(OutvotedConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {

    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading event) {

    }
}
