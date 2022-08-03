package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.NetherBridgePieces;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NetherBridgePieces.CastleEntrance.class)
public abstract class MixinCorridorExit extends NetherBridgePieces.NetherBridgePiece {
    protected MixinCorridorExit(StructurePieceType structurePieceType, int i, BoundingBox blockBox) {
        super(structurePieceType, i, blockBox);
    }

    protected void addEntity(WorldGenLevel structureWorldAccess, EntityType<? extends Mob> entityType, int x, int y, int z, BoundingBox blockBox) {
        BlockPos blockPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
        if (blockBox.isInside(blockPos)) {
            Mob livingEntity = entityType.create(structureWorldAccess.getLevel());
            livingEntity.setPersistenceRequired();
            livingEntity.moveTo(blockPos, 0.0F, 0.0F);
            livingEntity.finalizeSpawn(structureWorldAccess, structureWorldAccess.getCurrentDifficultyAt(livingEntity.blockPosition()), MobSpawnType.STRUCTURE, null, null);
            structureWorldAccess.addFreshEntity(livingEntity);
        }
    }

    @Inject(method = "postProcess", at = @At("TAIL"))
    private void injectSpawn(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfo cir) {
        this.addEntity(worldGenLevel, ModEntityTypes.WILDFIRE.get(), 5, 5, 7, boundingBox);
    }
}
