package net.impactvector.mobvats.common.multiblock.block;

import net.impactvector.mobvats.common.Properties;
import net.impactvector.mobvats.common.multiblock.IInputOutputPort;
import net.impactvector.mobvats.common.multiblock.PartTier;
import net.impactvector.mobvats.common.multiblock.PartType;
import net.impactvector.mobvats.common.multiblock.interfaces.INeighborUpdatableEntity;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorAccessPort;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorCoolantPort;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityTurbineFluidPort;
import net.impactvector.mobvats.common.multiblock.tileentity.creative.TileEntityReactorCreativeCoolantPort;
import net.impactvector.mobvats.common.multiblock.tileentity.creative.TileEntityTurbineCreativeSteamGenerator;
import net.impactvector.mobvats.init.ModBlocks;
import net.impactvector.mobvats.utils.StaticUtils;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.util.WorldHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockMultiblockIOPort extends BlockMultiblockDevice {

    public BlockMultiblockIOPort(PartType type, String blockName) {
        super(type, blockName);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {

        switch (this._type) {

            case ReactorAccessPort:
                return new TileEntityReactorAccessPort();

            case ReactorCoolantPort:
                return new TileEntityReactorCoolantPort();

            case ReactorCreativeCoolantPort:
                return new TileEntityReactorCreativeCoolantPort();

            case TurbineFluidPort:
                return new TileEntityTurbineFluidPort();

            case TurbineCreativeSteamGenerator:
                return new TileEntityTurbineCreativeSteamGenerator();

            default:
                throw new IllegalArgumentException("Unrecognized part");
        }
    }

    @Override
    public void registerRecipes() {

        if (PartType.ReactorAccessPort == this._type) {

            if (PartTier.REACTOR_TIERS.contains(PartTier.Legacy))
                GameRegistry.addRecipe(this.createItemStack(PartTier.Legacy, 1), "C C", " V ", "CPC",
                        'C', ModBlocks.reactorCasing.createItemStack(PartTier.Legacy, 1), 'V', Blocks.CHEST, 'P', Blocks.PISTON);

            if (PartTier.REACTOR_TIERS.contains(PartTier.Basic))
                GameRegistry.addRecipe(this.createItemStack(PartTier.Basic, 1), "C C", " V ", "CPC",
                        'C', ModBlocks.reactorCasing.createItemStack(PartTier.Basic, 1), 'V', Blocks.CHEST, 'P', Blocks.PISTON);

        } else if (PartType.ReactorCoolantPort == this._type) {

            if (PartTier.REACTOR_TIERS.contains(PartTier.Legacy))
                GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Legacy, 1), "C C", " V ", "CPC",
                        'C', ModBlocks.reactorCasing.createItemStack(PartTier.Legacy, 1), 'V', Items.BUCKET, 'P', Blocks.PISTON
                        ));

            if (PartTier.REACTOR_TIERS.contains(PartTier.Basic))
                GameRegistry.addRecipe(new ShapedOreRecipe(this.createItemStack(PartTier.Basic, 1), "C C", " V ", "CPC",
                        'C', ModBlocks.reactorCasing.createItemStack(PartTier.Basic, 1), 'V', Items.BUCKET, 'P', Blocks.PISTON
                        ));

        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        TileEntity te = world.getTileEntity(pos);
        boolean hasWrench = StaticUtils.Inventory.isPlayerHoldingWrench(heldItem);

        if (te instanceof TileEntityReactorCreativeCoolantPort) {

            if (WorldHelper.calledByLogicalServer(world)) {

                TileEntityReactorCreativeCoolantPort cp = (TileEntityReactorCreativeCoolantPort) te;

                if (heldItem == null || hasWrench)
                    // Use wrench to change inlet/outlet state
                    cp.toggleDirection(true);
                else
                    cp.forceAddWater();
            }

            return true;
        }

        if (hasWrench && te instanceof IInputOutputPort) {

            if (WorldHelper.calledByLogicalServer(world))
                ((IInputOutputPort)te).toggleDirection(true);

            return true;
        }

        return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
    }

    /**
     * Called when a tile entity on a side of this block changes is created or is destroyed.
     * @param world The world
     * @param position Block position in world
     * @param neighbor Block position of neighbor
     */
    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos position, BlockPos neighbor) {

        TileEntity te = world.getTileEntity(position);

        // Signal power taps and other ports when their neighbors change, etc.
        if (te instanceof INeighborUpdatableEntity) {
            ((INeighborUpdatableEntity)te).onNeighborTileChange(world, position, neighbor);
        }
    }

    @Override
    protected void buildBlockState(BlockStateContainer.Builder builder) {

        super.buildBlockState(builder);
        builder.add(Properties.PORTDIRECTION);
    }

    @Override
    protected IBlockState buildDefaultState(IBlockState state) {
        return super.buildDefaultState(state).withProperty(Properties.PORTDIRECTION, PortDirection.Inlet);
    }

    @Override
    protected IBlockState buildActualState(IBlockState state, IBlockAccess world, BlockPos position, MultiblockTileEntityBase part) {

        state = super.buildActualState(state, world, position, part);

        if (part instanceof IInputOutputPort)
            state = state.withProperty(Properties.PORTDIRECTION,
                    ((IInputOutputPort)part).getDirection().isInput() ? PortDirection.Inlet : PortDirection.Outlet);

        return state;
    }
}
