package io.github.how_bout_no.outvoted;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.item.Item;

public class OutvotedModPlatform {
    @ExpectPlatform
    public static Item.Settings setISTER(Item.Settings properties) {
        throw new AssertionError();
    }
}
