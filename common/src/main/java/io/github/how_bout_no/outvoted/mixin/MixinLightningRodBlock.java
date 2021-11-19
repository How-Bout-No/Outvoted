package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
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
    private static final Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = state -> state != null && (state.isOf(Blocks.CARVED_PUMPKIN) || state.isOf(Blocks.JACK_O_LANTERN));
    private static final Predicate<BlockState> IS_GOLEM_BODY_PREDICATE = state -> state != null && (state.isOf(Blocks.COPPER_BLOCK) || state.isOf(Blocks.WEATHERED_COPPER) || state.isOf(Blocks.EXPOSED_COPPER) || state.isOf(Blocks.OXIDIZED_COPPER));

    protected MixinLightningRodBlock(Settings settings) {
        super(settings);
    }

//    @Override
//    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
//        if (!oldState.isOf(state.getBlock())) {
//            this.trySpawnEntity(world, pos);
//        }
//        super.onBlockAdded(state, world, pos, oldState, notify);
//    }

    @Inject(method = "onBlockAdded", at = @At("HEAD"))
    public void trySpawn(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (!oldState.isOf(state.getBlock())) {
            this.trySpawnEntity(world, pos);
        }
    }

    private void trySpawnEntity(World world, BlockPos pos) {
        BlockPattern.Result result = this.getCopperGolemPattern().searchAround(world, pos);
        if (result != null) {
            BlockState head = result.translate(0, 1, 0).getBlockState();
            BlockState body = result.translate(0, 2, 0).getBlockState();
            for (int i = 0; i < this.getCopperGolemPattern().getHeight(); ++i) {
                CachedBlockPosition cachedBlockPosition = result.translate(0, i, 0);
                world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, cachedBlockPosition.getBlockPos(), Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
            }

            CopperGolemEntity entity = ModEntityTypes.COPPER_GOLEM.get().create(world);
            BlockPos blockPos = result.translate(0, 2, 0).getBlockPos();
            entity.refreshPositionAndAngles((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.05, (double) blockPos.getZ() + 0.5, head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F, entity.getPitch());
            entity.headYaw = head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F;
            entity.bodyYaw = entity.headYaw;
            entity.setRotation(head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F, entity.getPitch());
            entity.setRotations(new float[]{0, 0, 0, 0, 0, 0, head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F});
            world.spawnEntity(entity);
            entity.setOxidizationLevel(((OxidizableBlock) body.getBlock()).getDegradationLevel().ordinal());

            for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, entity.getBoundingBox().expand(5.0D))) {
                Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
            }

            for (int j = 0; j < 3; ++j) {
                CachedBlockPosition cachedBlockPosition2 = result.translate(0, j, 0);
                world.updateNeighbors(cachedBlockPosition2.getBlockPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getCopperGolemPattern() {
        if (this.copperGolemPattern == null) {
            this.copperGolemPattern = BlockPatternBuilder.start().aisle("!", "^", "#").where('!', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.LIGHTNING_ROD))).where('^', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_PREDICATE)).where('#', CachedBlockPosition.matchesBlockState(IS_GOLEM_BODY_PREDICATE)).build();
        }

        return this.copperGolemPattern;
    }
}
