package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import io.github.how_bout_no.outvoted.block.entity.ModSignBlockEntity;
import io.github.how_bout_no.outvoted.entity.*;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Outvoted.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Outvoted.MOD_ID);

    // Entities
    public static final RegistryObject<EntityType<Wildfire>> WILDFIRE = ENTITIES.register("wildfire", () -> EntityType.Builder
            .of(Wildfire::new, MobCategory.MONSTER)
            .fireImmune()
            .sized(0.8F, 2.0F)
            .build(new ResourceLocation(Outvoted.MOD_ID, "wildfire").toString()));
    public static final RegistryObject<EntityType<Glutton>> GLUTTON = ENTITIES.register("glutton", () -> EntityType.Builder
            .of(Glutton::new, MobCategory.CREATURE)
            .sized(1.0F, 1.2F)
            .build(new ResourceLocation(Outvoted.MOD_ID, "glutton").toString()));
    public static final RegistryObject<EntityType<Barnacle>> BARNACLE = ENTITIES.register("barnacle", () -> EntityType.Builder
            .of(Barnacle::new, MobCategory.MONSTER)
            .sized(1.2F, 1.2F)
            .build(new ResourceLocation(Outvoted.MOD_ID, "barnacle").toString()));
    public static final RegistryObject<EntityType<Meerkat>> MEERKAT = ENTITIES.register("meerkat", () -> EntityType.Builder
            .of(Meerkat::new, MobCategory.CREATURE)
            .sized(0.8F, 1.3F)
            .build(new ResourceLocation(Outvoted.MOD_ID, "meerkat").toString()));
        public static final RegistryObject<EntityType<Ostrich>> OSTRICH = ENTITIES.register("ostrich", () -> EntityType.Builder
                    .of(Ostrich::new, MobCategory.CREATURE)
                    .sized(1.0F, 1.7F)
                    .build(new ResourceLocation(Outvoted.MOD_ID, "ostrich").toString()));
    public static final RegistryObject<EntityType<Glare>> GLARE = ENTITIES.register("glare", () -> EntityType.Builder
            .of(Glare::new, MobCategory.CREATURE)
            .sized(0.8F, 0.95F)
            .build(new ResourceLocation(Outvoted.MOD_ID, "glare").toString()));
    public static final RegistryObject<EntityType<CopperGolem>> COPPER_GOLEM = ENTITIES.register("copper_golem", () -> EntityType.Builder
            .of(CopperGolem::new, MobCategory.MISC)
            .sized(0.9F, 1.2F)
            .build(new ResourceLocation(Outvoted.MOD_ID, "copper_golem").toString()));

    // Blocks
    public static final RegistryObject<BlockEntityType<BurrowBlockEntity>> BURROW = BLOCK_ENTITIES
            .register("burrow", () -> BlockEntityType.Builder
                    .of(BurrowBlockEntity::new, ModBlocks.BURROW.get())
                    .build(Util.fetchChoiceType(References.BLOCK_ENTITY, "burrow")));
    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> SIGN_BLOCK_ENTITIES = BLOCK_ENTITIES
            .register("sign_block_entity", () -> BlockEntityType.Builder
                    .of(ModSignBlockEntity::new,
                            ModBlocks.PALM_SIGN.get(),
                            ModBlocks.PALM_WALL_SIGN.get())
                    .build(Util.fetchChoiceType(References.BLOCK_ENTITY, "sign_block_entity")));
}
