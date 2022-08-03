package io.github.how_bout_no.outvoted.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ForgeConfig {

    public static void register() {
//        registerServerConfigs();
        registerCommonConfigs();
        registerClientConfigs();
    }

    private static void registerClientConfigs() {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        Config.registerClientConfig(CLIENT_BUILDER);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    private static void registerCommonConfigs() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        Config.registerCommonConfig(COMMON_BUILDER);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

//    private static void registerServerConfigs() {
//        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
//        Config.registerServerConfig(SERVER_BUILDER);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
//    }
}
