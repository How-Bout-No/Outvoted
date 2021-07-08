package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.structure.NetherFortressGenerator;
import net.minecraft.structure.StructurePieceType;
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
public abstract class MixinCorridorExit extends NetherFortressGenerator.Piece {
    protected MixinCorridorExit(StructurePieceType structurePieceType, int i, BlockBox blockBox) {
        super(structurePieceType, i, blockBox);
    }

    protected void addEntity(StructureWorldAccess structureWorldAccess, EntityType<? extends MobEntity> entityType, int x, int y, int z, BlockBox blockBox) {
        BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
        if (blockBox.contains(blockPos)) {
            MobEntity livingEntity = entityType.create(structureWorldAccess.toServerWorld());
            livingEntity.setPersistent();
            livingEntity.refreshPositionAndAngles(blockPos, 0.0F, 0.0F);
            livingEntity.initialize(structureWorldAccess, structureWorldAccess.getLocalDifficulty(livingEntity.getBlockPos()), SpawnReason.STRUCTURE, null, null);
            structureWorldAccess.spawnEntity(livingEntity);

        }
    }

    @Inject(method = "generate", at = @At("RETURN"))
    private void injectSpawn(StructureWorldAccess structureWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        this.addEntity(structureWorldAccess, ModEntityTypes.WILDFIRE.get(), 5, 5, 7, boundingBox);
    }
}
