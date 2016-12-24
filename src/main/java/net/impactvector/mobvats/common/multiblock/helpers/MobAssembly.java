package net.impactvector.mobvats.common.multiblock.helpers;

//import erogenousbeef.bigreactors.api.data.ReactantData;
//import erogenousbeef.bigreactors.api.registry.Reactants;
import it.zerono.mods.zerocore.util.ItemHelper;
import net.impactvector.mobvats.api.data.ReactorInteriorData;
import net.impactvector.mobvats.api.registry.ReactorInterior;
import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.ModEventLog;
import net.impactvector.mobvats.common.multiblock.MultiblockReactor;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorHead;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorFlesh;
import net.impactvector.mobvats.utils.FloatAverager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
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

    public float absorption, burnRate, nutrition;

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

        BlockPos maxPos = homHead.getWorldPosition().add(1,1,1); //head is at the top, look one above and positive in both directions
        BlockPos minPos = lookupPosition.add(-1,0,-1); //last position is already below the last flesh block

//        List<Float> absorptions = new ArrayList<Float>();
//        List<Float> burnRates = new ArrayList<Float>();
//        List<Float> nutritions = new ArrayList<Float>();

        int numBlocks = (maxPos.getX() - minPos.getX())
                * (maxPos.getY() - minPos.getY())
                * (maxPos.getZ() - minPos.getZ())
                - (_flesh.size() + 1);

        FloatAverager absorptions = new FloatAverager(numBlocks);
        FloatAverager burnRates = new FloatAverager(numBlocks);
        FloatAverager nutritions = new FloatAverager(numBlocks);


        IBlockState blockState;
        Block block;
        BlockPos checkPos;
        ReactorInteriorData data;

        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    checkPos = new BlockPos(x,y,z);

                    //don't check any blocks that are part of this assembly
                    if (homHead.getPos() == checkPos)
                        continue;
                    for (FleshData f : _flesh)
                        if (f.Flesh.getPos() == checkPos)
                            continue;

                    blockState =  world.getBlockState(checkPos);
                    block = blockState.getBlock();

                    if(block instanceof IFluidBlock) {

                        Fluid fluid = ((IFluidBlock) block).getFluid();
                        String fluidName = fluid.getName();

                        data = ReactorInterior.getFluidData(fluidName);
                    }
                    else {
                        ItemStack stack = ItemHelper.createItemStack(blockState, 1);

                        if (null == stack) {
                            ModEventLog.error("Got null ItemStack for blockstate %s", blockState);
                            return false;
                        }

                        data = ReactorInterior.getBlockData(stack);
                    }

                    if(data == null) { //probably a casing, etc
                        absorptions.add(.1f);
                        burnRates.add(.1f);
                        nutritions.add(0);
                    } else {
                        absorptions.add(data.absorption);
                        burnRates.add(data.burnRate);
                        nutritions.add(data.nutrition);
                    }
                }
            }
        }

        this.absorption = absorptions.average();
        this.burnRate = burnRates.average();
        this.nutrition = (float)Math.floor(nutritions.average());

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
