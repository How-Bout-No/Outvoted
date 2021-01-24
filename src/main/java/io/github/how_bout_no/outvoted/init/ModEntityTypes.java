package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.InfernoEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Outvoted.MOD_ID);

    public static final RegistryObject<EntityType<InfernoEntity>> INFERNO = ENTITY_TYPES
            .register("inferno", () -> EntityType.Builder
                    .create(InfernoEntity::new, EntityClassification.MONSTER)
                    .immuneToFire()
                    .size(0.8F, 2.0F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "inferno").toString()));
    public static final RegistryObject<EntityType<HungerEntity>> HUNGER = ENTITY_TYPES
            .register("hunger", () -> EntityType.Builder
                    .create(HungerEntity::new, EntityClassification.CREATURE)
                    .size(1.0F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "hunger").toString()));
    public static final RegistryObject<EntityType<KrakenEntity>> KRAKEN = ENTITY_TYPES
            .register("kraken", () -> EntityType.Builder
                    .create(KrakenEntity::new, EntityClassification.MONSTER)
                    .size(1.2F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "kraken").toString()));
}
