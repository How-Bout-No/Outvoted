package io.github.how_bout_no.outvoted.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.Arrays;
import java.util.List;

@Config(name = "common")
public class OutvotedConfigCommon implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Entities entities = new Entities();

    public static class Entities {
        @ConfigEntry.Gui.CollapsibleObject
        public Entities.Wildfire wildfire = new Entities.Wildfire();

        @ConfigEntry.Gui.CollapsibleObject
        public Entities.Glutton glutton = new Entities.Glutton();

        @ConfigEntry.Gui.CollapsibleObject
        public Entities.Barnacle barnacle = new Entities.Barnacle();

        @ConfigEntry.Gui.CollapsibleObject
        public Entities.Meerkat meerkat = new Entities.Meerkat();

        public static class Wildfire implements EntityConfigBase {
            public boolean spawn = true;
            public int rate = 1;
            public List<String> biomes = Arrays.asList("minecraft:nether_wastes", "minecraft:basalt_deltas", "minecraft:crimson_forest", "minecraft:soul_sand_valley");
            public double health = 50.0D;

            @ConfigEntry.Gui.PrefixText
            @ConfigEntry.Gui.CollapsibleObject
            public Entities.WildfireAttacking attacking = new Entities.WildfireAttacking();
        }

        public static class WildfireAttacking {
            public int fireballCount = 17;
            public float offsetAngle = 4.0F;
            public float maxDepressAngle = 50.0F;
            public boolean doFireballExplosion = false;
            public float fireballExplosionPower = 0.5F;
        }

        public static class Glutton implements EntityConfigBase {
            public boolean spawn = true;
            public int rate = 5;
            public List<String> biomes = Arrays.asList("minecraft:swamp", "minecraft:swamp_hills", "minecraft:badlands_plateau", "minecraft:desert", "minecraft:desert_hills", "minecraft:badlands");
            public double health = 20.0D;
            @ConfigEntry.Gui.Tooltip
            public int maxEnchants = 5;
        }

        public static class Barnacle implements EntityConfigBase {
            public boolean spawn = true;
            public int rate = 2;
            public List<String> biomes = Arrays.asList("minecraft:deep_warm_ocean", "minecraft:deep_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_lukewarm_ocean");
            public double health = 40.0D;
        }

        public static class Meerkat implements EntityConfigBase {
            public boolean spawn = true;
            public int rate = 1;
            public List<String> biomes = Arrays.asList("minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes");
            public double health = 10.0D;
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
    }
}