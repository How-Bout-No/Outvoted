package io.github.how_bout_no.outvoted.util.forge;

import io.github.how_bout_no.outvoted.client.render.ClientRender;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class OutvotedModPlatformImpl {
    public static Item.Settings setISTER(Item.Settings properties) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientRender.addISTER(properties));
        return properties;
    }

    public static boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }
}
