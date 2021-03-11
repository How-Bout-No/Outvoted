package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.HungerEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.entity.KrakenEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Outvoted.MOD_ID);

    public static final RegistryObject<EntityType<WildfireEntity>> WILDFIRE = ENTITY_TYPES
            .register("wildfire", () -> EntityType.Builder
                    .of(WildfireEntity::new, EntityClassification.MONSTER)
                    .fireImmune()
                    .sized(0.8F, 2.0F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "wildfire").toString()));
    public static final RegistryObject<EntityType<HungerEntity>> HUNGER = ENTITY_TYPES
            .register("hunger", () -> EntityType.Builder
                    .of(HungerEntity::new, EntityClassification.CREATURE)
                    .sized(1.0F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "hunger").toString()));
    public static final RegistryObject<EntityType<KrakenEntity>> KRAKEN = ENTITY_TYPES
            .register("kraken", () -> EntityType.Builder
                    .of(KrakenEntity::new, EntityClassification.MONSTER)
                    .sized(1.2F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "kraken").toString()));
}
