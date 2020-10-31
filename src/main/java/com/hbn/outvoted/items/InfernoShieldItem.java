package com.hbn.outvoted.items;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.render.ShieldRenderer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class InfernoShieldItem extends ShieldItem {
    public InfernoShieldItem() {
        super(new Properties().setISTER(() -> getISTER()).maxDamage(750).group(Outvoted.TAB));
        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> getISTER() {
        return ShieldRenderer::new;
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }
}
