package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.config.Config;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

public class WildfireHelmetItem extends GeoArmorItem implements IAnimatable {
    private int timer = 0;

    public WildfireHelmetItem() {
        super(ModArmor.WILDFIRE, EquipmentSlot.HEAD, ModItems.ITEM_PROPERTIES.fireResistant());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (slot == EquipmentSlot.HEAD.getIndex()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            int helmetPenalty = Config.helmetPenalty.get();
            if (helmetPenalty != 0) {
                if (livingEntity.isOnFire()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1, 0, false, false, true));
                    if (timer % helmetPenalty == 0) {
                        stack.hurtAndBreak(1 + (timer / 600), livingEntity, consumer -> consumer.broadcastBreakEvent(EquipmentSlot.HEAD));
                        //timer = 0;
                    }
                    timer++;
                } else {
                    timer = 0;
                }
            } else {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1, 0, false, false, true));
            }
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            stacks.add(new ItemStack(this));
            if (Config.wildfireVariants.get()) {
                ItemStack soul = new ItemStack(this);
                soul.getOrCreateTag().putFloat("SoulTexture", 1.0F);
                stacks.add(soul);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack != null && isSoul(stack)) {
            return new TranslatableComponent("item.outvoted.wildfire_helmet_s");
        }

        return super.getName(stack);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
//        super.appendTooltip(stack, world, tooltip, context);
//
//        if (isSoul(stack)) {
//            tooltip.add(new LiteralText("Soul").formatted(Formatting.GRAY));
//        }
//    }

    private boolean isSoul(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getFloat("SoulTexture") == 1.0F;
    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
