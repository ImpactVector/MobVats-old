package net.impactvector.mobvats.common.multiblock.tileentity.creative;

import net.impactvector.mobvats.common.multiblock.IInputOutputPort;
import net.impactvector.mobvats.common.multiblock.MultiblockTurbine;
import net.impactvector.mobvats.common.multiblock.interfaces.ITickableMultiblockPart;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityTurbinePart;
import net.impactvector.mobvats.init.ModFluids;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityTurbineCreativeSteamGenerator extends TileEntityTurbinePart implements ITickableMultiblockPart {

	public TileEntityTurbineCreativeSteamGenerator() {
		super();
	}

	@Override
	public void onMultiblockServerTick() {

//		final MultiblockTurbine turbine = this.getTurbine();
//
//		if (null != turbine && turbine.getActive()) {
//
//			FluidStack steam = new FluidStack(ModFluids.fluidSteam, turbine.getMaxIntakeRate());
//
//			turbine.getFluidHandler(IInputOutputPort.Direction.Input).fill(steam, true);
//		}
	}
}
