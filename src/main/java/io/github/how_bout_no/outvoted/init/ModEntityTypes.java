package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Outvoted.MOD_ID);

    public static final RegistryObject<EntityType<WildfireEntity>> WILDFIRE = ENTITY_TYPES
            .register("wildfire", () -> EntityType.Builder
                    .of(WildfireEntity::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(0.8F, 2.0F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "wildfire").toString()));
    public static final RegistryObject<EntityType<GluttonEntity>> GLUTTON = ENTITY_TYPES
            .register("glutton", () -> EntityType.Builder
                    .of(GluttonEntity::new, MobCategory.CREATURE)
                    .sized(1.0F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "glutton").toString()));
    public static final RegistryObject<EntityType<BarnacleEntity>> BARNACLE = ENTITY_TYPES
            .register("barnacle", () -> EntityType.Builder
                    .of(BarnacleEntity::new, MobCategory.MONSTER)
                    .sized(1.2F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "barnacle").toString()));
    public static final RegistryObject<EntityType<GlareEntity>> GLARE = ENTITY_TYPES
            .register("glare", () -> EntityType.Builder
                    .of(GlareEntity::new, MobCategory.CREATURE)
                    .sized(0.8F, 0.95F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "glare").toString()));
    public static final RegistryObject<EntityType<CopperGolemEntity>> COPPER_GOLEM = ENTITY_TYPES
            .register("copper_golem", () -> EntityType.Builder
                    .of(CopperGolemEntity::new, MobCategory.MISC)
                    .sized(0.9F, 1.2F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "copper_golem").toString()));
}
