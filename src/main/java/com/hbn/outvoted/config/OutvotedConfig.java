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
        public final ForgeConfigSpec.BooleanValue spawnkraken;
        public final ForgeConfigSpec.DoubleValue healthinferno;
        public final ForgeConfigSpec.DoubleValue healthhunger;
        public final ForgeConfigSpec.DoubleValue healthkraken;
        public final ForgeConfigSpec.IntValue rateinferno;
        public final ForgeConfigSpec.IntValue ratehunger;
        public final ForgeConfigSpec.IntValue ratekraken;
        public final ForgeConfigSpec.BooleanValue infernovariant;
        //public final ForgeConfigSpec.BooleanValue krakenvariant;
        public final ForgeConfigSpec.BooleanValue restrictinferno;

        /**
         * Common config setup
         * Related to both server and client options
         *
         * @param builder
         */
        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Hovering Inferno").push("inferno");

            spawninferno = builder.comment("This will affect all Inferno spawns (natural + spawner)").define("Natural Spawning", true);
            rateinferno = builder.defineInRange("Inferno Spawn Weight", 1, 1, 100);
            healthinferno = builder.defineInRange("Max Health", 50.0D, 1.0D, 1000.0D);
            infernovariant = builder.comment("Blue coloration to *both* Blazes and Infernos in Soul Sand Valleys. Disabled by default to keep it Vanilla").define("Biome Variants", false);
            restrictinferno = builder.comment("Restrict Inferno groups to spawn only in Nether Wastes or not").define("Restrict Spawning", true);

            builder.pop();
            builder.comment("Great Hunger").push("hunger");

            spawnhunger = builder.define("Natural Spawning", true);
            ratehunger = builder.defineInRange("Spawn Weight", 5, 1, 100);
            healthhunger = builder.defineInRange("Max Health", 20.0D, 1.0D, 1000.0D);
            max_enchants = builder.define("Maximum Stored Enchantments", 5);

            builder.pop();
            builder.comment("Monster of the Ocean Depths").push("kraken");

            spawnkraken = builder.define("Natural Spawning", true);
            ratekraken = builder.defineInRange("Spawn Weight", 2, 1, 100);
            healthkraken = builder.defineInRange("Max Health", 40.0D, 1.0D, 1000.0D);
            //krakenvariant = builder.comment("Slight coloration based on biomes, bluer in colder oceans while yellower in warmer ones").define("Biome Variants", true);

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

    public static class Client {
        public final ForgeConfigSpec.BooleanValue creativetab;

        /**
         * Client config setup
         * Just used for the creative tab right now
         *
         * @param builder
         */
        public Client(ForgeConfigSpec.Builder builder) {
            builder.comment("General").push("general");

            creativetab = builder.define("Use Custom Creative Tab", true);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(OutvotedConfig.Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {
        System.out.println(">> Outvoted " + event.getConfig().getType() + " Config Loaded");
    }

//    @SubscribeEvent
//    public static void onFileChange(final ModConfig.Reloading event) {
//    }
}
