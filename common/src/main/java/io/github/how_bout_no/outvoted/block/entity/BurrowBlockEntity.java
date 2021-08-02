package io.github.how_bout_no.outvoted.block.entity;

import com.google.common.collect.Lists;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.block.BurrowBlock;
import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import io.github.how_bout_no.outvoted.init.ModBlockEntityTypes;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class BurrowBlockEntity extends BlockEntity implements Tickable {
    private final List<Meerkat> meerkats = Lists.newArrayList();
    private final int capacity = 4;

    public BurrowBlockEntity() {
        super(ModBlockEntityTypes.BURROW.get());
    }

    public boolean hasNoMeerkats() {
        return this.meerkats.isEmpty();
    }

    public boolean isFullOfMeerkats() {
        return this.meerkats.size() == 3;
    }

    public void tryEnterBurrow(Entity entity, boolean hasNectar) {
        this.tryEnterBurrow(entity, hasNectar, 0);
    }

    public void tryEnterBurrow(Entity entity, boolean hasNectar, int ticksInBurrow) {
        if (this.meerkats.size() < capacity) {
            entity.stopRiding();
            entity.removeAllPassengers();
            NbtCompound compoundTag = new NbtCompound();
            entity.saveNbt(compoundTag);
            this.meerkats.add(new Meerkat(compoundTag, ticksInBurrow, hasNectar ? 2400 : 600));
            if (this.world != null) {
                BlockPos blockPos = this.getPos();
                this.world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            entity.remove();
        }
    }

    private boolean releaseMeerkat(BlockState state, Meerkat meerkat, @Nullable List<Entity> list, MeerkatState meerkatState) {
        if ((this.world.isNight() || this.world.isRaining()) && meerkatState != MeerkatState.EMERGENCY && !this.world.getBlockState(this.pos.offset(state.get(FacingBlock.FACING))).isAir()) {
            return false;
        } else {
            BlockPos blockPos = this.getPos();
            NbtCompound compoundTag = meerkat.entityData;
            compoundTag.remove("Passengers");
            compoundTag.remove("Leash");
            compoundTag.remove("UUID");
            Direction direction = this.world.getBlockState(blockPos).get(BurrowBlock.FACING);
            BlockPos blockPos2 = blockPos.offset(direction);
            boolean bl = !this.world.getBlockState(blockPos2).getCollisionShape(this.world, blockPos2).isEmpty();
            if (bl && meerkatState != MeerkatState.EMERGENCY) {
                return false;
            } else {
                Entity entity = EntityType.loadEntityWithPassengers(compoundTag, this.world, (entityx) -> entityx);
                if (entity != null) {
                    if (entity instanceof MeerkatEntity) {
                        MeerkatEntity meerkatEntity = (MeerkatEntity) entity;

                        this.ageMeerkat(meerkat.ticksInBurrow, meerkatEntity);
                        if (list != null) {
                            list.add(meerkatEntity);
                        }

                        entity.refreshPositionAndAngles(blockPos2, entity.yaw, entity.pitch);
                    }

                    this.world.playSound(null, blockPos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
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

    private void tickMeerkats() {
        Iterator<Meerkat> iterator = this.meerkats.iterator();

        Meerkat meerkat;
        for (BlockState blockState = this.getCachedState(); iterator.hasNext(); meerkat.ticksInBurrow++) {
            meerkat = iterator.next();
            if (meerkat.ticksInBurrow > meerkat.minOccupationTicks) {
                MeerkatState meerkatState = meerkat.entityData.getBoolean("HasNectar") ? MeerkatState.HONEY_DELIVERED : MeerkatState.MEERKAT_RELEASED;
                if (this.releaseMeerkat(blockState, meerkat, null, meerkatState))
                    iterator.remove();
            }
        }

    }

    public void tick() {
        if (!this.world.isClient) {
            this.tickMeerkats();
            if (this.meerkats.size() > 0 && this.world.getRandom().nextDouble() < 0.001D) {
                for (Meerkat meerkat : this.meerkats) {
                    double health = meerkat.entityData.getDouble("Health");
                    if (health != Outvoted.commonConfig.entities.meerkat.health) {
                        health = Math.min(health + 1, Outvoted.commonConfig.entities.meerkat.health);
                        meerkat.entityData.putDouble("Health", health);
                    }
                }
            } else if (this.meerkats.size() > 1 && this.meerkats.size() < capacity && this.world.getRandom().nextDouble() < 0.0001D) {
                MeerkatEntity entity = ModEntityTypes.MEERKAT.get().create(this.world);
                NbtCompound compoundTag = new NbtCompound();
                entity.initialize((ServerWorldAccess) this.world, this.world.getLocalDifficulty(this.getPos()), SpawnReason.BREEDING, null, null);
                entity.saveNbt(compoundTag);
                compoundTag.putInt("Age", -25000);
                this.meerkats.add(new Meerkat(compoundTag, 0, 600));
            }
        }
    }

    public void fromTag(BlockState state, NbtCompound tag) {
        super.fromTag(state, tag);
        this.meerkats.clear();
        NbtList NbtList = tag.getList("Meerkats", 10);

        for (int i = 0; i < NbtList.size(); ++i) {
            NbtCompound compoundTag = NbtList.getCompound(i);
            Meerkat meerkat = new Meerkat(compoundTag.getCompound("EntityData"), compoundTag.getInt("TicksInBurrow"), compoundTag.getInt("MinOccupationTicks"));
            this.meerkats.add(meerkat);
        }

    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.put("Meerkats", this.getMeerkats());

        return tag;
    }

    public NbtList getMeerkats() {
        NbtList NbtList = new NbtList();

        for (Meerkat meerkat : this.meerkats) {
            meerkat.entityData.remove("UUID");
            NbtCompound compoundTag = new NbtCompound();
            compoundTag.put("EntityData", meerkat.entityData);
            compoundTag.putInt("TicksInBurrow", meerkat.ticksInBurrow);
            compoundTag.putInt("MinOccupationTicks", meerkat.minOccupationTicks);
            NbtList.add(compoundTag);
        }

        return NbtList;
    }

    public static class Meerkat {
        private final NbtCompound entityData;
        private int ticksInBurrow;
        private final int minOccupationTicks;

        private Meerkat(NbtCompound entityData, int ticksInBurrow, int minOccupationTicks) {
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
