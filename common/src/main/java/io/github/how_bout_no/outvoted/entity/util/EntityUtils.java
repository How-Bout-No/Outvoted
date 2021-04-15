package io.github.how_bout_no.outvoted.entity.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.UUID;

public class EntityUtils {
    /**
     * Sets entity max health to that of its appropriate config value
     */
    public static void setConfigHealth(LivingEntity entity, double value) {
        EntityAttributeInstance max_health = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        max_health.addTemporaryModifier(new EntityAttributeModifier(UUID.fromString("84b09787-fa1c-46f0-92f8-ebac1f08839e"), "Max health", value - 20.0D, EntityAttributeModifier.Operation.ADDITION));
        entity.setHealth(entity.getMaxHealth());
    }
}
