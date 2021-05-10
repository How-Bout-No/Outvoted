package io.github.how_bout_no.outvoted.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * This class really only exists because the AT is inconsistent on whether or not it applies
 */
@Mixin(Item.class)
public interface ItemInvoker {
    @Invoker("isIn")
    boolean invokeIsIn(ItemGroup group);
}
