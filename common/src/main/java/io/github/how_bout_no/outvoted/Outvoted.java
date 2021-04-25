package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.util.SignSprites;
import io.github.how_bout_no.outvoted.world.gen.WorldGen;
import me.shedaniel.architectury.registry.CreativeTabs;
import me.shedaniel.architectury.registry.RenderTypes;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.util.function.Supplier;

public class Outvoted {
    public static final String MOD_ID = "outvoted";
    public static OutvotedConfig config;

    public static ItemGroup TAB_BLOCKS;
    public static ItemGroup TAB_DECO;
    public static ItemGroup TAB_COMBAT;
    //public static ItemGroup TAB_MATERIAL;
    public static ItemGroup TAB_MISC;
    public static ItemGroup TAB_REDSTONE;

    public static void init() {
        AutoConfig.register(OutvotedConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        config = AutoConfig.getConfigHolder(OutvotedConfig.class).getConfig();

        GeckoLib.initialize();
        GeckoLibMod.DISABLE_IN_DEV = true;

        ModEntityTypes.ENTITY_TYPES.register();
        ModItems.ITEMS.register();
        ModBlocks.BLOCKS.register();
        ModBlocks.BLOCK_ITEMS.register();
        ModFeatures.FEATURES.register();
        ModRecipes.RECIPES.register();
        ModSounds.SOUNDS.register();
        new ModTags.Blocks();
        new ModTags.Items();
        WorldGen.addSpawnEntries();

        if (config.client.creativetab) {
            ItemGroup TAB = CreativeTabs.create(new Identifier(MOD_ID, "modtab"), new Supplier<ItemStack>() {
                @Override
                public ItemStack get() {
                    return new ItemStack(ModItems.WILDFIRE_HELMET.get());
                }
            });
            TAB_BLOCKS = TAB;
            TAB_DECO = TAB;
            TAB_COMBAT = TAB;
            TAB_MISC = TAB;
            TAB_REDSTONE = TAB;
        } else {
            TAB_BLOCKS = ItemGroup.BUILDING_BLOCKS;
            TAB_DECO = ItemGroup.DECORATIONS;
            TAB_COMBAT = ItemGroup.COMBAT;
            TAB_MISC = ItemGroup.MISC;
            TAB_REDSTONE = ItemGroup.REDSTONE;
        }
    }

    public static void clientInit() {
        RenderTypes.register(RenderLayer.getCutoutMipped(), ModBlocks.PALM_SAPLING.get(), ModBlocks.PALM_TRAPDOOR.get(), ModBlocks.PALM_DOOR.get(),
                ModBlocks.BAOBAB_SAPLING.get(), ModBlocks.BAOBAB_TRAPDOOR.get(), ModBlocks.BAOBAB_DOOR.get());

        SignSprites.addRenderMaterial(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/signs/palm")));
        SignSprites.addRenderMaterial(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/signs/baobab")));
    }
}
