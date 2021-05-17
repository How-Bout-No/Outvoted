package io.github.how_bout_no.outvoted.block.entity;

import com.google.common.collect.Lists;
import io.github.how_bout_no.outvoted.block.BurrowBlock;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.init.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class BurrowBlockEntity extends BlockEntity implements Tickable {
    private final List<BurrowBlockEntity.Meerkat> meerkats = Lists.newArrayList();
    @Nullable
    private BlockPos flowerPos = null;

    public BurrowBlockEntity() {
        super(ModBlockEntityTypes.BURROW.get());
    }

    public boolean hasNoMeerkats() {
        return this.meerkats.isEmpty();
    }

    public boolean isFullOfMeerkats() {
        return this.meerkats.size() == 3;
    }

    private List<Entity> tryReleaseMeerkat(BlockState state, BurrowBlockEntity.MeerkatState meerkatState) {
        List<Entity> list = Lists.newArrayList();
        this.meerkats.removeIf((meerkat) -> {
            return this.releaseMeerkat(state, meerkat, list, meerkatState);
        });
        return list;
    }

    public void tryEnterBurrow(Entity entity, boolean hasNectar) {
        this.tryEnterBurrow(entity, hasNectar, 0);
    }

    public int getMeerkatCount() {
        return this.meerkats.size();
    }

    public void tryEnterBurrow(Entity entity, boolean hasNectar, int ticksInBurrow) {
        if (this.meerkats.size() < 3) {
            entity.stopRiding();
            entity.removeAllPassengers();
            CompoundTag compoundTag = new CompoundTag();
            entity.saveToTag(compoundTag);
            this.meerkats.add(new BurrowBlockEntity.Meerkat(compoundTag, ticksInBurrow, hasNectar ? 2400 : 600));
            if (this.world != null) {
                BlockPos blockPos = this.getPos();
                this.world.playSound((PlayerEntity) null, (double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            entity.remove();
        }
    }

    private boolean releaseMeerkat(BlockState state, BurrowBlockEntity.Meerkat meerkat, @Nullable List<Entity> list, BurrowBlockEntity.MeerkatState meerkatState) {
        if ((this.world.isNight() || this.world.isRaining()) && meerkatState != BurrowBlockEntity.MeerkatState.EMERGENCY) {
            return false;
        } else {
            BlockPos blockPos = this.getPos();
            CompoundTag compoundTag = meerkat.entityData;
            compoundTag.remove("Passengers");
            compoundTag.remove("Leash");
            compoundTag.remove("UUID");
            Direction direction = this.world.getBlockState(blockPos).get(BurrowBlock.FACING);
            BlockPos blockPos2 = blockPos.offset(direction);
            boolean bl = !this.world.getBlockState(blockPos2).getCollisionShape(this.world, blockPos2).isEmpty();
            if (bl && meerkatState != BurrowBlockEntity.MeerkatState.EMERGENCY) {
                return false;
            } else {
                Entity entity = EntityType.loadEntityWithPassengers(compoundTag, this.world, (entityx) -> {
                    return entityx;
                });
                if (entity != null) {
                    if (entity instanceof MeerkatEntity) {
                        MeerkatEntity meerkatEntity = (MeerkatEntity) entity;

                        this.ageMeerkat(meerkat.ticksInBurrow, meerkatEntity);
                        if (list != null) {
                            list.add(meerkatEntity);
                        }

                        float f = entity.getWidth();
                        double d = bl ? 0.0D : 0.55D + (double) (f / 2.0F);
                        double e = (double) blockPos.getX() + 0.5D + d * (double) direction.getOffsetX();
                        double g = (double) blockPos.getY() + 0.5D - (double) (entity.getHeight() / 2.0F);
                        double h = (double) blockPos.getZ() + 0.5D + d * (double) direction.getOffsetZ();
                        entity.refreshPositionAndAngles(e, g, h, entity.yaw, entity.pitch);
                    }

                    this.world.playSound((PlayerEntity) null, blockPos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return this.world.spawnEntity(entity);
                } else {
                    return false;
                }
            }
        }
    }

    private void ageMeerkat(int ticks, MeerkatEntity meerkat) {
        int i = meerkat.getBreedingAge();
        if (i < 0) {
            meerkat.setBreedingAge(Math.min(0, i + ticks));
        } else if (i > 0) {
            meerkat.setBreedingAge(Math.max(0, i - ticks));
        }

        meerkat.setLoveTicks(Math.max(0, meerkat.getLoveTicks() - ticks));
    }

    private boolean hasFlowerPos() {
        return this.flowerPos != null;
    }

    private void tickMeerkats() {
        Iterator<BurrowBlockEntity.Meerkat> iterator = this.meerkats.iterator();

        BurrowBlockEntity.Meerkat meerkat;
        for (BlockState blockState = this.getCachedState(); iterator.hasNext(); meerkat.ticksInBurrow++) {
            meerkat = (BurrowBlockEntity.Meerkat) iterator.next();
            if (meerkat.ticksInBurrow > meerkat.minOccupationTicks) {
                BurrowBlockEntity.MeerkatState meerkatState = meerkat.entityData.getBoolean("HasNectar") ? BurrowBlockEntity.MeerkatState.HONEY_DELIVERED : BurrowBlockEntity.MeerkatState.MEERKAT_RELEASED;
                if (this.releaseMeerkat(blockState, meerkat, (List) null, meerkatState)) {
                    iterator.remove();
                }
            }
        }

    }

    public void tick() {
        if (!this.world.isClient) {
            this.tickMeerkats();
            BlockPos blockPos = this.getPos();
            if (this.meerkats.size() > 0 && this.world.getRandom().nextDouble() < 0.005D) {
                double d = (double) blockPos.getX() + 0.5D;
                double e = (double) blockPos.getY();
                double f = (double) blockPos.getZ() + 0.5D;
                this.world.playSound((PlayerEntity) null, d, e, f, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.meerkats.clear();
        ListTag listTag = tag.getList("Meerkats", 10);

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            BurrowBlockEntity.Meerkat meerkat = new BurrowBlockEntity.Meerkat(compoundTag.getCompound("EntityData"), compoundTag.getInt("TicksInBurrow"), compoundTag.getInt("MinOccupationTicks"));
            this.meerkats.add(meerkat);
        }

        this.flowerPos = null;
        if (tag.contains("FlowerPos")) {
            this.flowerPos = NbtHelper.toBlockPos(tag.getCompound("FlowerPos"));
        }

    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Meerkats", this.getMeerkats());
        if (this.hasFlowerPos()) {
            tag.put("FlowerPos", NbtHelper.fromBlockPos(this.flowerPos));
        }

        return tag;
    }

    public ListTag getMeerkats() {
        ListTag listTag = new ListTag();

        for (Meerkat meerkat : this.meerkats) {
            meerkat.entityData.remove("UUID");
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("EntityData", meerkat.entityData);
            compoundTag.putInt("TicksInBurrow", meerkat.ticksInBurrow);
            compoundTag.putInt("MinOccupationTicks", meerkat.minOccupationTicks);
            listTag.add(compoundTag);
        }

        return listTag;
    }

    public static class Meerkat {
        private final CompoundTag entityData;
        private int ticksInBurrow;
        private final int minOccupationTicks;

        private Meerkat(CompoundTag entityData, int ticksInBurrow, int minOccupationTicks) {
            entityData.remove("UUID");
            this.entityData = entityData;
            this.ticksInBurrow = ticksInBurrow;
            this.minOccupationTicks = minOccupationTicks;
        }
    }

    public static enum MeerkatState {
        HONEY_DELIVERED,
        MEERKAT_RELEASED,
        EMERGENCY;

        private MeerkatState() {
        }
    }
}
