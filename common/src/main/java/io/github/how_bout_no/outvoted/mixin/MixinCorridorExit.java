package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.entity.WildfireEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.NetherFortressGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(NetherFortressGenerator.CorridorExit.class)
public abstract class MixinCorridorExit {
    @Inject(method = "generate", at = @At("RETURN"))
    private void injectSpawn(StructureWorldAccess structureWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("pleasegodwhy");
        WildfireEntity livingEntity = ModEntityTypes.WILDFIRE.get().create(structureWorldAccess.toServerWorld());
        livingEntity.setPersistent();
        livingEntity.refreshPositionAndAngles(blockPos.add(random.nextInt(7) - 3 + blockPos.getX(), blockPos.getY(), random.nextInt(7) - 3 + blockPos.getZ()), 0.0F, 0.0F);
        livingEntity.initialize(structureWorldAccess, structureWorldAccess.getLocalDifficulty(livingEntity.getBlockPos()), SpawnReason.STRUCTURE, (EntityData) null, (CompoundTag) null);
        structureWorldAccess.spawnEntity(livingEntity);
    }
}
