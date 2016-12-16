package net.impactvector.mobvats.common.multiblock.helpers;

//import erogenousbeef.bigreactors.api.data.ReactantData;
//import erogenousbeef.bigreactors.api.registry.Reactants;
import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.multiblock.MultiblockReactor;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorHead;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorFlesh;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zero.temp.BlockHelper;

import java.util.ArrayList;
import java.util.List;

public class MobAssembly {

    public MobAssembly(final TileEntityReactorHead head) {
        if (null == head || !this.build(head))
            throw new IllegalStateException("Invalid homunculus assembly");

        currLearnTicks = 0;
        currSpawnTicks = 0;
        spawnReq = MobVats.spawnerManager.getSpawnReq(head.getMobName(), head);
    }

    public AxisAlignedBB lootLocation;
    public TileEntityReactorHead getMobHead() {
        return this._head;
    }

    public EnumFacing.Axis getAxis() {
        return this._cachedOutwardFacing.getAxis();
    }

    public EnumFacing[] getRadiateDirections() {

        switch (this.getAxis()) {

            case X:
                return RADIATE_DIRECTIONS_X_AXIS;

            default:
            case Y:
                return RADIATE_DIRECTIONS_Y_AXIS;

            case Z:
                return RADIATE_DIRECTIONS_Z_AXIS;
        }
    }

    public int currLearnTicks;
    public int currSpawnTicks;
    public SpawnerManager.SpawnReq spawnReq;

    public int getFleshCount() {
        return this._flesh.size();
    }

    public FleshData getFuelRodData(int rodIndex) {
        return rodIndex >= 0 && rodIndex < this._flesh.size() ? this._flesh.get(rodIndex) : null;
    }

    private boolean build(final TileEntityReactorHead homHead) {

        MultiblockReactor vat = homHead.getReactorController();

        this._head = homHead;
        this._cachedOutwardFacing = EnumFacing.VALUES[BlockHelper.SIDE_ABOVE[homHead.getFacing()]];

        if (null == vat)
            return false;

        BlockPos minCoord = vat.getMinimumCoord();
        BlockPos maxCoord = vat.getMaximumCoord();
        BlockPos lookupPosition = homHead.getWorldPosition();
        EnumFacing lookupDirection = this._cachedOutwardFacing.getOpposite();

//        switch (this.getAxis()) {
//
//            case X:
//                fleshCount = maxCoord.getX() - minCoord.getX() - 1;
//                break;
//
//            case Y:
//                fleshCount = maxCoord.getY() - minCoord.getY() - 1;
//                lookupPosition = new BlockPos(lookupPosition.getX(), minCoord.getY(), lookupPosition.getZ());
//                lookupDirection = EnumFacing.UP;
//                break;
//
//            case Z:
//                fleshCount = maxCoord.getZ() - minCoord.getZ() - 1;
//                break;
//        }


        // capture all fuel rods in this assembly

        World world = homHead.getWorld();
        TileEntity te;
        this._flesh = new ArrayList<FleshData>();

        lookupPosition = lookupPosition.offset(lookupDirection);
        te = world.getTileEntity(lookupPosition);

        while (te instanceof TileEntityReactorFlesh)
        {
            this._flesh.add(new FleshData((TileEntityReactorFlesh)te));
            ((TileEntityReactorFlesh)te).linkToAssembly(this);

            lookupPosition = lookupPosition.offset(lookupDirection);
            te = world.getTileEntity(lookupPosition);
        } ;

        return true;
    }

    public class FleshData {

        public final TileEntityReactorFlesh Flesh;

        public FleshData(TileEntityReactorFlesh flesh) {

            this.Flesh = flesh;
        }

        public void update(float fuelAmount, float wasteAmount) {

        }
    }

    private TileEntityReactorHead _head;
    private List<FleshData> _flesh;
    private EnumFacing _cachedOutwardFacing;

    private static final EnumFacing[] RADIATE_DIRECTIONS_X_AXIS = {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.SOUTH};
    private static final EnumFacing[] RADIATE_DIRECTIONS_Y_AXIS = EnumFacing.HORIZONTALS;
    private static final EnumFacing[] RADIATE_DIRECTIONS_Z_AXIS = {EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.WEST, EnumFacing.UP};
}
