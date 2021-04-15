package io.github.how_bout_no.outvoted.config;

import java.util.ArrayList;
import java.util.List;

public interface EntityConfigBase {
    boolean spawn = true;
    int rate = 1;
    List<String> biomes = new ArrayList<>();
    double health = 20.0D;
}
