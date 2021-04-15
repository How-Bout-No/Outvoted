package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.util.GroupCheck;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModSpawnEggItem extends SpawnEggItem {
    protected static final List<ModSpawnEggItem> UNADDED_EGGS = new ArrayList<>();
    private final Lazy<? extends EntityType<?>> entityTypeSupplier;

    public ModSpawnEggItem(RegistrySupplier<? extends EntityType<?>> entityTypeSupplier, int primaryColor, int secondaryColor, Item.Settings settings) {
        super(null, primaryColor, secondaryColor, settings);
        this.entityTypeSupplier = new Lazy(entityTypeSupplier);
        UNADDED_EGGS.add(this);
    }

    public static void initSpawnEggs() {
        for (final SpawnEggItem spawnEgg : UNADDED_EGGS) {
            SpawnEggItem.SPAWN_EGGS.put(spawnEgg.getEntityType(null), spawnEgg);
        }
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isInMisc(group);
    }

    @Override
    public EntityType<?> getEntityType(@Nullable CompoundTag tag) {
        return this.entityTypeSupplier.get();
    }
}