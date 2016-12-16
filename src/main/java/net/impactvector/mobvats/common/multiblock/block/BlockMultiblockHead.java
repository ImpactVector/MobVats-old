package net.impactvector.mobvats.common.multiblock.block;

import cofh.api.block.IDismantleable;
import cofh.api.tileentity.IReconfigurableFacing;
import it.zerono.mods.zerocore.lib.block.properties.Orientation;
import it.zerono.mods.zerocore.util.WorldHelper;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.interfaces.IWrenchable;
import net.impactvector.mobvats.init.ModBlocks;
import net.impactvector.mobvats.init.ModItems;
import net.impactvector.mobvats.common.item.ItemVialEssence;
import net.impactvector.mobvats.common.multiblock.PartType;
import net.impactvector.mobvats.plugins.top.ITOPInfoProvider;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorHead;
import net.impactvector.mobvats.plugins.waila.WailaInfoProvider;
import net.impactvector.mobvats.utils.StaticUtils;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ImpactVector on 12/1/2016.
 */
public class BlockMultiblockHead extends BlockMultiblockDevice implements ITOPInfoProvider, WailaInfoProvider, IDismantleable
{

    public BlockMultiblockHead(PartType type, String name) {

        super(type, name);

        this.setDefaultState(
                this.blockState.getBaseState()
                        .withProperty(Orientation.HFACING, EnumFacing.NORTH)
        );
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {

        switch (this._type) {

            case ReactorHead:
                return new TileEntityReactorHead();

            default:
                throw new IllegalArgumentException("Invalid part type");
        }
    }

    @Override
    public void registerRecipes() {
        GameRegistry.addRecipe(
                new ShapedOreRecipe(
                        new ItemStack(ModBlocks.vatHead),
                        "rbr", "eve", "rbr",
                        'b', Items.BONE,
                        'v', ModItems.vialEssence,
                        'r', Items.ROTTEN_FLESH,
                        'e', Items.SPIDER_EYE
                        ));

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.vatHead), new ItemStack(ModBlocks.vatHead));
    }


    @Override
    protected void buildBlockState(BlockStateContainer.Builder builder) {

        super.buildBlockState(builder);
        builder.add(Orientation.HFACING);
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos position, IBlockState state, EntityPlayer player, EnumHand hand,
                                    ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        TileEntity te = world.getTileEntity(position);
        if(te == null) { return false; }

        if(player.isSneaking()) {

            // Wrench + Sneak = Dismantle
            if(StaticUtils.Inventory.isPlayerHoldingWrench(heldItem)) {
                // Pass simulate == true on the client to prevent creation of "ghost" item stacks
                dismantleBlock(player, world, position, false);
                return true;
            }

            return false;
        }

        if(te instanceof IWrenchable && StaticUtils.Inventory.isPlayerHoldingWrench(heldItem)) {
            return ((IWrenchable)te).onWrench(player, side);
        }

        if (!player.worldObj.isRemote){
            if(heldItem != null
                && heldItem.getItem() instanceof ItemVialEssence) {

                if (ItemVialEssence.hasMobName(heldItem)) {
                    if (te instanceof TileEntityReactorHead) {
                        if (!MobVats.mobRegistry.isValidMobName(((TileEntityReactorHead) te).getMobName())) {
                            ((TileEntityReactorHead) te).setMobName(ItemVialEssence.getMobName(heldItem), ItemVialEssence.getDisplayName(heldItem), ItemVialEssence.getXp(heldItem));
                            if (!player.capabilities.isCreativeMode)
                                heldItem.stackSize--;
                            return true;
                        }
                    }
                }
                else {
                    player.addChatComponentMessage(new TextComponentString("Mob essence required."));
                    return false;
                }
            }
            else {
                return super.onBlockActivated(world, position, state, player, hand, heldItem, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }


    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                     int meta, EntityLivingBase placer) {

        facing = (null != placer) ? placer.getHorizontalFacing().getOpposite() : EnumFacing.NORTH;

        return this.getDefaultState().withProperty(Orientation.HFACING, facing);
    }

    @Override
    public void onBlockAdded(World world, BlockPos position, IBlockState state) {

        EnumFacing newFacing = Orientation.suggestDefaultHorizontalFacing(world, position,
                state.getValue(Orientation.HFACING));

        world.setBlockState(position, state.withProperty(Orientation.HFACING, newFacing), 2);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {

        EnumFacing enumfacing = EnumFacing.getFront(meta);

        return this.getDefaultState().withProperty(Orientation.HFACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(Orientation.HFACING)).getIndex();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileEntityReactorHead) {
            TileEntityReactorHead head = (TileEntityReactorHead)te;

            if (!head.getMobDisplayName().equals((""))) {

                probeInfo.text(TextFormatting.GREEN + "Mob: " + head.getMobDisplayName());
            }
            else {
                probeInfo.text(TextFormatting.GREEN + "Mob: " + "None");
            }
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (te instanceof TileEntityReactorHead) {
            TileEntityReactorHead head = (TileEntityReactorHead) te;
            if (!head.getMobDisplayName().equals((""))) {
                currenttip.add(TextFormatting.GREEN + "Mob: " + head.getMobDisplayName());
            }
            else {
                currenttip.add(TextFormatting.GREEN + "Mob: " + "None");
            }
        }
        return currenttip;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos position, boolean returnDrops) {

        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        IBlockState blockState = world.getBlockState(position);

        stacks.add(new ItemStack(getItemDropped(blockState, world.rand, 0), 1, damageDropped(blockState)));

        if(returnDrops) {
            TileEntity te = world.getTileEntity(position);

            if(te instanceof IInventory) {
                IInventory invTe = (IInventory)te;
                for(int i = 0; i < invTe.getSizeInventory(); i++) {
                    ItemStack stack = invTe.getStackInSlot(i);
                    if(stack != null) {
                        stacks.add(stack);
                        invTe.setInventorySlotContents(i, null);
                    }
                }
            }
        }

        world.setBlockToAir(position);

        if(!returnDrops) {

            int x = position.getX(), y = position.getY(), z = position.getZ();

            for(ItemStack stack: stacks) {
                WorldHelper.spawnItemStack(stack, world, x, y, z, true);
            }
        }

        return stacks;
    }

    @Override
    public boolean canDismantle(EntityPlayer entityPlayer, World world, BlockPos blockPos) {
        return true;
    }
}
