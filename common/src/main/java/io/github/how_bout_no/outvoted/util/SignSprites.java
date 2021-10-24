// Adapted from noobutil https://maven.blamejared.com/noobanidus/libs/noobutil/1.16.4-0.0.7.62/

package io.github.how_bout_no.outvoted.util;

import net.minecraft.client.util.SpriteIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SignSprites {
    private static final List<SpriteIdentifier> sprites = new ArrayList<>();

    public static void addRenderMaterial(SpriteIdentifier material) {
        sprites.add(material);
    }

    public static Collection<SpriteIdentifier> getSprites() {
        return Collections.unmodifiableCollection(sprites);
    }
}
