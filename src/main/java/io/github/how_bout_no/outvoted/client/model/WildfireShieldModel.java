// Adapted from https://github.com/ToMe25/Better-Shields/

package io.github.how_bout_no.outvoted.client.model;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModItems;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Outvoted.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WildfireShieldModel {

    public static final RenderMaterial base = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Outvoted.MOD_ID, "entity/wildfire_shield_base"));

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        addShieldPropertyOverrides(new ResourceLocation(Outvoted.MOD_ID, "blocking"),
                (stack, world, entity) -> entity != null && entity.isUsingItem()
                        && entity.getUseItem() == stack ? 1.0F : 0.0F,
                ModItems.WILDFIRE_SHIELD.get());
    }

    private static void addShieldPropertyOverrides(ResourceLocation override, IItemPropertyGetter propertyGetter,
                                                   IItemProvider... shields) {
        for (IItemProvider shield : shields) {
            ItemModelsProperties.register(shield.asItem(), override, propertyGetter);
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
            event.addSprite(base.texture());
        }
    }
}