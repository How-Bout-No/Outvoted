package io.github.how_bout_no.outvoted;

import io.github.how_bout_no.outvoted.config.OutvotedConfig;
import io.github.how_bout_no.outvoted.entity.util.MobAttributes;
import io.github.how_bout_no.outvoted.init.ModFeatures;
import io.github.how_bout_no.outvoted.init.ModFireBlock;
import io.github.how_bout_no.outvoted.item.ModSpawnEggItem;
import io.github.how_bout_no.outvoted.util.compat.PatchouliCompat;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;

public class OutvotedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Outvoted.init();
        MobAttributes.init();
        ModSpawnEggItem.initSpawnEggs();
        ModFeatures.Configured.registerConfiguredFeatures();
        ModFireBlock.init();

        AutoConfig.getConfigHolder(OutvotedConfig.class).registerSaveListener((manager, data) -> {
            System.out.println("save");
            if (FabricLoader.getInstance().isModLoaded("patchouli")) {
                PatchouliCompat.updateFlag();
            }
            return ActionResult.SUCCESS;
        });
        AutoConfig.getConfigHolder(OutvotedConfig.class).registerLoadListener((manager, newData) -> {
            System.out.println("load");
            if (FabricLoader.getInstance().isModLoaded("patchouli")) {
                PatchouliCompat.updateFlag();
            }
            return ActionResult.SUCCESS;
        });
    }
}