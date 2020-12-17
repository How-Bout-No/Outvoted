package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {

    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Outvoted.MOD_ID);

    public static final RegistryObject<SoundEvent> KRAKEN_AMBIENT = SOUNDS.register("kraken_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "kraken_ambient")));
    public static final RegistryObject<SoundEvent> KRAKEN_HIT = SOUNDS.register("kraken_hit",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "kraken_hit")));
    public static final RegistryObject<SoundEvent> KRAKEN_DEATH = SOUNDS.register("kraken_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "kraken_death")));

    public static final RegistryObject<SoundEvent> HUNGER_AMBIENT = SOUNDS.register("hunger_ambient",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_ambient")));
    public static final RegistryObject<SoundEvent> HUNGER_HIT = SOUNDS.register("hunger_hit",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_hit")));
    public static final RegistryObject<SoundEvent> HUNGER_DEATH = SOUNDS.register("hunger_death",
            () -> new SoundEvent(new ResourceLocation(Outvoted.MOD_ID, "hunger_death")));
}
