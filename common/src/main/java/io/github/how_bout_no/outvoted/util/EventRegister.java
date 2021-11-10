package io.github.how_bout_no.outvoted.util;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;

public class EventRegister {
    public static void init() {
        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (!entity.world.isClient()) {
                if (entity instanceof PlayerEntity player) {
                    if (amount > 0.0F && player.isBlocking() && player.getActiveItem().getItem() == ModItems.WILDFIRE_SHIELD.get()) {
                        if (source.getSource() != null) {
                            if (source.isProjectile()) {
                                source.getSource().setOnFireFor(5);
                            } else if (source.getAttacker() != null) {
                                source.getAttacker().setOnFireFor(5);
                            }
                        }
                    }
                }
            }
            return EventResult.pass();
        });
    }
}
