package io.github.how_bout_no.outvoted.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config {

    //Common
    public static ForgeConfigSpec.DoubleValue wildfireHealth;
    public static ForgeConfigSpec.BooleanValue wildfireSpawn;
    public static ForgeConfigSpec.IntValue wildfireRate;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> wildfireBiomes;
    public static ForgeConfigSpec.IntValue wildfireFireballCount;
    public static ForgeConfigSpec.DoubleValue wildfireOffsetAngle;
    public static ForgeConfigSpec.DoubleValue wildfireMaxDepressAngle;
    public static ForgeConfigSpec.BooleanValue wildfireDoFireballExplode;
    public static ForgeConfigSpec.DoubleValue wildfireExplodePower;
    public static ForgeConfigSpec.DoubleValue gluttonHealth;
    public static ForgeConfigSpec.BooleanValue gluttonSpawn;
    public static ForgeConfigSpec.IntValue gluttonRate;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> gluttonBiomes;
    public static ForgeConfigSpec.BooleanValue gluttonStealEnchants;
    public static ForgeConfigSpec.BooleanValue gluttonCapEnchants;
    public static ForgeConfigSpec.IntValue gluttonMaxEnchants;
    public static ForgeConfigSpec.DoubleValue barnacleHealth;
    public static ForgeConfigSpec.BooleanValue barnacleSpawn;
    public static ForgeConfigSpec.IntValue barnacleRate;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> barnacleBiomes;
    public static ForgeConfigSpec.DoubleValue copperGolemHealth;
    public static ForgeConfigSpec.DoubleValue copperGolemOxidation;
    public static ForgeConfigSpec.DoubleValue glareHealth;
    public static ForgeConfigSpec.BooleanValue glareSpawn;
    public static ForgeConfigSpec.IntValue glareRate;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> glareBiomes;
    public static ForgeConfigSpec.BooleanValue glareShouldInteract;
    public static ForgeConfigSpec.IntValue glareInvSize;
    public static ForgeConfigSpec.BooleanValue giveBook;
    public static ForgeConfigSpec.IntValue helmetPenalty;

    // Client
    public static ForgeConfigSpec.BooleanValue wildfireVariants;

    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.push("Wildfire Settings");

        wildfireHealth = COMMON_BUILDER
                .comment("health")
                .defineInRange("health", 50, 1, Double.MAX_VALUE);
        wildfireSpawn = COMMON_BUILDER
                .comment("spawn")
                .define("spawn", true);
        wildfireRate = COMMON_BUILDER
                .comment("rate")
                .defineInRange("rate", 1, 1, 100);
        wildfireBiomes = COMMON_BUILDER
                .comment("biomes")
                .defineList("biomes", Arrays.asList("minecraft:nether_wastes", "minecraft:basalt_deltas", "minecraft:crimson_forest", "minecraft:soul_sand_valley"), b -> b instanceof String);

        COMMON_BUILDER.push("Attack Settings");

        wildfireFireballCount = COMMON_BUILDER
                .comment("fireball count")
                .defineInRange("fireballs", 17, 1, Integer.MAX_VALUE);
        wildfireOffsetAngle = COMMON_BUILDER
                .comment("offset")
                .defineInRange("offset", 4, 1, Double.MAX_VALUE);
        wildfireMaxDepressAngle = COMMON_BUILDER
                .comment("depress")
                .defineInRange("depress", 50, 1, Double.MAX_VALUE);
        wildfireDoFireballExplode = COMMON_BUILDER
                .comment("explode")
                .define("explode", false);
        wildfireExplodePower = COMMON_BUILDER
                .comment("power")
                .defineInRange("power", 0.5, 0, 1);

        COMMON_BUILDER.pop(2);
        COMMON_BUILDER.push("Glutton Settings");

        gluttonHealth = COMMON_BUILDER
                .comment("health")
                .defineInRange("Glutton Health", 20, 1, Double.MAX_VALUE);
        gluttonSpawn = COMMON_BUILDER
                .comment("spawn")
                .define("Should Glutton Spawn", true);
        gluttonRate = COMMON_BUILDER
                .comment("rate")
                .defineInRange("Glutton Spawn Rate", 5, 1, 100);
        gluttonBiomes = COMMON_BUILDER
                .comment("biomes")
                .defineList("Glutton Spawn Biomes", Arrays.asList("#swamp", "#desert", "minecraft:badlands_plateau", "minecraft:badlands"), b -> b instanceof String);
        gluttonStealEnchants = COMMON_BUILDER
                .comment("Enable stealing enchantments on bite")
                .define("Should Glutton Steal Enchants?", true);
        gluttonCapEnchants = COMMON_BUILDER
                .comment("Cap overenchants to 1 over vanilla max level, disable to have no upper limit on overenchants")
                .define("Allow Enchants Above Maximum Level", true);
        gluttonMaxEnchants = COMMON_BUILDER
                .comment("max")
                .defineInRange("Maximum # Of Enchants To Store", 5, 1, 100);

        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Barnacle Settings");

        barnacleHealth = COMMON_BUILDER
                .comment("health")
                .defineInRange("Barnacle Health", 40, 1, Double.MAX_VALUE);
        barnacleSpawn = COMMON_BUILDER
                .comment("spawn")
                .define("Should Barnacle Spawn", true);
        barnacleRate = COMMON_BUILDER
                .comment("rate")
                .defineInRange("Barnacle Spawn Rate", 2, 1, 100);
        barnacleBiomes = COMMON_BUILDER
                .comment("biomes")
                .defineList("Barnacle Spawn Biomes", Arrays.asList("minecraft:deep_warm_ocean", "minecraft:deep_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_lukewarm_ocean"), b -> b instanceof String);

        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Barnacle Settings");

        barnacleHealth = COMMON_BUILDER
                .comment("health")
                .defineInRange("Barnacle Health", 40, 1, Double.MAX_VALUE);
        barnacleSpawn = COMMON_BUILDER
                .comment("spawn")
                .define("Should Barnacle Spawn", true);
        barnacleRate = COMMON_BUILDER
                .comment("rate")
                .defineInRange("Barnacle Spawn Rate", 2, 1, 100);
        barnacleBiomes = COMMON_BUILDER
                .comment("biomes")
                .defineList("Barnacle Spawn Biomes", Arrays.asList("minecraft:deep_warm_ocean", "minecraft:deep_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_lukewarm_ocean"), b -> b instanceof String);

        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Copper Golem Settings");

        copperGolemHealth = COMMON_BUILDER
                .comment("health")
                .defineInRange("Copper Golem Health", 25, 1, Double.MAX_VALUE);
        copperGolemOxidation = COMMON_BUILDER
                .comment("Percentage chance for golem to oxidize per second, as a decimal")
                .defineInRange("Copper Golem Oxidation Rate", 0.001, 0, 1);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Glare Settings");

        glareHealth = COMMON_BUILDER
                .comment("health")
                .defineInRange("Glare Health", 10, 1, Double.MAX_VALUE);
        glareSpawn = COMMON_BUILDER
                .comment("spawn")
                .define("Should Glare Spawn", true);
        glareRate = COMMON_BUILDER
                .comment("rate")
                .defineInRange("Glare Spawn Rate", 10, 1, 100);
        glareBiomes = COMMON_BUILDER
                .comment("biomes")
                .defineList("Glare Spawn Biomes", Arrays.asList("#taiga", "#jungle", "#plains", "#savanna", "#forest", "#swamp", "#underground"), b -> b instanceof String);
        glareShouldInteract = COMMON_BUILDER
                .comment("Set to FALSE to disable the Glare picking up and placing blocks/items")
                .define("Should Glare Interact With The World?", true);
        glareInvSize = COMMON_BUILDER
                .comment("inventory")
                .defineInRange("Glare Inventory Size", 32, 1, 64);

        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Misc Settings");

        giveBook = COMMON_BUILDER
                .comment("book")
                .define("Give Patchouli Book On Login", true);
        helmetPenalty = COMMON_BUILDER
                .comment("Set to 0 to never consume durability")
                .defineInRange("Time (in ticks) it takes in between consuming durability", 40, 0, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();
    }

    public static void registerClientConfig(ForgeConfigSpec.Builder CLIENT_BUILDER) {
        CLIENT_BUILDER.push("outvoted");

        wildfireVariants = CLIENT_BUILDER
                .comment("Enable Wildfire Variants?")
                .define("enabled", true);

        CLIENT_BUILDER.pop();
    }
}
