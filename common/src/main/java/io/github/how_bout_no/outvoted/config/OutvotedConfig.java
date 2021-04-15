package io.github.how_bout_no.outvoted.config;

import io.github.how_bout_no.outvoted.Outvoted;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = Outvoted.MOD_ID)
public class OutvotedConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Client client = new Client();

    public static class Client {
        public boolean creativetab = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Entities entities = new Entities();

    public static class Entities {

        @ConfigEntry.Gui.CollapsibleObject
        public Wildfire wildfire = new Wildfire();

        @ConfigEntry.Gui.CollapsibleObject
        public Hunger hunger = new Hunger();

        @ConfigEntry.Gui.CollapsibleObject
        public Kraken kraken = new Kraken();

        public static class Wildfire {
            public boolean spawn = true;
            public int rate = 1;
            public List<String> biomes = new ArrayList<>();
            public double health = 50.0D;
            public boolean variants = false;

            @ConfigEntry.Gui.CollapsibleObject
            public WildfireAttacking attacking = new WildfireAttacking();
        }

        public static class WildfireAttacking {
            public int fireballcount = 17;
            public float offsetangle = 4.0F;
            public float maxdepressangle = 50.0F;
            public boolean dofireballexplosion = false;
            public float fireballexplosionpower = 0.5F;
        }

        public static class Hunger {
            public boolean spawn = true;
            public int rate = 5;
            public List<String> biomes = new ArrayList<>();
            public double health = 20.0D;
            public int max_enchants = 5;
        }

        public static class Kraken {
            public boolean spawn = true;
            public int rate = 2;
            public List<String> biomes = new ArrayList<>();
            public double health = 40.0D;
        }
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Generation generation = new Generation();

    public static class Generation {
        public boolean genpalmtrees = true;
        public boolean genbaobabtrees = true;
        public int baobabtype = 0;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Misc misc = new Misc();

    public static class Misc {
        public int helmetpenalty = 40;
    }
}
