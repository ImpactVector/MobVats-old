package net.impactvector.mobvats.common.multiblock.block;

import net.impactvector.mobvats.init.ModBlocks;
import net.impactvector.mobvats.init.ModItems;
import net.impactvector.mobvats.common.multiblock.PartType;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorFlesh;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorHead;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by dgerold on 11/30/2016.
 */
public class BlockMultiblockFlesh extends BlockMultiblockDevice {

    public BlockMultiblockFlesh(PartType type, String name) {

        super(type, name);
    }


    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {

        switch (this._type) {

            case ReactorFlesh:
                return new TileEntityReactorFlesh();

            default:
                throw new IllegalArgumentException("Invalid part type");
        }
    }

@Override
public void registerRecipes() {
    GameRegistry.addRecipe(
            new ShapedOreRecipe(
                    new ItemStack(ModBlocks.vatFlesh),
                    "brb", "rvr", "brb",
                    'b', Items.BONE,
                    'v', ModItems.vialEssence,
                    'r', Items.ROTTEN_FLESH));
}

}
