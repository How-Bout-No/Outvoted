package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.item.WildfireHelmetItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    World world;

    @Shadow
    abstract double getX();

    @Shadow
    abstract double getY();

    @Shadow
    abstract double getZ();

    @Redirect(method = "dropStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", opcode = Opcodes.PUTFIELD))
    private ItemEntity injected(Entity entity, ItemStack stack, float yOffset) {
        ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY() + (double) yOffset, this.getZ(), stack);
        if (stack.getItem() instanceof WildfireHelmetItem) {
            CompoundTag tag = itemEntity.toTag(new CompoundTag());
            tag.putFloat("SoulTexture", 1.0F);
            itemEntity.fromTag(tag);
        }
        return itemEntity;
    }
}
