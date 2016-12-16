package net.impactvector.mobvats.common.multiblock.tileentity.creative;

import net.impactvector.mobvats.common.multiblock.MultiblockReactor;
//import net.impactvector.mobvats.common.multiblock.helpers.CoolantContainer;
import net.impactvector.mobvats.common.multiblock.interfaces.ITickableMultiblockPart;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorCoolantPort;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityReactorCreativeCoolantPort extends TileEntityReactorCoolantPort implements ITickableMultiblockPart {

	public TileEntityReactorCreativeCoolantPort() {
		super();
	}

	@Override
	public void onMultiblockServerTick() {
//		if(!isConnected()) { return; }
//
//		MultiblockReactor reactor = getReactorController();
//
//		if (this.getDirection().isInput()) {
//
//			CoolantContainer cc = reactor.getCoolantContainer();
//			if(cc.getCoolantAmount() < cc.getCapacity())
//			{
//				reactor.getCoolantContainer().addCoolant(new FluidStack(FluidRegistry.WATER, cc.getCapacity()));
//			}
//		}
//		else {
//			reactor.getCoolantContainer().emptyVapor();
//		}
	}

	public void forceAddWater() {
//		if(!isConnected()) { return; }
//
//		MultiblockReactor reactor = getReactorController();
//		reactor.getCoolantContainer().addCoolant(new FluidStack(FluidRegistry.WATER, 1000));
	}
}
