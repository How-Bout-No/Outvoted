package io.github.how_bout_no.outvoted.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Config(name = "common")
public class OutvotedConfigCommon implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Entities entities = new Entities();

    public static class Entities {
        @ConfigEntry.Gui.CollapsibleObject
        public Wildfire wildfire = new Wildfire();

        @ConfigEntry.Gui.CollapsibleObject
        public Glutton glutton = new Glutton();

        @ConfigEntry.Gui.CollapsibleObject
        public Barnacle barnacle = new Barnacle();

        @ConfigEntry.Gui.CollapsibleObject
        public Meerkat meerkat = new Meerkat();

        @ConfigEntry.Gui.CollapsibleObject
        public Ostrich ostrich = new Ostrich();

        public static class Wildfire {
            public boolean spawn = true;
            public int rate = 1;
            public List<String> biomes = Arrays.asList("minecraft:nether_wastes", "minecraft:basalt_deltas", "minecraft:crimson_forest", "minecraft:soul_sand_valley");
            public double health = 50.0D;
            @ConfigEntry.Gui.PrefixText
            @ConfigEntry.Gui.CollapsibleObject
            public WildfireAttacking attacking = new WildfireAttacking();
        }

        public static class WildfireAttacking {
            public int fireballCount = 17;
            public float offsetAngle = 4.0F;
            public float maxDepressAngle = 50.0F;
            public boolean doFireballExplosion = false;
            public float fireballExplosionPower = 0.5F;
        }

        public static class Glutton {
            public boolean spawn = true;
            public int rate = 5;
            public List<String> biomes = Arrays.asList("#swamp", "#desert", "minecraft:badlands_plateau", "minecraft:badlands");
            public double health = 20.0D;
            public boolean stealEnchants = true;
            @ConfigEntry.Gui.Tooltip(count = 3)
            public boolean capEnchants = true;
            @ConfigEntry.Gui.Tooltip
            public int maxEnchants = 5;
        }

        public static class Barnacle {
            public boolean spawn = true;
            public int rate = 2;
            public List<String> biomes = Arrays.asList("minecraft:deep_warm_ocean", "minecraft:deep_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_lukewarm_ocean");
            public double health = 40.0D;
        }

        public static class Meerkat {
            public boolean spawn = true;
            @ConfigEntry.Gui.Tooltip
            public int burrowRate = 40;
            public List<String> biomes = Collections.singletonList("#desert");
            public double health = 10.0D;
        }

        public static class Ostrich {
            public boolean spawn = true;
            public int rate = 2;
            public List<String> biomes = Collections.singletonList("#savanna");
            public double health = 15.0D;
        }
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Generation generation = new Generation();

    public static class Generation {
        public boolean genPalmTrees = true;
        public boolean genBaobabTrees = true;
        @ConfigEntry.BoundedDiscrete(max = 1)
        @ConfigEntry.Gui.Tooltip(count = 2)
        public int baobabType = 0;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Misc misc = new Misc();

    public static class Misc {
        @ConfigEntry.Gui.Tooltip(count = 2)
        public int helmetPenalty = 40;
        @ConfigEntry.Gui.Excluded
        public boolean givePatchouliBookOnLogin = true;
    }
}