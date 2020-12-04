package com.hbn.outvoted.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.world.World;

public class SoulBlazeEntity extends BlazeEntity {
    public SoulBlazeEntity(EntityType<? extends BlazeEntity> type, World world) {
        super(type, world);
    }
}
