package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import javax.annotation.Nullable;

public class WildfireHelmetItem extends ArmorItem implements IAnimatable {
    private int timer = 0;
    private static final String HELMET_TEXTURE = new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png").toString();
    private static final String HELMET_TEXTURE_SOUL = new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire_soul.png").toString();

    private AnimationFactory factory = new AnimationFactory(this);

    public WildfireHelmetItem() {
        super(ModArmor.WILDFIRE, EquipmentSlot.HEAD, new Settings().group(Outvoted.TAB_COMBAT));
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    @Override
    public <A extends BipedEntityModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
        return (A) GeoArmorRenderer.getRenderer(this.getClass()).applyEntityStats(_default).applySlot(armorSlot).setCurrentItem(entityLiving, itemStack, armorSlot);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (Outvoted.config.get().entities.wildfire.variants) {
            if (stack.getTag() != null && stack.getTag().getFloat("SoulTexture") == 1.0F) {
                return HELMET_TEXTURE_SOUL;
            }
        }
        return HELMET_TEXTURE;
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
        return this.factory;
    }
}
