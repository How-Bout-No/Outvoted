package io.github.how_bout_no.outvoted.world;

import net.minecraftforge.common.BiomeDictionary;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DefaultBiomes {
    /*
        Automatically generates lists containing default spawn biomes for each mob
        Not the *most* elegant solution but it works

        The benefit of this is that it will also grab modded biomes that fit into these categories and can be pulled into config on
        first start or regen
     */
    public static List<String> WILDFIRE = BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER).stream()
            .flatMap(key -> Stream.of(key.getValue().toString())).collect(Collectors.toList());
    public static List<String> HUNGER = Stream.of(BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP), BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY))
            .flatMap(Collection::stream).flatMap(key -> Stream.of(key.getValue().toString())).collect(Collectors.toList());
    public static List<String> KRAKEN = BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN).stream().filter(biomeRegistryKey -> {
        String path = biomeRegistryKey.getValue().getPath();
        return path.contains("deep") && !path.contains("frozen");
    }).flatMap(key -> Stream.of(key.getValue().toString())).collect(Collectors.toList());
}
