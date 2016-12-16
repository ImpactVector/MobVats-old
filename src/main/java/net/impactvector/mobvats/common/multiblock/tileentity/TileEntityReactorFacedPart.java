package net.impactvector.mobvats.common.multiblock.tileentity;

import cofh.api.tileentity.IReconfigurableFacing;
import net.impactvector.mobvats.common.interfaces.IWrenchable;
import net.impactvector.mobvats.common.tileentity.base.TileEntityBeefBase;
import net.impactvector.mobvats.net.CommonPacketHandler;
import net.impactvector.mobvats.net.message.DeviceUpdateRotationMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zero.temp.BlockHelper;

/**
 * Created by dgerold on 12/10/2016.
 */
public class TileEntityReactorFacedPart extends TileEntityReactorPart implements IReconfigurableFacing, IWrenchable {
    protected EnumFacing facing;	// Tile rotation

    public TileEntityReactorFacedPart() {
        super();

        facing = EnumFacing.NORTH;
    }


    // IReconfigurableFacing
    @Override
    public int getFacing() { return facing.getIndex(); }

    @Override
    public boolean setFacing(EnumFacing newFacing) {
        if(facing == newFacing) { return false; }

        if(!allowYAxisFacing() && (newFacing == EnumFacing.UP || newFacing == EnumFacing.DOWN)) {
            return false;
        }

        facing = newFacing;
        if(!worldObj.isRemote) {
            BlockPos position = this.getPos();
            CommonPacketHandler.INSTANCE.sendToAllAround(new DeviceUpdateRotationMessage(this.getPos(), facing),
                    new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(),
                            position.getX(), position.getY(), position.getZ(), 50));
            this.markChunkDirty();
        }

        this.callNeighborBlockChange();
        return true;
    }

    @Override
    public boolean rotateBlock() {
        return setFacing(EnumFacing.VALUES[BlockHelper.SIDE_LEFT[facing.getIndex()]]);
    }

    @Override
    public boolean onWrench(EntityPlayer player, EnumFacing hitSide) {
        return rotateBlock();
    }

    @Override
    public boolean allowYAxisFacing() { return false; }

    @Override
    protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {

        // Rotation

        int newFacing;

        if(data.hasKey("facing")) {
            newFacing = Math.max(0, Math.min(5, data.getInteger("facing")));
        }
        else {
            newFacing = 2;
        }

        this.facing = EnumFacing.VALUES[newFacing];
    }

    @Override
    protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
        data.setInteger("facing", facing.getIndex());
    }

    /**
     * Fill this NBT Tag Compound with your custom entity data.
     * @param updateTag The tag to which your data should be written
     */
    protected void onSendUpdate(NBTTagCompound updateTag) {}

    /**
     * Read your custom update data from this NBT Tag Compound.
     * @param updateTag The tag which should contain your data.
     */
    public void onReceiveUpdate(NBTTagCompound updateTag) {}

    // Weird shit from TileCoFHBase
    public String getName() {
        return this.getBlockType().getUnlocalizedName();
    }

    public int getType() {
        return getBlockMetadata();
    }
}
