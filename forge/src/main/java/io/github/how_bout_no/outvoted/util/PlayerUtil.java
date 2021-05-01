package io.github.how_bout_no.outvoted.util;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerUtil {
    default PlayerEntity asPlayer() {
        return (PlayerEntity) this;
    }
}
