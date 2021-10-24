package io.github.how_bout_no.outvoted.forge.client.render;

import io.github.how_bout_no.outvoted.client.WildfireShield;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientRender {
    public static void addISTER(Item.Settings properties) {
//        properties.setISTER(() -> () -> new ModItemModelRender());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerTextureAtlas(TextureStitchEvent.Pre event) {
        event.addSprite(WildfireShield.base.getTextureId());
        event.addSprite(WildfireShield.base_nop.getTextureId());
    }
}
