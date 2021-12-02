package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.entity.BurrowBlockEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Outvoted.MOD_ID, Registry.BLOCK_ENTITY_TYPE_KEY);
//    private static final Block[] blockListArr = blockList.toArray(value -> new Block[blockList.toArray().length]);

    public static final RegistrySupplier<BlockEntityType<BurrowBlockEntity>> BURROW = BLOCK_ENTITIES
            .register("burrow", () -> BlockEntityType.Builder
                    .create(BurrowBlockEntity::new, ModBlocks.BURROW.get())
                    .build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, "burrow")));

    private static Block[] getBlocks() {
        Object[] blocks = Registry.BLOCK.stream().filter((block) -> block.getDefaultState().getMaterial() == Material.WOOD).toArray();
        Block[] list = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            list[i] = (Block) blocks[i];
        }
        return list;
    }
}
