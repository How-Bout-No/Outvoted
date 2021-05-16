package io.github.how_bout_no.outvoted.mixin;

import com.mojang.authlib.GameProfile;
import io.github.how_bout_no.outvoted.entity.util.IMixinPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IMixinPlayerEntity {
    private boolean hasBook;

    public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    public void readInject(CompoundTag tag, CallbackInfo ci) {
        this.hasBook = tag.getBoolean("hasBook");
    }

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    public void writeInject(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("hasBook", this.hasBook);
    }

    public boolean hasBook() {
        return this.hasBook;
    }

    public void setBook(boolean hasBook) {
        this.hasBook = hasBook;
    }
}
