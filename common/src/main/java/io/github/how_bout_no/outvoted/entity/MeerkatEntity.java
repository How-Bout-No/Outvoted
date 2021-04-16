package io.github.how_bout_no.outvoted.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@SuppressWarnings("EntityConstructor")
public class MeerkatEntity extends AnimalEntity implements IAnimatable {
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.COD, Items.SALMON});

    public MeerkatEntity(EntityType<? extends MeerkatEntity> type, World worldIn) {
        super(type, worldIn);
    }

    private BlockPos findStructure() {
        if (!this.world.isClient) {
            BlockPos blockPos = new BlockPos(this.getBlockPos());
            if (this.getServer().getOverworld() != null) {
                System.out.println(this.getServer().getOverworld().getBiome(blockPos).getScale());
                BlockPos blockPos2 = this.getServer().getOverworld().locateStructure(StructureFeature.DESERT_PYRAMID, blockPos, 100, false);
                return blockPos2;
            }
        }
        return null;
    }

    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (this.isBreedingItem(itemStack) && player.squaredDistanceTo(this) < 9.0D) {
            this.eat(player, itemStack);
            if (!this.world.isClient) {
                BlockPos structurePos = findStructure();
                System.out.println(structurePos);
//                if (this.random.nextInt(3) == 0) {
//                    this.world.sendEntityStatus(this, (byte)41);
//                } else {
//                    this.world.sendEntityStatus(this, (byte)40);
//                }
            }

            return ActionResult.success(this.world.isClient);
        } else {
            return super.interactMob(player, hand);
        }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}
