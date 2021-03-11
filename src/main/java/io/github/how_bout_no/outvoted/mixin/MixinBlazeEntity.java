package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.util.IMixinBlazeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BlazeEntity.class)
public abstract class MixinBlazeEntity extends MonsterEntity implements IMixinBlazeEntity {

    protected MixinBlazeEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    private static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(BlazeEntity.class, DataSerializers.INT);

    @Override
    public void setVariant(int type) {
        this.entityData.set(VARIANT, type);
    }

    @Override
    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        int type;
        Block block = worldIn.getBlockState(new BlockPos(this.position().add(0D, -0.5D, 0D))).getBlock();
        if (block.is(Blocks.SOUL_SAND) || block.is(Blocks.SOUL_SOIL)) {
            type = 1;
        } else {
            type = 0;
        }
        this.setVariant(type);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Inject(method = "registerData", at = @At("TAIL"))
    protected void registerVariant(CallbackInfo info) {
        this.entityData.define(VARIANT, 0);
    }
}