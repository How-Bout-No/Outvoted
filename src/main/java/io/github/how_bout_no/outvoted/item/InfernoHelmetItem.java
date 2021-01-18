package io.github.how_bout_no.outvoted.item;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.client.model.InfernoHelmetModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;

public class InfernoHelmetItem extends ArmorItem {
    private int timer = 0;
//    private boolean timer = true;

    public InfernoHelmetItem() {
        super(ModArmor.INFERNO, EquipmentSlotType.HEAD, new Item.Properties().group(Outvoted.TAB_COMBAT));
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) new InfernoHelmetModel<>();
    }


    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (stack.getTag() != null && stack.getTag().getFloat("CustomModelData") == 1.0F) {
            return "outvoted:textures/entity/inferno_soul.png";
        }
        return "outvoted:textures/entity/inferno.png";
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1, 0, false, false, true));
        if (player.isBurning()) {
            if (timer % 40 == 0) {
                stack.damageItem(1 + (timer / 600), player, consumer -> consumer.sendBreakAnimation(EquipmentSlotType.HEAD));
                //timer = 0;
            }
            timer++;
        } else {
            timer = 0;
        }
        /*if (player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == ModItems.INFERNO_HELMET.get() && player.isBurning() && !player.isCreative()) {
            if (timer) {
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 400, 0, false, false, true));
                timer = false;
            }
        } else {
            timer = true;
        }*/
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        Collection<ItemGroup> groups = new ArrayList<>();
        groups.add(Outvoted.TAB_COMBAT);
        groups.add(ItemGroup.SEARCH);
        return groups;
    }

    /**
     * Allow or forbid the specific book/item combination as an anvil enchant
     *
     * @param stack The item
     * @param book  The book
     * @return if the enchantment is allowed
     */
    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return !EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.MENDING);
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }
}
