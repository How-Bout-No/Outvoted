package io.github.how_bout_no.outvoted.util.compat;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.entity.util.IMixinPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.item.PatchouliItems;

public class PatchouliCompat {
    public static void updateFlag() {
        PatchouliAPI.get().setConfigFlag("outvoted:wildfirevariant", Outvoted.clientConfig.wildfireVariants);
    }

    public static void giveBook(ServerPlayerEntity player) {
        if (Outvoted.commonConfig.misc.givePatchouliBookOnLogin && !((IMixinPlayerEntity) player).hasBook()) {
            ItemStack stack = new ItemStack(PatchouliItems.book);
            stack.getOrCreateTag().putString("patchouli:book", "outvoted:book");
            player.giveItemStack(stack);
            ((IMixinPlayerEntity) player).setBook(true);
        }
    }
}
