package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Outvoted.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY);

    public static final RegistrySupplier<BlockEntityType<BurrowBlockEntity>> BURROW = BLOCK_ENTITIES
            .register("burrow", () -> BlockEntityType.Builder
                    .create(BurrowBlockEntity::new, ModBlocks.BURROW.get())
                    .build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, "burrow")));
}
