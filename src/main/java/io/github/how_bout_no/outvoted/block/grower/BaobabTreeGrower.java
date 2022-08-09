package io.github.how_bout_no.outvoted.block.grower;


import io.github.how_bout_no.outvoted.init.ModFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BaobabTreeGrower extends AbstractTreeGrower {
    public BaobabTreeGrower() {
    }

    @Nullable
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean bl) {
        return ModFeatures.CONFIGURED_BAOBAB.getHolder().get();
    }
}
