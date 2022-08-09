package io.github.how_bout_no.outvoted.mixin;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneycombItem.class)
public abstract class MixinHoneycombItem {
    @Dynamic
    @Inject(method = {"method_34723", "m_150881_"}, at = @At("RETURN"), remap = false, cancellable = true)
    private static void onBuildWaxedMapping(CallbackInfoReturnable<BiMap<Block, Block>> cir) {
        var builder = ImmutableBiMap.<Block, Block>builder().putAll(cir.getReturnValue());
        builder.put(ModBlocks.COPPER_BUTTON.get(), ModBlocks.WAXED_COPPER_BUTTON.get())
                .put(ModBlocks.EXPOSED_COPPER_BUTTON.get(), ModBlocks.WAXED_EXPOSED_COPPER_BUTTON.get())
                .put(ModBlocks.WEATHERED_COPPER_BUTTON.get(), ModBlocks.WAXED_WEATHERED_COPPER_BUTTON.get())
                .put(ModBlocks.OXIDIZED_COPPER_BUTTON.get(), ModBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get());
        cir.setReturnValue(builder.build());
    }
}
