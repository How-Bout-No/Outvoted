package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.entities.HungerEntity;
import com.hbn.outvoted.entities.InfernoEntity;
import com.hbn.outvoted.entities.KrakenEntity;
import com.hbn.outvoted.entities.SoulBlazeEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Outvoted.MOD_ID);

    public static final RegistryObject<EntityType<SoulBlazeEntity>> SOUL_BLAZE = ENTITY_TYPES
            .register("soul_blaze", () -> EntityType.Builder
                    .create(SoulBlazeEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.6F, 1.8F)
                    .trackingRange(8)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "soul_blaze").toString()));
    public static final RegistryObject<EntityType<InfernoEntity>> INFERNO = ENTITY_TYPES
            .register("inferno", () -> EntityType.Builder
                    .create(InfernoEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.8F, 2.0F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "inferno").toString()));
    public static final RegistryObject<EntityType<HungerEntity>> HUNGER = ENTITY_TYPES
            .register("hunger", () -> EntityType.Builder
                    .create(HungerEntity::new, EntityClassification.CREATURE)
                    .size(0.9F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "hunger").toString()));
    public static final RegistryObject<EntityType<KrakenEntity>> KRAKEN = ENTITY_TYPES
            .register("kraken", () -> EntityType.Builder
                    .create(KrakenEntity::new, EntityClassification.MONSTER)
                    .size(1.0F, 1.0F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "kraken").toString()));
}
