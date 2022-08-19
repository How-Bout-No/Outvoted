package io.github.how_bout_no.outvoted.block.entity;

import com.google.common.collect.Lists;
import io.github.how_bout_no.outvoted.block.BurrowBlock;
import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.entity.Meerkat;
import io.github.how_bout_no.outvoted.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class BurrowBlockEntity extends BlockEntity {
    private final List<MeerkatInfo> meerkats = Lists.newArrayList();
    private final int capacity = 4;

    public BurrowBlockEntity(BlockPos arg, BlockState arg2) {
        super(ModEntities.BURROW.get(), arg, arg2);
    }

    public boolean isEmpty() {
        return this.meerkats.isEmpty();
    }

    public boolean isFull() {
        return this.meerkats.size() == 3;
    }

    public void tryEnterBurrow(Entity entity, boolean hasNectar) {
        this.tryEnterBurrow(entity, hasNectar, 0);
    }

    public void tryEnterBurrow(Entity entity, boolean hasNectar, int ticksInBurrow) {
        if (this.meerkats.size() < capacity) {
            entity.stopRiding();
            entity.ejectPassengers();
            CompoundTag compoundTag = new CompoundTag();
            entity.save(compoundTag);
            this.meerkats.add(new MeerkatInfo(compoundTag, ticksInBurrow, hasNectar ? 2400 : 600));
            if (this.level != null) {
                BlockPos blockPos = this.getBlockPos();
                this.level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    private boolean releaseMeerkat(BlockState state, MeerkatInfo meerkat, @Nullable List<Entity> list, MeerkatState meerkatState) {
        if ((this.level.isNight() || this.level.isRaining()) && meerkatState != MeerkatState.EMERGENCY && !this.level.getBlockState(this.worldPosition.relative(state.getValue(DirectionalBlock.FACING))).isAir()) {
            return false;
        } else {
            BlockPos blockPos = this.getBlockPos();
            CompoundTag compoundTag = meerkat.entityData;
            compoundTag.remove("Passengers");
            compoundTag.remove("Leash");
            compoundTag.remove("UUID");
            Direction direction = this.level.getBlockState(blockPos).getValue(BurrowBlock.FACING);
            BlockPos blockPos2 = blockPos.relative(direction);
            boolean bl = !this.level.getBlockState(blockPos2).getCollisionShape(this.level, blockPos2).isEmpty();
            if (bl && meerkatState != MeerkatState.EMERGENCY) {
                return false;
            } else {
                Entity entity = EntityType.loadEntityRecursive(compoundTag, this.level, (entityx) -> entityx);
                if (entity != null) {
                    if (entity instanceof Meerkat) {
                        Meerkat meerkat1 = (Meerkat) entity;

                        this.ageMeerkat(meerkat.ticksInBurrow, meerkat1);
                        if (list != null) {
                            list.add(meerkat1);
                        }

                        entity.moveTo(blockPos2, entity.getYRot(), entity.getXRot());
                    }

                    this.level.playSound(null, blockPos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return this.level.addFreshEntity(entity);
                } else {
                    return false;
                }
            }
        }
    }

    private void ageMeerkat(int ticks, Meerkat meerkat) {
        int i = meerkat.getAge();
        if (i < 0) {
            meerkat.setAge(Math.min(0, i + ticks));
        } else if (i > 0) {
            meerkat.setAge(Math.max(0, i - ticks));
        }

        meerkat.setInLoveTime(Math.max(0, meerkat.getInLoveTime() - ticks));
    }

    private void tickOccupants(Level arg, BlockPos arg2, BlockState arg3, List<BurrowBlockEntity.MeerkatInfo> list) {
        Iterator<MeerkatInfo> iterator = this.meerkats.iterator();

        MeerkatInfo meerkat;
        for (BlockState blockState = this.getBlockState(); iterator.hasNext(); meerkat.ticksInBurrow++) {
            meerkat = iterator.next();
            if (meerkat.ticksInBurrow > meerkat.minOccupationTicks) {
                MeerkatState meerkatState = meerkat.entityData.getBoolean("HasNectar") ? MeerkatState.HONEY_DELIVERED : MeerkatState.MEERKAT_RELEASED;
                if (this.releaseMeerkat(blockState, meerkat, null, meerkatState))
                    iterator.remove();
            }
        }

    }

    public void serverTick(Level arg, BlockPos arg2, BlockState arg3, BurrowBlockEntity arg4) {
        if (!this.level.isClientSide) {
            this.tickOccupants(arg, arg2, arg3, arg4.meerkats);
            if (this.meerkats.size() > 0 && this.level.getRandom().nextDouble() < 0.001D) {
                for (MeerkatInfo meerkat : this.meerkats) {
                    double health = meerkat.entityData.getDouble("Health");
                    if (health != Config.meerkatHealth.get()) {
                        health = Math.min(health + 1, Config.meerkatHealth.get());
                        meerkat.entityData.putDouble("Health", health);
                    }
                }
            } else if (this.meerkats.size() > 1 && this.meerkats.size() < capacity && this.level.getRandom().nextDouble() < 0.0001D) {
                Meerkat entity = ModEntities.MEERKAT.get().create(this.level);
                CompoundTag compoundTag = new CompoundTag();
                entity.finalizeSpawn((ServerLevelAccessor) this.level, this.level.getCurrentDifficultyAt(this.getBlockPos()), MobSpawnType.BREEDING, null, null);
                entity.save(compoundTag);
                compoundTag.putInt("Age", -25000);
                this.meerkats.add(new MeerkatInfo(compoundTag, 0, 600));
            }
        }
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.meerkats.clear();
        ListTag NbtList = tag.getList("Meerkats", 10);

        for (int i = 0; i < NbtList.size(); ++i) {
            CompoundTag compoundTag = NbtList.getCompound(i);
            MeerkatInfo meerkat = new MeerkatInfo(compoundTag.getCompound("EntityData"), compoundTag.getInt("TicksInBurrow"), compoundTag.getInt("MinOccupationTicks"));
            this.meerkats.add(meerkat);
        }

    }

    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Meerkats", this.getMeerkats());
    }

    public ListTag getMeerkats() {
        ListTag NbtList = new ListTag();

        for (MeerkatInfo meerkat : this.meerkats) {
            meerkat.entityData.remove("UUID");
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("EntityData", meerkat.entityData);
            compoundTag.putInt("TicksInBurrow", meerkat.ticksInBurrow);
            compoundTag.putInt("MinOccupationTicks", meerkat.minOccupationTicks);
            NbtList.add(compoundTag);
        }

        return NbtList;
    }

    public static class MeerkatInfo {
        private final CompoundTag entityData;
        private int ticksInBurrow;
        private final int minOccupationTicks;

        private MeerkatInfo(CompoundTag entityData, int ticksInBurrow, int minOccupationTicks) {
            entityData.remove("UUID");
            this.entityData = entityData;
            this.ticksInBurrow = ticksInBurrow;
            this.minOccupationTicks = minOccupationTicks;
        }
    }

    public enum MeerkatState {
        HONEY_DELIVERED,
        MEERKAT_RELEASED,
        EMERGENCY;

        MeerkatState() {
        }
    }
}

