package io.github.how_bout_no.outvoted.config;

import io.github.how_bout_no.completeconfig.api.ConfigEntries;
import io.github.how_bout_no.completeconfig.api.ConfigGroup;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class OutvotedConfigCommon implements ConfigGroup {
    @Transitive
    @ConfigEntries
    public static class Entities implements ConfigGroup {
        @Transitive
        @ConfigEntries
        public static class Wildfire extends EntityConfigBase implements ConfigGroup {
            @Getter
            private static boolean spawn = true;
            @Getter
            private static int rate = 1;
            @Getter
            private static List<String> biomes = Arrays.asList("minecraft:nether_wastes", "minecraft:basalt_deltas", "minecraft:crimson_forest", "minecraft:soul_sand_valley");
            @Getter
            private static double health = 50.0D;
            @Getter
            private static boolean variants = true;

            @Transitive
            @ConfigEntries
            public static class Attacking implements ConfigGroup {
                @Getter
                private static int fireballCount = 17;
                @Getter
                private static float offsetAngle = 4.0F;
                @Getter
                private static float maxDepressAngle = 50.0F;
                @Getter
                private static boolean doFireballExplosion = false;
                @Getter
                private static float fireballExplosionPower = 0.5F;
            }
        }

        @Transitive
        @ConfigEntries
        public static class Hunger extends EntityConfigBase implements ConfigGroup {
            @Getter
            private static boolean spawn = true;
            @Getter
            private static int rate = 1;
            @Getter
            private static List<String> biomes = Arrays.asList("minecraft:swamp", "minecraft:swamp_hills", "minecraft:badlands_plateau", "minecraft:desert", "minecraft:desert_hills", "minecraft:badlands");
            @Getter
            private static double health = 20.0D;
            @Getter
            private static int maxEnchants = 5;
        }

        @Transitive
        @ConfigEntries
        public static class Kraken extends EntityConfigBase implements ConfigGroup {
            @Getter
            private static boolean spawn = true;
            @Getter
            private static int rate = 1;
            @Getter
            private static List<String> biomes = Arrays.asList("minecraft:deep_warm_ocean", "minecraft:deep_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_lukewarm_ocean");
            @Getter
            private static double health = 40.0D;
        }

        @Transitive
        @ConfigEntries
        public static class Meerkat extends EntityConfigBase implements ConfigGroup {
            @Getter
            private static boolean spawn = true;
            @Getter
            private static int rate = 1;
            @Getter
            private static List<String> biomes = Arrays.asList("minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes");
            @Getter
            private static double health = 10.0D;
        }
    }

    @Transitive
    @ConfigEntries
    public static class Generation implements ConfigGroup {
        @Getter
        private static boolean genPalmTrees = true;
        @Getter
        private static boolean genBaobabTrees = true;
        @Getter
        private static boolean baobabType = true;
    }

    @Transitive
    @ConfigEntries
    public static class Misc implements ConfigGroup {
        @Getter
        private static int helmetPenalty = 40;
    }

    @Override
    public String getID() {
        return "common";
    }
}