package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Outvoted.MOD_ID, Registry.SOUND_EVENT_KEY);

    public static final RegistrySupplier<SoundEvent> WILDFIRE_AMBIENT = SOUNDS.register("wildfire_ambient",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "wildfire_ambient")));
    public static final RegistrySupplier<SoundEvent> WILDFIRE_HURT = SOUNDS.register("wildfire_hurt",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "wildfire_hurt")));
    public static final RegistrySupplier<SoundEvent> WILDFIRE_DEATH = SOUNDS.register("wildfire_death",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "wildfire_death")));
    public static final RegistrySupplier<SoundEvent> WILDFIRE_BURN = SOUNDS.register("wildfire_burn",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "wildfire_burn")));
    public static final RegistrySupplier<SoundEvent> WILDFIRE_SHOOT = SOUNDS.register("wildfire_shoot",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "wildfire_shoot")));

    public static final RegistrySupplier<SoundEvent> KRAKEN_AMBIENT = SOUNDS.register("kraken_ambient",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "kraken_ambient")));
    public static final RegistrySupplier<SoundEvent> KRAKEN_HURT = SOUNDS.register("kraken_hurt",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "kraken_hurt")));
    public static final RegistrySupplier<SoundEvent> KRAKEN_DEATH = SOUNDS.register("kraken_death",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "kraken_death")));
    public static final RegistrySupplier<SoundEvent> KRAKEN_FLOP = SOUNDS.register("kraken_flop",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "kraken_flop")));

    public static final RegistrySupplier<SoundEvent> HUNGER_AMBIENT = SOUNDS.register("hunger_ambient",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_ambient")));
    public static final RegistrySupplier<SoundEvent> HUNGER_HURT = SOUNDS.register("hunger_hurt",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_hurt")));
    public static final RegistrySupplier<SoundEvent> HUNGER_DEATH = SOUNDS.register("hunger_death",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_death")));
    public static final RegistrySupplier<SoundEvent> HUNGER_BITE = SOUNDS.register("hunger_bite",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_bite")));
    public static final RegistrySupplier<SoundEvent> HUNGER_SPIT = SOUNDS.register("hunger_spit",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_spit")));
    public static final RegistrySupplier<SoundEvent> HUNGER_EAT = SOUNDS.register("hunger_eat",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_eat")));
    public static final RegistrySupplier<SoundEvent> HUNGER_DIG = SOUNDS.register("hunger_dig",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_dig")));
    public static final RegistrySupplier<SoundEvent> HUNGER_DIG_SAND = SOUNDS.register("hunger_dig_sand",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "hunger_dig_sand")));
}
