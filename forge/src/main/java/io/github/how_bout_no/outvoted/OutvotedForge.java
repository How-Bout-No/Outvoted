package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.render.ClientRender;
import io.github.how_bout_no.outvoted.init.ModSigns;
import io.github.how_bout_no.outvoted.util.ServerEvents;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        ModSigns.init();
    }
}
