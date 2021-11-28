package io.github.how_bout_no.outvoted.forge.util;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModEntityTypes;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MappingsFixer {
    @SubscribeEvent
    public void fixItemMappings(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            if (entry.key.getNamespace().equals(Outvoted.MOD_ID)) {
                if (entry.key.getPath().equals("inferno_helmet")) {
                    entry.remap(ModItems.WILDFIRE_HELMET.get());
                }
                if (entry.key.getPath().equals("inferno_shield")) {
                    entry.remap(ModItems.WILDFIRE_SHIELD.get());
                }
                if (entry.key.getPath().equals("inferno_shield_part")) {
                    entry.remap(ModItems.WILDFIRE_SHIELD_PART.get());
                }
                if (entry.key.getPath().equals("inferno_piece")) {
                    entry.remap(ModItems.WILDFIRE_PIECE.get());
                }
                if (entry.key.getPath().equals("inferno_spawn_egg")) {
                    entry.remap(ModItems.WILDFIRE_SPAWN_EGG.get());
                }
            }
        }
    }

    @SubscribeEvent
    public void fixEntityMappings(RegistryEvent.MissingMappings<EntityType<?>> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityType<?>> entry : event.getAllMappings()) {
            if (entry.key.getNamespace().equals(Outvoted.MOD_ID)) {
                if (entry.key.getPath().equals("inferno")) {
                    entry.remap(ModEntityTypes.WILDFIRE.get());
                }
                if (entry.key.getPath().equals("hunger")) {
                    entry.remap(ModEntityTypes.GLUTTON.get());
                }
                if (entry.key.getPath().equals("kraken")) {
                    entry.remap(ModEntityTypes.BARNACLE.get());
                }
            }
        }
    }
}