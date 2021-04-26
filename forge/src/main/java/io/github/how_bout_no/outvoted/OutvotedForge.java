package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.client.render.ClientRender;
import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.ModSigns;
import io.github.how_bout_no.outvoted.util.ServerEvents;
import io.github.how_bout_no.outvoted.util.compat.PatchouliCompat;
import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.ActionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("outvoted")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OutvotedForge {
    public OutvotedForge() {
        EventBuses.registerModEventBus(Outvoted.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Outvoted.init();

        AutoConfig.getConfigHolder(OutvotedConfig.class).registerSaveListener((manager, data) -> {
            System.out.println("save");
            if (ModList.get().isLoaded("patchouli")) {
                PatchouliCompat.updateFlag();
            }
            return ActionResult.SUCCESS;
        });
        AutoConfig.getConfigHolder(OutvotedConfig.class).registerLoadListener((manager, newData) -> {
            System.out.println("load");
            if (ModList.get().isLoaded("patchouli")) {
                PatchouliCompat.updateFlag();
            }
            return ActionResult.SUCCESS;
        });

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
