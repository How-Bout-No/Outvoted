// Adapted from noobutil https://maven.blamejared.com/noobanidus/libs/noobutil/1.16.4-0.0.7.62/

package io.github.how_bout_no.outvoted.util;

import net.minecraft.client.resources.model.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SignSprites {
    private static final List<Material> sprites = new ArrayList<>();

    public static void addRenderMaterial(Material material) {
        sprites.add(material);
    }

    public static Collection<Material> getSprites() {
        return Collections.unmodifiableCollection(sprites);
    }
}
