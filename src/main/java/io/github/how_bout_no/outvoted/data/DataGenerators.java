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
            BlockTags blockTags = new BlockTags(generator, existingFileHelper);
            generator.addProvider(blockTags);
            generator.addProvider(new ItemTags(generator, blockTags, existingFileHelper));
            generator.addProvider(new ConfiguredStructureTags(generator, existingFileHelper));
        }
        if (event.includeClient()) {
            generator.addProvider(new BlockStates(generator, existingFileHelper));
            generator.addProvider(new BlockModels(generator, existingFileHelper));
            generator.addProvider(new ItemModels(generator, existingFileHelper));
        }
    }
}
