package net.impactvector.mobvats.common.multiblock.tileentity;

import cofh.api.tileentity.IReconfigurableFacing;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.util.WorldHelper;
import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.init.ModBlocks;
import net.impactvector.mobvats.common.multiblock.MultiblockReactor;
import net.impactvector.mobvats.net.CommonPacketHandler;
import net.impactvector.mobvats.net.message.DeviceUpdateRotationMessage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityReactorHead extends TileEntityReactorFacedPart {


    String mobName;
    String displayName;
    int xpValue;

    static final String NBT_MOB_NAME = "mobName";
    public static final String NBT_DISPLAY_NAME = "displayName";
    static final String NBT_XP_VALUE = "mobXpCost";

//    // Radiation
//    protected short controlRodInsertion; // 0 = retracted fully, 100 = inserted fully

    // User settings
    protected String name;

    public TileEntityReactorHead() {
        super();

        name = "";
        mobName = "";
        displayName = "";
        xpValue = 1;
    }

    @Override
    protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {

        super.syncDataFrom(data, syncReason);

        if (SyncReason.NetworkUpdate == syncReason) {

            this.readLocalDataFromNBT(data);

        } else {

            if(data.hasKey("vatHead")) {
                NBTTagCompound localData = data.getCompoundTag("vatHead");
                this.readLocalDataFromNBT(localData);

                if(worldObj != null && worldObj.isRemote) {
                    WorldHelper.notifyBlockUpdate(this.worldObj, this.getPos(), null, null);
                }
            }
        }
    }

    @Override
    protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {

        super.syncDataTo(data, syncReason);

        if (SyncReason.FullSync == syncReason) {

            this.writeLocalDataToNBT(data);

        } else {

            NBTTagCompound localData = new NBTTagCompound();
            this.writeLocalDataToNBT(localData);
            data.setTag("vatHead", localData);
        }
    }

//    @Override
//    public Object getServerGuiElement(int guiId, EntityPlayer player) {
//        return new ContainerBasic();
//    }

//    @Override
//    public Object getClientGuiElement(int guiId, EntityPlayer player) {
//        return new GuiReactorControlRod(new ContainerBasic(), this);
//    }

    // TileEntityReactorPart
    @Override
    public boolean isGoodForFrame(IMultiblockValidator validatorCallback) {

        BlockPos position = this.getPos();

        validatorCallback.setLastError("multiblock.validation.reactor.invalid_homunculus_head_position",
                position.getX(), position.getY(), position.getZ());
        return false;
    }

    @Override
    public boolean isGoodForSides(IMultiblockValidator validatorCallback) {

        BlockPos position = this.getPos();

        validatorCallback.setLastError("multiblock.validation.reactor.invalid_homunculus_head_position",
                position.getX(), position.getY(), position.getZ());

        return false;
    }

    @Override
    public boolean isGoodForTop(IMultiblockValidator validatorCallback) {

        BlockPos position = this.getPos();
        validatorCallback.setLastError("multiblock.validation.reactor.invalid_control_rods_column",
                    position.getX(), position.getY(), position.getZ());
        return false;
    }

    @Override
    public boolean isGoodForBottom(IMultiblockValidator validatorCallback) {
        BlockPos position = this.getPos();
        validatorCallback.setLastError("multiblock.validation.reactor.invalid_control_rods_column",
                position.getX(), position.getY(), position.getZ());
        return false;
    }


    @Override
    public boolean isGoodForInterior(IMultiblockValidator validatorCallback) {
        if (validatorCallback instanceof MultiblockReactor) {
            MultiblockReactor vat = (MultiblockReactor) validatorCallback;
            if (!vat.attachedHeads.contains(this))
                vat.attachBlock(this);
            return true;
        }
        else
            return false;
    }

    private boolean checkForFlesh(EnumFacing fleshDirection) {
        return null != fleshDirection && this.worldObj.getTileEntity(this.getWorldPosition().offset(fleshDirection)) instanceof TileEntityReactorFlesh;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        readHeadFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeHeadToNBT(compound);
        return compound;
    }

    // Save/Load Helpers
    private void readLocalDataFromNBT(NBTTagCompound data) {
        readHeadFromNBT(data);

        if(data.hasKey("name")) {
            this.name = data.getString("name");
        }
        else {
            this.name = "";
        }
    }

    private void writeLocalDataToNBT(NBTTagCompound data) {
        writeHeadToNBT(data);

        if(!this.name.isEmpty()) {
            data.setString("name", this.name);
        }
    }

    public void writeHeadToNBT(NBTTagCompound compound) {

        compound.setString(NBT_MOB_NAME, mobName);
        compound.setString(NBT_DISPLAY_NAME, displayName);
        compound.setInteger(NBT_XP_VALUE, xpValue);
    }

    public void readHeadFromNBT(NBTTagCompound compound) {

        mobName = compound.getString(NBT_MOB_NAME);
        displayName = compound.getString(NBT_DISPLAY_NAME);
        xpValue = compound.getInteger(NBT_XP_VALUE);
    }

    public void setMobName(String mobName, String displayName, int xp) {

        this.mobName = mobName;
        this.displayName = displayName;
        this.xpValue = xp;
        markDirty();
        //don't need to do this because you shouldn't be able to update while the vat is formed
        //updateMobFarm();
    }

    public String getMobName() {

        return mobName;
    }

    public String getMobDisplayName() {

        return displayName;
    }

    public int getXpValue() {

        return xpValue;
    }

//    public void blockAdded() {
//
//        updateMobFarm();
//    }
//
//    @Override
//    public void invalidate() {
//
//        updateMobFarm();
//    }

    public ItemStack getDroppedItemStack() {

        ItemStack itemStack = new ItemStack(Item.getItemFromBlock(ModBlocks.vatHead), 1);
        if (MobVats.mobRegistry.isValidMobName(mobName)) {
            NBTTagCompound tag = new NBTTagCompound();
            writeHeadToNBT(tag);
            itemStack.setTagCompound(tag);
        }

        return itemStack;
    }
}
