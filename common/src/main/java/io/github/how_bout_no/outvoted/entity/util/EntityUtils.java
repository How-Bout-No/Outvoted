package io.github.how_bout_no.outvoted.entity.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class EntityUtils {
    /**
     * Sets entity max health to that of its appropriate config value
     */
    public static void setConfigHealth(LivingEntity entity, double value) {
        if (value != 0) {
            entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier("Max health", value - 20.0D, EntityAttributeModifier.Operation.ADDITION));
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
