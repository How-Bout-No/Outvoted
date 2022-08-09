package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.entity.CopperGolem;
import io.github.how_bout_no.outvoted.init.ModEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(LightningRodBlock.class)
public abstract class MixinLightningRodBlock extends RodBlock {
    @Nullable
    private BlockPattern copperGolemPattern;
    private static final Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = state -> state != null && (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN));
    private static final Predicate<BlockState> IS_GOLEM_BODY_PREDICATE = state -> state != null && (state.is(Blocks.COPPER_BLOCK) || state.is(Blocks.WEATHERED_COPPER) || state.is(Blocks.EXPOSED_COPPER) || state.is(Blocks.OXIDIZED_COPPER));

    protected MixinLightningRodBlock(Properties settings) {
        super(settings);
    }

//    @Override
//    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
//        if (!oldState.isOf(state.getBlock())) {
//            this.trySpawnEntity(world, pos);
//        }
//        super.onBlockAdded(state, world, pos, oldState, notify);
//    }

    @Inject(method = "onPlace", at = @At("HEAD"))
    public void trySpawn(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnEntity(world, pos);
        }
    }

    private void trySpawnEntity(Level world, BlockPos pos) {
        BlockPattern.BlockPatternMatch result = this.getCopperGolemPattern().find(world, pos);
        if (result != null) {
            BlockState head = result.getBlock(0, 1, 0).getState();
            BlockState body = result.getBlock(0, 2, 0).getState();
            for (int i = 0; i < this.getCopperGolemPattern().getHeight(); ++i) {
                BlockInWorld cachedBlockPosition = result.getBlock(0, i, 0);
                world.setBlock(cachedBlockPosition.getPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, cachedBlockPosition.getPos(), Block.getId(cachedBlockPosition.getState()));
            }

            CopperGolem entity = ModEntities.COPPER_GOLEM.get().create(world);
            BlockPos blockPos = result.getBlock(0, 2, 0).getPos();
            entity.moveTo((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.05, (double) blockPos.getZ() + 0.5, head.getValue(CarvedPumpkinBlock.FACING).toYRot() - 360F, entity.getXRot());
            entity.yHeadRot = head.getValue(CarvedPumpkinBlock.FACING).toYRot() - 360F;
            entity.yBodyRot = entity.yHeadRot;
            entity.setRot(head.getValue(CarvedPumpkinBlock.FACING).toYRot() - 360F, entity.getXRot());
            entity.setRotations(new float[]{0, 0, 0, 0, 0, 0, head.getValue(CarvedPumpkinBlock.FACING).toYRot() - 360F});
            world.addFreshEntity(entity);
            entity.setOxidizationLevel(((WeatheringCopperFullBlock) body.getBlock()).getAge().ordinal());

            for (ServerPlayer serverPlayerEntity : world.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
            }

            for (int j = 0; j < 3; ++j) {
                BlockInWorld cachedBlockPosition2 = result.getBlock(0, j, 0);
                world.blockUpdated(cachedBlockPosition2.getPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getCopperGolemPattern() {
        if (this.copperGolemPattern == null) {
            this.copperGolemPattern = BlockPatternBuilder.start().aisle("!", "^", "#").where('!', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.LIGHTNING_ROD))).where('^', BlockInWorld.hasState(IS_GOLEM_HEAD_PREDICATE)).where('#', BlockInWorld.hasState(IS_GOLEM_BODY_PREDICATE)).build();
        }

        return this.copperGolemPattern;
    }
}
