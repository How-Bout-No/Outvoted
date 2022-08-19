package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.util.IMixinBlaze;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Add variant creation + storage code to normal Blazes
 */
@Mixin(Blaze.class)
public abstract class MixinBlaze extends Monster implements IMixinBlaze {
    protected MixinBlaze(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Blaze.class, EntityDataSerializers.INT);

    @Override
    public void setVariant(int type) {
        this.entityData.set(VARIANT, type);
    }

    @Override
    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        int type = 0;
        if (reason != MobSpawnType.SPAWN_EGG && reason != MobSpawnType.DISPENSER) {
            if (worldIn.getBiome(this.blockPosition()).isBound()) {
                if (worldIn.getBiome(this.blockPosition()).is(Biomes.SOUL_SAND_VALLEY)) {
                    type = 1;
                }
            }
        } else {
            Block block = worldIn.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock();
            if (block == Blocks.SOUL_SAND || block == Blocks.SOUL_SOIL) {
                type = 1;
            }
        }
        this.setVariant(type);

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected Component getTypeName() {
        return this.getVariant() == 0 ? super.getTypeName() : new TranslatableComponent("entity.outvoted.blaze_s");
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void registerVariant(CallbackInfo info) {
        this.entityData.define(VARIANT, 0);
    }
}