package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.entity.MeerkatEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderEntity.class)
public abstract class MixinSpiderEntity extends HostileEntity {
    protected MixinSpiderEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("INVOKE"))
    private void addGoals(CallbackInfo ci) {
        this.goalSelector.add(2, new FleeEntityGoal<>(this, MeerkatEntity.class, 6.0F, 1.0D, 1.2D));
    }
}
