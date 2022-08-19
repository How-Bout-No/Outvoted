package io.github.how_bout_no.outvoted.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HealthUtil {
    public static void setConfigHealth(LivingEntity entity, double value) {
        if (value != 0 && entity.getAttribute(Attributes.MAX_HEALTH).getModifiers().isEmpty()) {
            entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Max health", value - 20.0D, AttributeModifier.Operation.ADDITION));
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
