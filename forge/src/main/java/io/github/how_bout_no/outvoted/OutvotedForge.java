package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.render.ClientRender;
import io.github.how_bout_no.outvoted.init.ModBlocks;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.util.ServerEvents;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashSet;

@Mod("outvoted")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OutvotedForge {
    public OutvotedForge() {
        EventBuses.registerModEventBus(Outvoted.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Outvoted.init();

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().register(new ClientRender()));
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        BlockEntityType.SIGN.blocks = new HashSet<>(BlockEntityType.SIGN.blocks);
        BlockEntityType.SIGN.blocks.add(ModBlocks.PALM_SIGN.get());
        BlockEntityType.SIGN.blocks.add(ModBlocks.PALM_WALL_SIGN.get());
        BlockEntityType.SIGN.blocks.add(ModBlocks.BAOBAB_SIGN.get());
        BlockEntityType.SIGN.blocks.add(ModBlocks.BAOBAB_WALL_SIGN.get());
    }
}
