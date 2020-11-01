package com.hbn.outvoted.config;

import com.hbn.outvoted.Outvoted;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OutvotedConfig {
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> max_enchants;
        public final ForgeConfigSpec.BooleanValue spawninferno;
        public final ForgeConfigSpec.BooleanValue spawnhunger;
        public final ForgeConfigSpec.DoubleValue healthinferno;
        public final ForgeConfigSpec.DoubleValue healthhunger;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Outvoted Config").push("outvoted");

            builder.comment("Hovering Inferno").push("inferno");
            spawninferno = builder.comment("Enable/disable natural spawning")
                    .translation("outvoted.configgui.spawn")
                    .define("Natural Spawning", true);

            healthinferno = builder.comment("Set max health")
                    .translation("outvoted.configgui.health")
                    .defineInRange("Max Health", 50.0D, 1.0D, 1000.0D);
            builder.pop();

            builder.comment("Great Hunger").push("hunger");
            max_enchants = builder.comment("Sets the maximum enchants the Great Hunger can store")
                    .translation("outvoted.configgui.max_enchants")
                    .define("Maximum Stored Enchantments", 1);

            healthhunger = builder.comment("Set max health")
                    .translation("outvoted.configgui.health")
                    .defineInRange("Max Health", 20.0D, 1.0D, 1000.0D);

            spawnhunger = builder.comment("Enable/disable natural spawning")
                    .translation("outvoted.configgui.spawn")
                    .define("Natural Spawning", true);
            builder.pop();

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
