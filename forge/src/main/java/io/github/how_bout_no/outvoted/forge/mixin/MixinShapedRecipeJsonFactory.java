package io.github.how_bout_no.outvoted.forge.mixin;

import io.github.how_bout_no.outvoted.Outvoted;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShapedRecipeJsonFactory.class)
public abstract class MixinShapedRecipeJsonFactory {
    @Shadow
    @Final
    private Item output;

    @Redirect(method = "offerTo(Ljava/util/function/Consumer;Lnet/minecraft/util/Identifier;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;getName()Ljava/lang/String;"))
    private String bruh(ItemGroup itemGroup) {
        for (ItemGroup group : Outvoted.TABS) {
            if (((ItemInvoker) this.output).invokeIsIn(group)) {
                return group.getName();
            }
        }
        return itemGroup.getName();
    }
}