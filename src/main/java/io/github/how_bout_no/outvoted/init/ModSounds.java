package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Outvoted.MOD_ID);

    public static final RegistryObject<SoundEvent> WILDFIRE_AMBIENT = SOUNDS.register("wildfire_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "wildfire_ambient")));
    public static final RegistryObject<SoundEvent> WILDFIRE_HURT = SOUNDS.register("wildfire_hurt",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "wildfire_hurt")));
    public static final RegistryObject<SoundEvent> WILDFIRE_DEATH = SOUNDS.register("wildfire_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "wildfire_death")));
    public static final RegistryObject<SoundEvent> WILDFIRE_BURN = SOUNDS.register("wildfire_burn",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "wildfire_burn")));
    public static final RegistryObject<SoundEvent> WILDFIRE_SHOOT = SOUNDS.register("wildfire_shoot",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "wildfire_shoot")));

    public static final RegistryObject<SoundEvent> GLUTTON_AMBIENT = SOUNDS.register("glutton_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_ambient")));
    public static final RegistryObject<SoundEvent> GLUTTON_HURT = SOUNDS.register("glutton_hurt",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_hurt")));
    public static final RegistryObject<SoundEvent> GLUTTON_DEATH = SOUNDS.register("glutton_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_death")));
    public static final RegistryObject<SoundEvent> GLUTTON_BITE = SOUNDS.register("glutton_bite",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_bite")));
    public static final RegistryObject<SoundEvent> GLUTTON_SPIT = SOUNDS.register("glutton_spit",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_spit")));
    public static final RegistryObject<SoundEvent> GLUTTON_EAT = SOUNDS.register("glutton_eat",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_eat")));
    public static final RegistryObject<SoundEvent> GLUTTON_DIG = SOUNDS.register("glutton_dig",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_dig")));
    public static final RegistryObject<SoundEvent> GLUTTON_DIG_SAND = SOUNDS.register("glutton_dig_sand",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "glutton_dig_sand")));

    public static final RegistryObject<SoundEvent> BARNACLE_AMBIENT = SOUNDS.register("barnacle_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "barnacle_ambient")));
    public static final RegistryObject<SoundEvent> BARNACLE_HURT = SOUNDS.register("barnacle_hurt",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "barnacle_hurt")));
    public static final RegistryObject<SoundEvent> BARNACLE_DEATH = SOUNDS.register("barnacle_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "barnacle_death")));
    public static final RegistryObject<SoundEvent> BARNACLE_FLOP = SOUNDS.register("barnacle_flop",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "barnacle_flop")));

    public static final RegistryObject<SoundEvent> MEERKAT_AMBIENT = SOUNDS.register("meerkat_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "meerkat_ambient")));
    public static final RegistryObject<SoundEvent> MEERKAT_HURT = SOUNDS.register("meerkat_hurt",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "meerkat_hurt")));
    public static final RegistryObject<SoundEvent> MEERKAT_DEATH = SOUNDS.register("meerkat_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "meerkat_death")));

    public static final RegistryObject<SoundEvent> OSTRICH_AMBIENT = SOUNDS.register("ostrich_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "ostrich_ambient")));
}
