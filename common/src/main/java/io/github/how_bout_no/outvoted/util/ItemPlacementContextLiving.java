package io.github.how_bout_no.outvoted.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class ItemPlacementContextLiving extends ItemPlacementContext {
    private final BlockPos placementPos;
    protected boolean canReplaceExisting;
    private final LivingEntity living;

    public ItemPlacementContextLiving(World world, LivingEntity livingEntity, Hand hand, ItemStack itemStack, BlockHitResult blockHitResult) {
        super(world, null, hand, itemStack, blockHitResult);
        this.canReplaceExisting = true;
        this.living = livingEntity;
        this.placementPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
        this.canReplaceExisting = world.getBlockState(blockHitResult.getBlockPos()).canReplace(this);
    }

    public BlockPos getBlockPos() {
        return this.canReplaceExisting ? super.getBlockPos() : this.placementPos;
    }

    public boolean canPlace() {
        return this.canReplaceExisting || this.getWorld().getBlockState(this.getBlockPos()).canReplace(this);
    }

    public boolean canReplaceExisting() {
        return this.canReplaceExisting;
    }

    public LivingEntity getEntity() {
        return this.living;
    }

    public Direction getPlayerLookDirection() {
        return Direction.getEntityFacingOrder(this.getEntity())[0];
    }

    public Direction getVerticalPlayerLookDirection() {
        return Direction.getLookDirectionForAxis(this.getEntity(), Direction.Axis.Y);
    }

    public Direction[] getPlacementDirections() {
        Direction[] directions = Direction.getEntityFacingOrder(this.getEntity());
        if (this.canReplaceExisting) {
            return directions;
        } else {
            Direction direction = this.getSide();

            int i;
            for (i = 0; i < directions.length && directions[i] != direction.getOpposite(); ++i) {
            }

            if (i > 0) {
                System.arraycopy(directions, 0, directions, 1, i);
                directions[0] = direction.getOpposite();
            }

            return directions;
        }
    }
}
