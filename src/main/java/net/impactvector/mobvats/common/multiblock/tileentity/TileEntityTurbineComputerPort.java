package net.impactvector.mobvats.common.multiblock.tileentity;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.impactvector.mobvats.common.ModEventLog;
import net.impactvector.mobvats.common.multiblock.IInputOutputPort;
import net.impactvector.mobvats.common.multiblock.MultiblockTurbine;
import net.impactvector.mobvats.common.multiblock.MultiblockTurbine.VentStatus;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.Optional;

import java.util.HashMap;
import java.util.Map;

@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers"),
	@Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "OpenComputers"),
	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "ComputerCraft")
})
public class TileEntityTurbineComputerPort extends
		TileEntityTurbinePart implements IPeripheral, SimpleComponent, ManagedPeripheral {

	public enum ComputerMethod {
		getConnected,			// No arguments
		getActive,				// No arguments
		getEnergyStored, 		// No arguments
		getRotorSpeed,			// No arguments
		getInputAmount,  		// No arguments
		getInputType,			// No arguments
		getOutputAmount, 		// No arguments
		getOutputType,			// No arguments
		getFluidAmountMax,		// No arguments
		getFluidFlowRate,		// No arguments
		getFluidFlowRateMax,	// No arguments
		getFluidFlowRateMaxMax, // No arguments
		getEnergyProducedLastTick, // No arguments
		getNumberOfBlades,		// No arguments
		getBladeEfficiency,		// No arguments
		getRotorMass,			// No arguments
		getInductorEngaged,		// No arguments
		getMaximumCoordinate,	// No arguments
		getMinimumCoordinate,	// No arguments
		setActive,				// Required Arg: integer (active)
		setFluidFlowRateMax,	// Required Arg: integer (active)
		setVentNone,			// No arguments
		setVentOverflow,		// No arguments
		setVentAll,				// No arguments
		setInductorEngaged,		// Required Arg: integer (active)
	}

	public static final int numMethods = ComputerMethod.values().length;

	public static final String[] methodNames = new String[numMethods];
	static {
		ComputerMethod[] methods = ComputerMethod.values();
		for(ComputerMethod method : methods) {
			methodNames[method.ordinal()] = method.toString();
		}
	}

	public static final Map<String, Integer> methodIds = new HashMap<String, Integer>();
	static {
		for (int i = 0; i < numMethods; ++i) {
			methodIds.put(methodNames[i], i);
		}
	}

	public Object[] callMethod(int method, Object[] arguments) throws Exception {
		if(method < 0 || method >= numMethods) {
			throw new IllegalArgumentException("Invalid method number");
		}
		
		// Special case: getConnected can always be called.
		if(method == 0) {
			return new Object[] { this.isConnected() };
		}

		if(!this.isConnected()) {
			throw new Exception("Unable to access turbine - port is not connected");
		}

		ComputerMethod computerMethod = ComputerMethod.values()[method];
		MultiblockTurbine turbine = getTurbine();
		FluidTankInfo ti;

		switch(computerMethod) {
		case getConnected:
			return new Object[] { isConnected() };
		case getActive:
			return new Object[] { turbine.getActive() };
		case getEnergyProducedLastTick:
			return new Object[] { turbine.getEnergyGeneratedLastTick() };
		case getEnergyStored:
			return new Object[] { turbine.getEnergyStored() };
		case getFluidAmountMax:
			return new Object[] { MultiblockTurbine.TANK_SIZE };
		case getFluidFlowRate:
			return new Object[] { turbine.getFluidConsumedLastTick() };
		case getFluidFlowRateMax:
			return new Object[] { turbine.getMaxIntakeRate() };
		case getFluidFlowRateMaxMax:
			return new Object[] { turbine.getMaxIntakeRateMax() };
		case getInputAmount:
			return new Object[] { this.getFluidAmount(IInputOutputPort.Direction.Input) };
		case getInputType:
			return new Object[] { this.getFluidName(IInputOutputPort.Direction.Input) };
		case getOutputAmount:
			return new Object[] { this.getFluidAmount(IInputOutputPort.Direction.Output) };
		case getOutputType:
			return new Object[] { this.getFluidName(IInputOutputPort.Direction.Output) };
		case getRotorSpeed:
			return new Object[] { turbine.getRotorSpeed() };
		case getNumberOfBlades:
			return new Object[] { turbine.getNumRotorBlades() };
		case getBladeEfficiency:
			return new Object[] { turbine.getRotorEfficiencyLastTick() * 100f };
		case getRotorMass:
			return new Object[] { turbine.getRotorMass() };
		case getInductorEngaged:
			return new Object[] { turbine.getInductorEngaged() };
		case getMinimumCoordinate:
		{
			BlockPos coord = turbine.getMinimumCoord();
			return new Object[] { coord.getX(), coord.getY(), coord.getZ() };
		}
			
		case getMaximumCoordinate:
		{
			BlockPos coord = turbine.getMaximumCoord();
			return new Object[] { coord.getX(), coord.getY(), coord.getZ() };
		}

		case setActive:
			if(arguments.length < 1) {
				throw new IllegalArgumentException("Insufficient number of arguments, expected 1");
			}
			if(!(arguments[0] instanceof Boolean)) {
				throw new IllegalArgumentException("Invalid argument 0, expected Boolean");
			}
			turbine.setActive((Boolean)arguments[0]);
			break;
		case setFluidFlowRateMax:
			if(arguments.length < 1) {
				throw new IllegalArgumentException("Insufficient number of arguments, expected 1");
			}
			if(!(arguments[0] instanceof Double)) {
				throw new IllegalArgumentException("Invalid argument 0, expected Number");
			}
			int newRate = (int)Math.round((Double)arguments[0]);
			turbine.setMaxIntakeRate(newRate);
			break;
		case setVentNone:
			turbine.setVentStatus(VentStatus.DoNotVent, true);
			break;
		case setVentOverflow:
			turbine.setVentStatus(VentStatus.VentOverflow, true);
			break;
		case setVentAll:
			turbine.setVentStatus(VentStatus.VentAll, true);
			break;
		case setInductorEngaged:
			if(arguments.length < 1) {
				throw new IllegalArgumentException("Insufficient number of arguments, expected 1");
			}
			if(!(arguments[0] instanceof Boolean)) {
				throw new IllegalArgumentException("Invalid argument 0, expected Boolean");
			}
			turbine.setInductorEngaged((Boolean)arguments[0], true);
			break;
		default:
			throw new Exception("Method unimplemented - yell at Beef");
		}
		
		return null;
	}

	private IFluidTankProperties getTankProperties(IInputOutputPort.Direction direction) {

		MultiblockTurbine turbine = this.getTurbine();
		IFluidHandler handler = null != turbine ? turbine.getFluidHandler(direction) : null;
		IFluidTankProperties[] properties = null != handler ? handler.getTankProperties() : null;

		return null != properties && properties.length > 0 ? properties[0] : null;
	}

	private int getFluidAmount(IInputOutputPort.Direction direction) {

		IFluidTankProperties properties = this.getTankProperties(direction);
		FluidStack stack = null != properties ? properties.getContents() : null;

		return null != stack ? stack.amount : 0;
	}

	private String getFluidName(IInputOutputPort.Direction direction) {

		IFluidTankProperties properties = this.getTankProperties(direction);
		FluidStack stack = null != properties ? properties.getContents() : null;
		Fluid fluid = null != stack ? stack.getFluid() : null;

		return null != fluid ? fluid.getName() : null;
	}
	
	// ComputerCraft
	
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String getType() {
		return "MobVats-Turbine";
	}
	
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String[] getMethodNames() {
		return methodNames;
	}
	
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) {
        try {
            return callMethod(method, arguments);
        } catch(Exception e) {
        	ModEventLog.info("Exception encountered when invoking computercraft method: %s", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
	
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void attach(IComputerAccess computer) {
	}
	
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void detach(IComputerAccess computer) {
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "OpenComputers")
	public String getComponentName() {
		// Convention for OC names is a) lower case, b) valid variable names,
		// so this can be used as `component.br_turbine.setActive(true)` e.g.
		return "br_turbine";
	}
	
	@Override
	@Optional.Method(modid = "OpenComputers")
	public String[] methods() {
		return methodNames;
	}
	
	@Override
	@Optional.Method(modid = "OpenComputers")
	public Object[] invoke(final String method, final Context context,
						   final Arguments args) throws Exception {
		final Object[] arguments = new Object[args.count()];
		for (int i = 0; i < args.count(); ++i) {
			arguments[i] = args.checkAny(i);
		}
		final Integer methodId = methodIds.get(method);
		if (methodId == null) {
			throw new NoSuchMethodError();
		}
		return callMethod(methodId, arguments);
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public boolean equals(IPeripheral other) {
		return hashCode() == other.hashCode();
	}
}