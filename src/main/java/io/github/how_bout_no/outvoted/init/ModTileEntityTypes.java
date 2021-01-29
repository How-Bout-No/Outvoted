package io.github.how_bout_no.outvoted.init;

import io.github.how_bout_no.outvoted.Outvoted;
import io.github.how_bout_no.outvoted.tileentity.PalmSignTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Outvoted.MOD_ID);

    public static final RegistryObject<TileEntityType<PalmSignTileEntity>> PALM_SIGN = TILE_ENTITY_TYPES.register("palm_sign", () -> TileEntityType.Builder.create(PalmSignTileEntity::new, ModBlocks.PALM_SIGN.get()).build(null));
}
