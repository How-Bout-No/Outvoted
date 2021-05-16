package io.github.how_bout_no.outvoted.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.install(new Recipes(generator));
            generator.install(new LootTables(generator));
            BlockTagProvider blocktagprovider = new BlockTagProvider(generator, existingFileHelper);
            generator.install(blocktagprovider);
            generator.install(new ItemTagProvider(generator, blocktagprovider, existingFileHelper));
        }
        if (event.includeClient()) {
            generator.install(new Blocks(generator, event.getExistingFileHelper()));
            generator.install(new Items(generator, event.getExistingFileHelper()));
            generator.install(new BlockStates(generator, existingFileHelper));
        }
    }
}
