package com.hbn.outvoted.client.model;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.init.ModItems;
import net.minecraft.client.renderer.entity.model.ShieldModel;
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
public class InfernoShieldModel {

    public static final RenderMaterial base = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(Outvoted.MOD_ID, "entity/inferno_shield_base"));
    //public static final RenderMaterial basenop = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(Outvoted.MOD_ID,"entity/inferno_shield_base_nopattern"));

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        addShieldPropertyOverrides(new ResourceLocation(Outvoted.MOD_ID, "blocking"),
                (stack, world, entity) -> entity != null && entity.isHandActive()
                        && entity.getActiveItemStack() == stack ? 1.0F : 0.0F,
                ModItems.INFERNO_SHIELD.get());
    }

    private static void addShieldPropertyOverrides(ResourceLocation override, IItemPropertyGetter propertyGetter,
                                                   IItemProvider... shields) {
        for (IItemProvider shield : shields) {
            ItemModelsProperties.registerProperty(shield.asItem(), override, propertyGetter);
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            event.addSprite(base.getTextureLocation());
        }
    }
}