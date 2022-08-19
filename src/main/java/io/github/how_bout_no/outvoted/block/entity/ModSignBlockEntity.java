package io.github.how_bout_no.outvoted.block.entity;

import io.github.how_bout_no.outvoted.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntity extends SignBlockEntity {
    public ModSignBlockEntity(BlockPos arg, BlockState arg2) {
        super(arg, arg2);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModEntities.SIGN_BLOCK_ENTITIES.get();
    }
}
