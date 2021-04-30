package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.util.GroupCheck;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

public class WildfireHelmetItem extends GeoArmorItem implements IAnimatable {
    private int timer = 0;
    private static final Identifier HELMET_TEXTURE = new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire.png");
    private static final Identifier HELMET_TEXTURE_SOUL = new Identifier(Outvoted.MOD_ID, "textures/entity/wildfire/wildfire_soul.png");

    private AnimationFactory factory = new AnimationFactory(this);

    public WildfireHelmetItem() {
        super(ModArmor.WILDFIRE, EquipmentSlot.HEAD, new Item.Settings().fireproof());
    }

    @Override
    public Identifier getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, Identifier defaultTexture) {
        if (Outvoted.config.client.wildfireVariants) {
            if (stack.getTag() != null && stack.getTag().getFloat("SoulTexture") == 1.0F) {
                return HELMET_TEXTURE_SOUL;
            }
        }
        return HELMET_TEXTURE;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        LivingEntity livingEntity = (LivingEntity) entity;
        int helmetPenalty = Outvoted.config.common.misc.helmetPenalty;
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

    @Override
    protected boolean isIn(ItemGroup group) {
        return GroupCheck.isIn(group, Outvoted.TAB_COMBAT);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
