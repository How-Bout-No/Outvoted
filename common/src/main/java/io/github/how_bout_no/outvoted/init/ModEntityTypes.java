package io.github.how_bout_no.outvoted.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Outvoted.MOD_ID, Registry.ENTITY_TYPE_KEY);

    public static final RegistrySupplier<EntityType<WildfireEntity>> WILDFIRE = ENTITY_TYPES
            .register("wildfire", () -> EntityType.Builder
                    .create(WildfireEntity::new, SpawnGroup.MONSTER)
                    .makeFireImmune()
                    .setDimensions(0.8F, 2.0F)
                    .build(new Identifier(Outvoted.MOD_ID, "wildfire").toString()));
    public static final RegistrySupplier<EntityType<GluttonEntity>> GLUTTON = ENTITY_TYPES
            .register("glutton", () -> EntityType.Builder
                    .create(GluttonEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(1.0F, 1.2F)
                    .build(new Identifier(Outvoted.MOD_ID, "glutton").toString()));
    public static final RegistrySupplier<EntityType<BarnacleEntity>> BARNACLE = ENTITY_TYPES
            .register("barnacle", () -> EntityType.Builder
                    .create(BarnacleEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(1.2F, 1.2F)
                    .build(new Identifier(Outvoted.MOD_ID, "barnacle").toString()));
    public static final RegistrySupplier<EntityType<GlareEntity>> GLARE = ENTITY_TYPES
            .register("glare", () -> EntityType.Builder
                    .create(GlareEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(1.0F, 1.4F)
                    .build(new Identifier(Outvoted.MOD_ID, "glare").toString()));
    public static final RegistrySupplier<EntityType<CopperGolemEntity>> COPPER_GOLEM = ENTITY_TYPES
            .register("copper_golem", () -> EntityType.Builder
                    .create(CopperGolemEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.9F, 1.2F)
                    .build(new Identifier(Outvoted.MOD_ID, "copper_golem").toString()));
}
