package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.util.IMixinBlazeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Add variant creation + storage code to normal Blazes
 */
@Mixin(BlazeEntity.class)
public abstract class MixinBlazeEntity extends HostileEntity implements IMixinBlazeEntity {
    protected MixinBlazeEntity(EntityType<? extends HostileEntity> type, World worldIn) {
        super(type, worldIn);
    }

    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(BlazeEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Override
    public void setVariant(int type) {
        this.dataTracker.set(VARIANT, type);
    }

    @Override
    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("Variant", this.getVariant());
    }

    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, @Nullable EntityData spawnDataIn, @Nullable NbtCompound dataTag) {
        int type = 0;
        if (reason != SpawnReason.SPAWN_EGG && reason != SpawnReason.DISPENSER) {
            if (worldIn.getBiomeKey(this.getBlockPos()).isPresent()) {
                if (worldIn.getBiomeKey(this.getBlockPos()).get() == BiomeKeys.SOUL_SAND_VALLEY) {
                    type = 1;
                }
            }
        } else {
            Block block = worldIn.getBlockState(this.getVelocityAffectingPos()).getBlock();
            if (block == Blocks.SOUL_SAND || block == Blocks.SOUL_SOIL) {
                type = 1;
            }
        }
        this.setVariant(type);

        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void registerVariant(CallbackInfo info) {
        this.dataTracker.startTracking(VARIANT, 0);
    }
}