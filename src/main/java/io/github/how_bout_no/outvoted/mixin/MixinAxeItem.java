package io.github.how_bout_no.outvoted.mixin;

import io.github.how_bout_no.outvoted.block.IOxidizable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public abstract class MixinAxeItem {
    protected Level world;
    protected BlockPos blockPos;
    protected Player playerEntity;
    protected BlockState blockState;

    @Inject(method = "useOn", at = @At(value = "HEAD"))
    private void inject(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        this.world = context.getLevel();
        this.blockPos = context.getClickedPos();
        this.playerEntity = context.getPlayer();
        this.blockState = world.getBlockState(blockPos);
    }

    @ModifyVariable(method = "useOn", at = @At("STORE"), ordinal = 1)
    private Optional<BlockState> setScrape(Optional<BlockState> optional) {
        return IOxidizable.getPrevious(blockState);
    }

//    @ModifyVariable(method = "useOnBlock", at = @At("STORE"), ordinal = 2)
//    private Optional<BlockState> setWax(Optional<BlockState> optional) {
//        return Optional.ofNullable((Block)((BiMap) HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock())).map((block) -> {
//            return block.getStateWithProperties(blockState);
//        });
//    }
//
//    @Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z", ordinal = 2))
//    private boolean injected(Optional<BlockState> instance) {
//        if (instance.isEmpty()) {
//            ImmutableBiMap<Object, Object> buttonMaps = ImmutableBiMap.builder().put(ModBlocks.COPPER_BUTTON.get(), ModBlocks.WAXED_COPPER_BUTTON.get())
//                    .put(ModBlocks.EXPOSED_COPPER_BUTTON.get(), ModBlocks.WAXED_EXPOSED_COPPER_BUTTON.get())
//                    .put(ModBlocks.WEATHERED_COPPER_BUTTON.get(), ModBlocks.WAXED_WEATHERED_COPPER_BUTTON.get())
//                    .put(ModBlocks.OXIDIZED_COPPER_BUTTON.get(), ModBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get()).build();
//            Optional<BlockState> optionalBlockState = Optional.ofNullable((Block) ((BiMap) buttonMaps.inverse()).get(this.blockState.getBlock())).map((block) -> {
//                return block.getStateWithProperties(this.blockState);
//            });
//            return optionalBlockState.isPresent();
//        }
//        return true;
//    }
}
