package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HostileEntity.class)
public abstract class MixinHostileEntity extends PathAwareEntity implements Monster {
    protected MixinHostileEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void initGoals() {
        if (this.getGroup() == EntityGroup.ARTHROPOD)
            this.goalSelector.add(2, new FleeEntityGoal<>(this, MeerkatEntity.class, 6.0F, 1.0D, 1.2D));
    }
}
