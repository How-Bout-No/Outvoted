package io.github.how_bout_no.outvoted;

import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.config.OutvotedConfigClient;
import io.github.how_bout_no.outvoted.config.OutvotedConfigCommon;
import io.github.how_bout_no.outvoted.entity.*;
import io.github.how_bout_no.outvoted.init.*;
import io.github.how_bout_no.outvoted.util.EventRegister;
import io.github.how_bout_no.outvoted.util.compat.PatchouliCompat;
import io.github.how_bout_no.outvoted.world.gen.WorldGen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

public class Outvoted {
    public static final String MOD_ID = "outvoted";
    public static OutvotedConfig config;
    public static OutvotedConfigCommon commonConfig;
    public static OutvotedConfigClient clientConfig;

    public static ItemGroup TAB_BLOCKS = ItemGroup.BUILDING_BLOCKS;
    public static ItemGroup TAB_DECO = ItemGroup.DECORATIONS;
    public static ItemGroup TAB_COMBAT = ItemGroup.COMBAT;
    //public static ItemGroup TAB_MATERIAL;
    public static ItemGroup TAB_REDSTONE = ItemGroup.REDSTONE;
    public static ItemGroup TAB_MISC = ItemGroup.MISC;
    public static ItemGroup[] TABS = new ItemGroup[]{Outvoted.TAB_BLOCKS, Outvoted.TAB_COMBAT, Outvoted.TAB_DECO, Outvoted.TAB_MISC, Outvoted.TAB_REDSTONE};

    public static void init() {
        AutoConfig.register(OutvotedConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        config = AutoConfig.getConfigHolder(OutvotedConfig.class).getConfig();
        clientConfig = config.client;
        commonConfig = config.common;

        GeckoLib.initialize();
        GeckoLibMod.DISABLE_IN_DEV = true;

        ModBlocks.BLOCKS.register();
        ModBlocks.BLOCK_ITEMS.register();
        ModEntityTypes.ENTITY_TYPES.register();
        ModItems.ITEMS.register();
        ModLootConditionTypes.CONDITIONS.register();
        ModRecipes.RECIPES.register();
        ModSounds.SOUNDS.register();
        ModTags.init();
        EventRegister.init();
        WorldGen.addSpawnEntries();

//        if (Platform.getEnv() == EnvType.CLIENT) {
//            SignSprites.addRenderMaterial(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/signs/palm")));
//            SignSprites.addRenderMaterial(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, new Identifier(Outvoted.MOD_ID, "entity/signs/baobab")));
//        }

        EntityAttributeRegistry.register(ModEntityTypes.WILDFIRE::get, WildfireEntity::setCustomAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.GLUTTON::get, GluttonEntity::setCustomAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.BARNACLE::get, BarnacleEntity::setCustomAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.GLARE::get, GlareEntity::setCustomAttributes);
        EntityAttributeRegistry.register(ModEntityTypes.COPPER_GOLEM::get, CopperGolemEntity::setCustomAttributes);
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        if (clientConfig.creativeTab) {
            ItemGroup TAB = CreativeTabRegistry.create(new Identifier(MOD_ID, "tab"), () -> new ItemStack(ModItems.WILDFIRE_HELMET.get()));
            TAB_BLOCKS = TAB;
            TAB_DECO = TAB;
            TAB_COMBAT = TAB;
            TAB_MISC = TAB;
            TAB_REDSTONE = TAB;
        }

        if (Platform.isModLoaded("patchouli")) PatchouliCompat.updateFlag();

        AutoConfig.getConfigHolder(OutvotedConfig.class).registerSaveListener((manager, data) -> {
            if (Platform.isModLoaded("patchouli")) PatchouliCompat.updateFlag();
            return ActionResult.SUCCESS;
        });

        AutoConfig.getConfigHolder(OutvotedConfig.class).registerLoadListener((manager, newData) -> {
            if (Platform.isModLoaded("patchouli")) PatchouliCompat.updateFlag();
            return ActionResult.SUCCESS;
        });
    }
}
