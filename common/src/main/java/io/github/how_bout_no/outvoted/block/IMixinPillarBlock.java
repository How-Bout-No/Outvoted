package io.github.how_bout_no.outvoted.block;

import net.minecraft.nbt.NbtCompound;

public interface IMixinPillarBlock {
    void addTermite(NbtCompound compoundTag);
}
