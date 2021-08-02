package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Lazy;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModSpawnEggItem extends SpawnEggItem {
    protected static final List<ModSpawnEggItem> UNADDED_EGGS = new ArrayList<>();
    private final Lazy<? extends EntityType<?>> entityTypeSupplier;

    public ModSpawnEggItem(RegistrySupplier<? extends EntityType<?>> entityTypeSupplier, int primaryColor, int secondaryColor, Settings settings) {
        super(null, primaryColor, secondaryColor, settings);
        this.entityTypeSupplier = new Lazy<>(entityTypeSupplier);
        UNADDED_EGGS.add(this);
    }

    public static void initSpawnEggs() {
        ItemDispenserBehavior dispenseBehavior = new ItemDispenserBehavior() {
            @Override
            public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
                Direction direction = blockPointer.getBlockState().get(DispenserBlock.FACING);
                EntityType<?> type = ((SpawnEggItem) itemStack.getItem()).getEntityType(itemStack.getTag());
                type.spawn(blockPointer.getWorld(), itemStack.getTag(), null, null, blockPointer.getBlockPos().offset(direction),
                        SpawnReason.DISPENSER, direction != Direction.UP, false);
                itemStack.decrement(1);
                return itemStack;
            }
        };
        for (final SpawnEggItem spawnEgg : UNADDED_EGGS) {
            SpawnEggItem.SPAWN_EGGS.put(spawnEgg.getEntityType(null), spawnEgg);
            DispenserBlock.registerBehavior(spawnEgg, dispenseBehavior);
        }
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isIn(group, Outvoted.TAB_MISC);
    }

    @Override
    public EntityType<?> getEntityType(@Nullable NbtCompound tag) {
        return this.entityTypeSupplier.get();
    }
}