package io.github.how_bout_no.outvoted.util.compat;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class XaeroCompat {
    private static StringBuilder VARIANT_STRING_BUILDER = new StringBuilder();

    public static String getVariantString(EntityRenderer entityRenderer, Entity entity) {
        Identifier entityTexture = entityRenderer.getTexture(entity);
        if (entityTexture == null)
            return "";
        StringBuilder stringBuilder = VARIANT_STRING_BUILDER;
        stringBuilder.setLength(0);
        stringBuilder.append(entityTexture);
        return stringBuilder.toString();
    }
}
