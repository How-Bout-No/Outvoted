package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.entity.CopperGolemEntity;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningRodBlock.class)
public abstract class MixinLightningRodBlock extends RodBlock {

    protected MixinLightningRodBlock(Settings settings) {
        super(settings);
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            this.trySpawnEntity(world, pos);
        }
    }

    private void trySpawnEntity(World world, BlockPos pos) {
        if (!world.isClient) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.LIGHTNING_ROD) {
                BlockState head = world.getBlockState(pos.down());
                BlockState body = world.getBlockState(pos.down(2));
                if (head.getBlock() == Blocks.CARVED_PUMPKIN && body.getBlock() instanceof OxidizableBlock) {
                    for (int k = 0; k < 3; ++k) {
                        world.setBlockState(pos.down(k), Blocks.AIR.getDefaultState(), 2);
                        world.syncWorldEvent(2001, pos.down(k), Block.getRawIdFromState(world.getBlockState(pos.down(k))));
                    }

                    CopperGolemEntity entity = ModEntityTypes.COPPER_GOLEM.get().create(world);
                    // This pos update is rly ugly but for some reason yaw doesnt get set properly??
                    entity.refreshPositionAndAngles(pos.down(2), head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F, entity.getPitch());
                    entity.updatePositionAndAngles(pos.getX() + 0.51D, pos.down(2).getY(), pos.getZ() + 0.51D, head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F, entity.getPitch());
                    world.spawnEntity(entity);
                    entity.prevBodyYaw = head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F;
                    entity.prevHeadYaw = head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F;
                    entity.bodyYaw = head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F;
                    entity.headYaw = head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F;
                    entity.updatePositionAndAngles(pos.getX() + 0.5D, pos.down(2).getY(), pos.getZ() + 0.5D, head.get(CarvedPumpkinBlock.FACING).asRotation() - 360F, entity.getPitch());
                    world.spawnEntity(entity);
                    ((ServerWorld) entity.getEntityWorld()).getChunkManager().sendToNearbyPlayers(entity, new EntityPositionS2CPacket(entity));
                    entity.setOxidizationLevel(((OxidizableBlock) body.getBlock()).getDegradationLevel().ordinal());
                    for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, entity.getBoundingBox().expand(5.0D))) {
                        Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity);
                    }

                    for (int m = 0; m < 3; ++m) {
                        world.updateNeighbors(pos.down(m), Blocks.AIR);
                    }
                }
            }
        }
    }
}
