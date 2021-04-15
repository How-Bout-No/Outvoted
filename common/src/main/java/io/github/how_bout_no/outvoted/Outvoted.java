package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.init.*;
import me.shedaniel.architectury.registry.CreativeTabs;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.util.function.Supplier;

public class Outvoted {
    public static final String MOD_ID = "outvoted";
    public static ConfigHolder<OutvotedConfig> config;

    public static ItemGroup TAB_BLOCKS;
    public static ItemGroup TAB_DECO;
    public static ItemGroup TAB_COMBAT;
    //public static ItemGroup TAB_MATERIAL;
    public static ItemGroup TAB_MISC;
    public static ItemGroup TAB_REDSTONE;

    public static void init() {
        config = AutoConfig.register(OutvotedConfig.class, GsonConfigSerializer::new);
        config.getConfig();

        GeckoLib.initialize();
        GeckoLibMod.DISABLE_IN_DEV = true;

        ModEntityTypes.ENTITY_TYPES.register();
        ModItems.ITEMS.register();
        ModBlocks.BLOCKS.register();
        ModBlocks.BLOCK_ITEMS.register();
        ModFeatures.FEATURES.register();
        ModRecipes.RECIPES.register();
        ModSounds.SOUNDS.register();
        ModTags.init();

        if (config.get().client.creativetab) {
            ItemGroup TAB = CreativeTabs.create(new Identifier(MOD_ID, "modtab"), new Supplier<ItemStack>() {
                @Override
                public ItemStack get() {
                    return new ItemStack(ModItems.WILDFIRE_HELMET.get());
                }
            });
            TAB.setTexture("item_search.png");
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
}
