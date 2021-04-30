package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.BarnacleEntity;
import io.github.how_bout_no.outvoted.entity.GluttonEntity;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
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
    public static final RegistrySupplier<EntityType<MeerkatEntity>> MEERKAT = ENTITY_TYPES
            .register("meerkat", () -> EntityType.Builder
                    .create(MeerkatEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(0.7F, 1.2F)
                    .build(new Identifier(Outvoted.MOD_ID, "meerkat").toString()));
}
