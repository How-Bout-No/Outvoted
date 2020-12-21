package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {

    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Outvoted.MOD_ID);

    public static final RegistryObject<SoundEvent> INFERNO_AMBIENT = SOUNDS.register("inferno_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "inferno_ambient")));
    public static final RegistryObject<SoundEvent> INFERNO_HURT = SOUNDS.register("inferno_hurt",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "inferno_hurt")));
    public static final RegistryObject<SoundEvent> INFERNO_DEATH = SOUNDS.register("inferno_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "inferno_death")));
    public static final RegistryObject<SoundEvent> INFERNO_BURN = SOUNDS.register("inferno_burn",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "inferno_burn")));
    public static final RegistryObject<SoundEvent> INFERNO_SHOOT = SOUNDS.register("inferno_shoot",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "inferno_shoot")));

    public static final RegistryObject<SoundEvent> KRAKEN_AMBIENT = SOUNDS.register("kraken_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "kraken_ambient")));
    public static final RegistryObject<SoundEvent> KRAKEN_HURT = SOUNDS.register("kraken_hurt",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "kraken_hurt")));
    public static final RegistryObject<SoundEvent> KRAKEN_DEATH = SOUNDS.register("kraken_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "kraken_death")));
    public static final RegistryObject<SoundEvent> KRAKEN_FLOP = SOUNDS.register("kraken_flop",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "kraken_flop")));

    public static final RegistryObject<SoundEvent> HUNGER_AMBIENT = SOUNDS.register("hunger_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_ambient")));
    public static final RegistryObject<SoundEvent> HUNGER_HURT = SOUNDS.register("hunger_hurt",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_hurt")));
    public static final RegistryObject<SoundEvent> HUNGER_DEATH = SOUNDS.register("hunger_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_death")));
    public static final RegistryObject<SoundEvent> HUNGER_BITE = SOUNDS.register("hunger_bite",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_bite")));
    public static final RegistryObject<SoundEvent> HUNGER_SPIT = SOUNDS.register("hunger_spit",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_spit")));
    public static final RegistryObject<SoundEvent> HUNGER_EAT = SOUNDS.register("hunger_eat",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_eat")));
    public static final RegistryObject<SoundEvent> HUNGER_DIG = SOUNDS.register("hunger_dig",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_digs")));
    public static final RegistryObject<SoundEvent> HUNGER_DIG_SAND = SOUNDS.register("hunger_dig_sand",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_dig_sand")));
}
