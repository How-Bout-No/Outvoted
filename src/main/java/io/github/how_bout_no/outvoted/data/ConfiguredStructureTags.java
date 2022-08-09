package io.github.how_bout_no.outvoted.data;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ConfiguredStructureTagsProvider;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ConfiguredStructureTags extends ConfiguredStructureTagsProvider {
    public ConfiguredStructureTags(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, Outvoted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.IN_DESERT)
                .add(BuiltinStructures.DESERT_PYRAMID)
                .add(BuiltinStructures.VILLAGE_DESERT)
                .add(BuiltinStructures.BURIED_TREASURE)
                .add(BuiltinStructures.RUINED_PORTAL_DESERT);
    }

    public String getName() {
        return "Configured Structure Feature Tags";
    }
}
