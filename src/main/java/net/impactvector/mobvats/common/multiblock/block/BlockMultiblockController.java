package net.impactvector.mobvats.common.multiblock.block;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.Properties;
import net.impactvector.mobvats.common.multiblock.PartTier;
import net.impactvector.mobvats.common.multiblock.PartType;
import net.impactvector.mobvats.common.multiblock.interfaces.IActivateable;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorController;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityTurbineController;
import net.impactvector.mobvats.init.ModBlocks;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class BlockMultiblockController extends BlockMultiblockDevice {

    public BlockMultiblockController(PartType type, String blockName) {
        super(type, blockName);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {

        switch (this._type) {

            case ReactorController:
                return new TileEntityReactorController();

            case TurbineController:
                return new TileEntityTurbineController();

            default:
                throw new IllegalArgumentException("Invalid part type");
        }
    }

    @Override
    public void registerRecipes() {

        if (PartType.ReactorController == this._type) {

            if (PartTier.REACTOR_TIERS.contains(PartTier.Legacy))
                GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Legacy, 1), "CDC", "GGG", "CRC",
                        'D', Items.DIAMOND, 'G', "paneGlass",
                        'C', ModBlocks.reactorCasing.createItemStack(PartTier.Legacy, 1), 'R', Items.REDSTONE));

            if (PartTier.REACTOR_TIERS.contains(PartTier.Basic))
                GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Basic, 1), "CDC", "GGG", "CRC",
                        'D', Items.DIAMOND, 'G', "paneGlass",
                        'C', ModBlocks.reactorCasing.createItemStack(PartTier.Basic, 1), 'R', Items.REDSTONE));

        }
    }

    @Override
    protected void buildBlockState(BlockStateContainer.Builder builder) {

        super.buildBlockState(builder);
        builder.add(Properties.CONTROLLERSTATE);
    }

    @Override
    protected IBlockState buildDefaultState(IBlockState state) {

        return super.buildDefaultState(state).withProperty(Properties.CONTROLLERSTATE, ControllerState.Off);
    }

    @Override
    protected IBlockState buildActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos position,
                                           @Nonnull MultiblockTileEntityBase part) {

        MultiblockControllerBase controller = part.getMultiblockController();
        ControllerState controllerState = null == controller || !controller.isAssembled() || !(controller instanceof IActivateable) ?
                ControllerState.Off : ((IActivateable)controller).getActive() ? ControllerState.Active : ControllerState.Idle;

        return super.buildActualState(state, world, position, part).withProperty(Properties.CONTROLLERSTATE, controllerState);
    }
}
