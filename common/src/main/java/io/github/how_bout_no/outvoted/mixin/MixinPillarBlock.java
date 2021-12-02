package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.block.IMixinPillarBlock;
import io.github.how_bout_no.outvoted.entity.TermiteEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(PillarBlock.class)
public abstract class MixinPillarBlock extends Block implements IMixinPillarBlock {
    protected List<NbtCompound> termiteData;

    public MixinPillarBlock(Settings settings) {
        super(settings);
    }

    {
        this.termiteData = new ArrayList<>();
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    public void addProp(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if (this.material == Material.WOOD) builder.add(TermiteEntity.Nest.NESTED);
    }

    public void addTermite(NbtCompound compoundTag) {
        this.termiteData.add(compoundTag);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (this.asBlock().isIn(BlockTags.LOGS_THAT_BURN) && !world.isClient()) {
            spawnTermites((ServerWorld) world, pos);
        }
    }

    private void spawnTermites(ServerWorld world, BlockPos pos) {
        if (world.getBlockState(pos).get(TermiteEntity.Nest.NESTED) > 0)
            for (NbtCompound nbt : this.termiteData)
                spawnTermite(world, pos, nbt);
        this.termiteData.clear();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (!this.termiteData.isEmpty() && random.nextDouble() > 0.9)
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), this.getParticleX(0.5D, pos, random) + 0.5D, pos.getY(), this.getParticleZ(0.5D, pos, random) + 0.5D, (random.nextDouble() - 0.5D) * 2.0D, random.nextDouble(), (random.nextDouble() - 0.5D) * 2.0D);
    }

    private boolean spawnTermite(ServerWorld world, BlockPos pos, NbtCompound entityData) {
        Entity entity = EntityType.loadEntityWithPassengers(entityData, world, (entityx) -> entityx);
        if (entity != null) {
            if (entity instanceof TermiteEntity) {
                TermiteEntity termiteEntity = (TermiteEntity) entity;

                termiteEntity.refreshPositionAndAngles((double) pos.getX() + 0.5D + world.random.nextDouble() / 10, pos.getY(), (double) pos.getZ() + 0.5D + world.random.nextDouble() / 10, 0.0F, 0.0F);
            }

            boolean bl = world.spawnEntity(entity);
            if (bl) ((TermiteEntity) entity).playSpawnEffects();
            return bl;
        } else {
            return false;
        }
    }

    public double offsetX(double widthScale, BlockPos pos) {
        return pos.getX() + widthScale;
    }

    public double getParticleX(double widthScale, BlockPos pos, Random random) {
        return this.offsetX((2.0D * random.nextDouble() - 1.0D) * widthScale, pos);
    }

    public double offsetZ(double widthScale, BlockPos pos) {
        return pos.getZ() + widthScale;
    }

    public double getParticleZ(double widthScale, BlockPos pos, Random random) {
        return this.offsetZ((2.0D * random.nextDouble() - 1.0D) * widthScale, pos);
    }
}
