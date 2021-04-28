package io.github.how_bout_no.outvoted.config;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityConfigBase {
    @Getter
    static boolean spawn = true;
    @Getter
    static int rate = 1;
    @Getter
    static List<String> biomes = new ArrayList<>();
    @Getter
    static double health = 20.0D;
}
