package net.impactvector.mobvats.utils.intermod;

import dan200.computercraft.api.ComputerCraftAPI;
import net.impactvector.mobvats.init.ModBlocks;
import net.minecraftforge.fml.common.Optional;

public class ModHelperComputerCraft extends ModHelperBase {

	@Optional.Method(modid = "ComputerCraft")
	@Override
	public void register() {

		ComputerCraftAPI.registerPeripheralProvider(ModBlocks.reactorComputerPort);
        // TODO add back when turbine computer port is ready
		//ComputerCraftAPI.registerPeripheralProvider(ModBlocks.blockTurbinePart);
	}
}