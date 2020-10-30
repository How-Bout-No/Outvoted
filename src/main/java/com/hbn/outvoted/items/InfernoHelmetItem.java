package com.hbn.outvoted.items;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.client.model.InfernoHelmetModel;
import com.hbn.outvoted.init.ModItems;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InfernoHelmetItem extends ArmorItem {
    private boolean timer = true;

    public InfernoHelmetItem() {
        super(ModArmor.BLAZE, EquipmentSlotType.HEAD, new Item.Properties().group(Outvoted.TAB));
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) new InfernoHelmetModel<LivingEntity>();
    }


    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "outvoted:textures/entity/inferno.png";
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == ModItems.INFERNO_HELMET.get() && player.isBurning()) {
            if (timer) {
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 200, 0, false, false, true));
                timer = false;
            }
        } else {
            timer = true;
        }
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }
}
