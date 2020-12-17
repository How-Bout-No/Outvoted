package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.item.InfernoHelmetItem;
import com.hbn.outvoted.item.InfernoShieldItem;
import com.hbn.outvoted.item.ModItem;
import com.hbn.outvoted.item.ModdedSpawnEggItem;
import net.minecraft.item.Item;
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
}
