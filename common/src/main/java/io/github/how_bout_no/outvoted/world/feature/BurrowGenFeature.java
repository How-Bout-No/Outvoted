package io.github.how_bout_no.outvoted.world.feature;

import com.mojang.serialization.Codec;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class BurrowGenFeature extends Feature<DefaultFeatureConfig> {
    public BurrowGenFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutable2 = new BlockPos.Mutable();
        int k = blockPos.getX();
        int l = blockPos.getZ();
        int m = structureWorldAccess.getTopY(Heightmap.Type.MOTION_BLOCKING, k, l);
        mutable.set(k, m, l);
        mutable2.set(mutable).move(Direction.DOWN, 1);
        if (structureWorldAccess.getBlockState(mutable2).isOf(Blocks.SAND)) {
            structureWorldAccess.setBlockState(mutable2, ModBlocks.BURROW.get().getDefaultState(), 0);
            for (int i = 0; i < random.nextInt(2) + 2; i++) {
                MeerkatEntity livingEntity = ModEntityTypes.MEERKAT.get().create(structureWorldAccess.toServerWorld());
                blockPos.add(livingEntity.getParticleX(2.0D), 0, livingEntity.getParticleZ(2.0D));
                livingEntity.refreshPositionAndAngles(blockPos, livingEntity.getYaw(), livingEntity.getPitch());
                livingEntity.initialize(structureWorldAccess, structureWorldAccess.getLocalDifficulty(blockPos), SpawnReason.NATURAL, null, null);
                structureWorldAccess.spawnEntity(livingEntity);
            }
        }

        return true;
    }
}