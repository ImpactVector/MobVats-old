package net.impactvector.mobvats.common.multiblock.block;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.multiblock.PartTier;
import net.impactvector.mobvats.common.multiblock.PartType;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorControlRod;
import net.impactvector.mobvats.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockReactorControlRod extends BlockMultiblockDevice {

    public BlockReactorControlRod(String blockName) {

        super(PartType.ReactorControlRod, blockName);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityReactorControlRod();
    }

    @Override
    public void registerRecipes() {

        if (PartTier.REACTOR_TIERS.contains(PartTier.Legacy))
            GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Legacy, 1), "C C", " R ", "C C",
                'C', ModBlocks.reactorCasing.createItemStack(PartTier.Legacy, 1),
                'R', Items.REDSTONE));

        if (PartTier.REACTOR_TIERS.contains(PartTier.Basic))
            GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Basic, 1), "C C", " R ", "C C",
                'C', ModBlocks.reactorCasing.createItemStack(PartTier.Basic, 1),
                'R', Items.REDSTONE));
    }
}
