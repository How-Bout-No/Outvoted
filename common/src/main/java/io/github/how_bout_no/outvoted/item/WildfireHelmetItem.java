package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class WildfireHelmetItem extends ArmorItem implements IAnimatable {
    private int timer = 0;

    public WildfireHelmetItem() {
        super(ModArmor.WILDFIRE, EquipmentSlot.HEAD, new Settings().fireproof().group(Outvoted.TAB_COMBAT));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (slot == EquipmentSlot.HEAD.getEntitySlotId()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            int helmetPenalty = Outvoted.commonConfig.misc.helmetPenalty;
            if (helmetPenalty != 0) {
                if (livingEntity.isOnFire()) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1, 0, false, false, true));
                    if (timer % helmetPenalty == 0) {
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
    }

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isIn(group, Outvoted.TAB_COMBAT);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
            if (Outvoted.config.client.wildfireVariants) {
                ItemStack soul = new ItemStack(this);
                soul.getOrCreateNbt().putFloat("SoulTexture", 1.0F);
                stacks.add(soul);
            }
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack != null && isSoul(stack)) {
            return new TranslatableText("item.outvoted.wildfire_helmet_s");
        }

        return super.getName(stack);
    }

//    @Override
//    @Environment(EnvType.CLIENT)
//    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
//        super.appendTooltip(stack, world, tooltip, context);
//
//        if (isSoul(stack)) {
//            tooltip.add(new LiteralText("Soul").formatted(Formatting.GRAY));
//        }
//    }

    private boolean isSoul(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getFloat("SoulTexture") == 1.0F;
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
