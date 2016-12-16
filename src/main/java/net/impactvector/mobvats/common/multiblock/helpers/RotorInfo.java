package net.impactvector.mobvats.common.multiblock.helpers;

import net.impactvector.mobvats.common.multiblock.RotorBladeState;
import net.impactvector.mobvats.common.multiblock.RotorShaftState;
import net.minecraft.util.EnumFacing;

public class RotorInfo {
	// Location of bearing
	public int x, y, z;
	
	// Rotor direction
	public EnumFacing rotorDirection;
	
	// Rotor length
	public int rotorLength = 0;
	
	// Array of arrays, containing rotor lengths
	public int[][] bladeLengths = null;

	public RotorShaftState[] shaftStates = null;
	public RotorBladeState[][] bladeStates = null;
}
