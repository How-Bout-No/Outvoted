package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

public class WildfireHelmetItem extends GeoArmorItem implements IAnimatable {
    private int timer = 0;

    public WildfireHelmetItem() {
        super(ModArmor.WILDFIRE, EquipmentSlot.HEAD, new Settings().group(Outvoted.TAB_COMBAT));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        LivingEntity livingEntity = (LivingEntity) entity;
        if (Outvoted.config.get().misc.helmetpenalty != 0) {
            if (livingEntity.isOnFire()) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1, 0, false, false, true));
                if (timer % Outvoted.config.get().misc.helmetpenalty == 0) {
                    stack.damage(1 + (timer / 600), livingEntity, consumer -> consumer.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                    //timer = 0;
                }
                timer++;
            } else {
                timer = 0;
            }
        } else {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1, 0, false, false, true));
        }
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isInCombat(group);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        System.out.println(stack);
        return super.isEnchantable(stack);
    }

    @Override
    public boolean isFireproof() {
        return true;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return new AnimationFactory(this);
    }
}