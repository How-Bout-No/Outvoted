package io.github.how_bout_no.outvoted.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.forge.util.MappingsFixer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("outvoted")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OutvotedForge {
    public OutvotedForge() {
        EventBuses.registerModEventBus(Outvoted.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Outvoted.init();

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        modEventBus.addListener(this::setup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().register(new OutvotedImpl()));
        MinecraftForge.EVENT_BUS.register(new MappingsFixer());
    }
}
