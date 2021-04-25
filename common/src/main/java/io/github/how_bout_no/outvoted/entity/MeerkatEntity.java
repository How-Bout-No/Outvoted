package io.github.how_bout_no.outvoted.entity;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.util.EntityUtils;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.UUID;

public class MeerkatEntity extends TameableEntity implements IAnimatable {
    private static final TrackedData<BlockPos> STRUCTURE_POS = DataTracker.registerData(MeerkatEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(Items.COD, Items.SALMON);
    private int animtimer = 0;

    public MeerkatEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        EntityUtils.setConfigHealth(this, Outvoted.config.common.entities.meerkat.health);
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("standing"));
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walking"));
            animtimer = 0;
        } else if (animtimer >= 10 && !this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("standing"));
        } else if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("standingalt"));
        }
        animtimer++;
        return PlayState.CONTINUE;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeerkatEntity.VentureGoal(this, 1.25D));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.add(4, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CAT_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    public static DefaultAttributeContainer.Builder setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
    }

    private BlockPos getStructurePos() {
        if (this.dataTracker.get(STRUCTURE_POS) == null) setStructurePos(BlockPos.ORIGIN);
        return this.dataTracker.get(STRUCTURE_POS);
    }

    private void setStructurePos(BlockPos pos) {
        this.dataTracker.set(STRUCTURE_POS, pos);
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.put("StructPos", NbtHelper.fromBlockPos(this.getStructurePos()));
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setStructurePos(NbtHelper.toBlockPos(tag.getCompound("StructPos")));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STRUCTURE_POS, BlockPos.ORIGIN);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    private BlockPos findStructure(StructureFeature<?> structureFeature) {
        if (!this.world.isClient) {
            BlockPos blockPos = new BlockPos(this.getBlockPos());
            if (this.getServer().getOverworld() != null) {
                ServerWorld serverWorld = this.getServer().getOverworld();
                System.out.println(serverWorld.getBiome(blockPos).getScale());
                BlockPos blockPos2 = serverWorld.locateStructure(structureFeature, blockPos, (int) Math.floor(10 * serverWorld.getBiome(blockPos).getScale()), false);
                return blockPos2;
            }
        }
        return null;
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (this.world.isClient) {
            if (this.isTamed() && this.isOwner(player)) {
                return ActionResult.SUCCESS;
            } else {
                return !this.isBreedingItem(itemStack) || !(this.getHealth() < this.getMaxHealth()) && this.isTamed() ? ActionResult.PASS : ActionResult.SUCCESS;
            }
        } else {
            ActionResult actionResult;
            if (this.isTamed()) {
                if (this.isOwner(player)) {
                    if (itemStack.getItem().isFood() && this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                        this.eat(player, itemStack);
                        this.heal((float) itemStack.getItem().getFoodComponent().getHunger());
                        return ActionResult.CONSUME;
                    } else if (itemStack.getItem() == Items.STICK) {
                        BlockPos pyramidPos = findStructure(StructureFeature.DESERT_PYRAMID);
                        BlockPos treasurePos = findStructure(StructureFeature.BURIED_TREASURE);
                        BlockPos struct = null;
                        if (pyramidPos != null) {
                            struct = pyramidPos;
                        }
                        if (treasurePos != null) {
                            struct = treasurePos;
                        }
                        if (pyramidPos != null && treasurePos != null) {
                            if (getDistance(this.getBlockPos().getX(), this.getBlockPos().getZ(), treasurePos.getX(), treasurePos.getZ()) > getDistance(this.getBlockPos().getX(), this.getBlockPos().getZ(), pyramidPos.getX(), pyramidPos.getZ())) {
                                struct = pyramidPos;
                            }
                        }
                        setStructurePos(struct);
                        return ActionResult.CONSUME;
                    }

                    actionResult = super.interactMob(player, hand);
                    if (!actionResult.isAccepted() || this.isBaby()) {
                        System.out.println(this.isSitting());
                        this.setSitting(!this.isSitting());
                    }

                    return actionResult;
                }
            } else if (this.isBreedingItem(itemStack)) {
                this.eat(player, itemStack);
                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.setSitting(true);
                    this.world.sendEntityStatus(this, (byte) 7);
                } else {
                    this.world.sendEntityStatus(this, (byte) 6);
                }

                this.setPersistent();
                return ActionResult.CONSUME;
            }

            actionResult = super.interactMob(player, hand);
            if (actionResult.isAccepted()) {
                this.setPersistent();
            }

            return actionResult;
        }
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isTamed() && this.age > 2400;
    }

    private static float getDistance(int a, int b, int x, int y) {
        int i = x - a;
        int j = y - b;
        return MathHelper.sqrt((float) (i * i + j * j));
    }

    static class VentureGoal extends Goal {
        protected final MeerkatEntity mob;
        public final double speed;
        protected BlockPos targetPos;
        private boolean reached = false;

        public VentureGoal(MeerkatEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.LOOK));
        }

        public boolean canStart() {
            return this.mob.getStructurePos() != BlockPos.ORIGIN && !this.mob.isSitting();
        }

        public boolean shouldContinue() {
            return super.shouldContinue() && !hasReached();
        }

        public void stop() {
            this.mob.setStructurePos(BlockPos.ORIGIN);
        }

        public void start() {
            this.targetPos = this.mob.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, this.mob.getStructurePos());
            this.startMovingToTarget();
        }

        protected void startMovingToTarget() {
            this.mob.getNavigation().startMovingTo((double) ((float) this.targetPos.getX()) + 0.5D, (double) (this.targetPos.getY()), (double) ((float) this.targetPos.getZ()) + 0.5D, this.speed);
        }

        protected boolean hasReached() {
            return this.reached;
        }

        public void tick() {
            if (this.mob.getOwner() != null) {
                if (this.mob.squaredDistanceTo(this.mob.getOwner()) > 64) {
                    this.mob.getNavigation().stop();
                    this.mob.getLookControl().lookAt(this.mob.getOwner().getX(), this.mob.getOwner().getEyeY(), this.mob.getOwner().getZ());
                } else if (this.mob.getNavigation().isIdle() || this.mob.getNavigation().getCurrentPath() == null) {
                    this.startMovingToTarget();
                }
                if (this.mob.squaredDistanceTo(this.mob.getStructurePos().getX(), this.mob.getY(), this.mob.getStructurePos().getZ()) <= 100)
                    this.reached = true;
            }
        }
    }

    @Nullable
    @Override
    public MeerkatEntity createChild(ServerWorld world, PassiveEntity entity) {
        MeerkatEntity meerkatEntity = ModEntityTypes.MEERKAT.get().create(world);
        UUID uuid = this.getOwnerUuid();
        if (uuid != null) {
            meerkatEntity.setOwnerUuid(uuid);
            meerkatEntity.setTamed(true);
        }

        return meerkatEntity;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController(this, "controller", 2, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
