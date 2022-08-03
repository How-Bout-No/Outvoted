package io.github.how_bout_no.outvoted.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;


public class ItemPlacementContextLiving extends BlockPlaceContext {
    private final BlockPos placementPos;
    protected boolean canReplaceExisting;
    private final LivingEntity living;

    public ItemPlacementContextLiving(Level world, LivingEntity livingEntity, InteractionHand hand, ItemStack itemStack, BlockHitResult blockHitResult) {
        super(world, null, hand, itemStack, blockHitResult);
        this.replaceClicked = true;
        this.living = livingEntity;
        this.placementPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
        this.replaceClicked = world.getBlockState(blockHitResult.getBlockPos()).canBeReplaced(this);
    }

    public BlockPos getClickedPos() {
        return this.replaceClicked ? super.getClickedPos() : this.placementPos;
    }

    public boolean canPlace() {
        return this.replaceClicked || this.getLevel().getBlockState(this.getClickedPos()).canBeReplaced(this);
    }

    public boolean replacingClickedOnBlock() {
        return this.replaceClicked;
    }

    public LivingEntity getEntity() {
        return this.living;
    }

    public Direction getNearestLookingDirection() {
        return Direction.orderedByNearest(this.getEntity())[0];
    }

    public Direction getNearestLookingVerticalDirection() {
        return Direction.getFacingAxis(this.getEntity(), Direction.Axis.Y);
    }

    public Direction[] getNearestLookingDirections() {
        Direction[] directions = Direction.orderedByNearest(this.getEntity());
        if (this.replaceClicked) {
            return directions;
        } else {
            Direction direction = this.getClickedFace();

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
