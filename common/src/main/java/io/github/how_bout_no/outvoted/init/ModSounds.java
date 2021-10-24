package io.github.how_bout_no.outvoted.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.how_bout_no.outvoted.Outvoted;
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

    public static final RegistrySupplier<SoundEvent> GLUTTON_AMBIENT = SOUNDS.register("glutton_ambient",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_ambient")));
    public static final RegistrySupplier<SoundEvent> GLUTTON_HURT = SOUNDS.register("glutton_hurt",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_hurt")));
    public static final RegistrySupplier<SoundEvent> GLUTTON_DEATH = SOUNDS.register("glutton_death",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_death")));
    public static final RegistrySupplier<SoundEvent> GLUTTON_BITE = SOUNDS.register("glutton_bite",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_bite")));
    public static final RegistrySupplier<SoundEvent> GLUTTON_SPIT = SOUNDS.register("glutton_spit",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_spit")));
    public static final RegistrySupplier<SoundEvent> GLUTTON_EAT = SOUNDS.register("glutton_eat",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_eat")));
    public static final RegistrySupplier<SoundEvent> GLUTTON_DIG = SOUNDS.register("glutton_dig",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_dig")));
    public static final RegistrySupplier<SoundEvent> GLUTTON_DIG_SAND = SOUNDS.register("glutton_dig_sand",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "glutton_dig_sand")));

    public static final RegistrySupplier<SoundEvent> BARNACLE_AMBIENT = SOUNDS.register("barnacle_ambient",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "barnacle_ambient")));
    public static final RegistrySupplier<SoundEvent> BARNACLE_HURT = SOUNDS.register("barnacle_hurt",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "barnacle_hurt")));
    public static final RegistrySupplier<SoundEvent> BARNACLE_DEATH = SOUNDS.register("barnacle_death",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "barnacle_death")));
    public static final RegistrySupplier<SoundEvent> BARNACLE_FLOP = SOUNDS.register("barnacle_flop",
            () -> new SoundEvent(new Identifier(Outvoted.MOD_ID, "barnacle_flop")));
}
