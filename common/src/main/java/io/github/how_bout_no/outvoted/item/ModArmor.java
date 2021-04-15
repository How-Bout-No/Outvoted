package io.github.how_bout_no.outvoted.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum ModArmor implements ArmorMaterial {
    /*BLAZE(Outvoted.MOD_ID + ":blaze", 23, new int[]{2, 2, 2, 2}, 0, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            0.5F, () -> {
        return Ingredient.fromItems(Items.BLAZE_ROD.getItem());
    }, 0.0F);*/

    WILDFIRE("wildfire", 25, new int[]{2, 2, 2, 2}, 0, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            0.5F, () -> Ingredient.EMPTY, 0.0F);

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{11, 16, 15, 13};
    private final String name;
    private final int maxDamageFactor; //Durability, Iron=15, Diamond=33, Gold=7, Leather=5
    private final int[] damageReductionAmountArray; //Armor Bar Protection, 1 = 1/2 armor bar
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness; //Increases Protection, 0.0F=Iron/Gold/Leather, 2.0F=Diamond, 3.0F=Netherite
    private final Supplier<Ingredient> repairMaterial;
    private final float knockbackResistance; //1.0F=No Knockback, 0.0F=Disabled

    ModArmor(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability,
             SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairMaterial, float knockbackResistance) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.repairMaterial = repairMaterial;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurability(EquipmentSlot slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getEntitySlotId()] * this.maxDamageFactor;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slotIn) {
        return this.damageReductionAmountArray[slotIn.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

}
