package io.github.how_bout_no.outvoted.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new LootTables(generator));
            BlockTagProvider blocktagprovider = new BlockTagProvider(generator, existingFileHelper);
            generator.addProvider(blocktagprovider);
//            generator.install(new ItemTagProvider(generator, blocktagprovider, existingFileHelper));
        }
        if (event.includeClient()) {
            generator.addProvider(new Blocks(generator, event.getExistingFileHelper()));
            generator.addProvider(new Items(generator, event.getExistingFileHelper()));
            generator.addProvider(new BlockStates(generator, existingFileHelper));
        }
    }
}
