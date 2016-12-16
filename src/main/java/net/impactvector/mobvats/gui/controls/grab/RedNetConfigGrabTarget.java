// TODO Removing support for ComputerCraft and MineFactory Reloaded until they are updated to 1.9.x
/*
package net.impactvector.mobvats.gui.controls.grab;

import net.impactvector.mobvats.client.gui.BeefGuiBase;
import net.impactvector.mobvats.client.gui.GuiReactorRedNetPort;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorRedNetPort;
import net.impactvector.mobvats.gui.IBeefTooltipControl;

public class RedNetConfigGrabTarget extends BeefGuiGrabTarget implements IBeefTooltipControl {

	TileEntityReactorRedNetPort port;
	int channel;
	String tooltip;
	TileEntityReactorRedNetPort.CircuitType currentCircuitType;
	
	public RedNetConfigGrabTarget(BeefGuiBase container, int x, int y, TileEntityReactorRedNetPort port, int channel) {
		super(container, x, y);
		this.port = port;
		this.channel = channel;
		this.tooltip = null;
		currentCircuitType = port.getChannelCircuitType(channel);
	}
	
	@Override
	public void onSlotCleared() {
		currentCircuitType = TileEntityReactorRedNetPort.CircuitType.DISABLED;
		tooltip = null;
		
		if(guiContainer instanceof GuiReactorRedNetPort) {
			((GuiReactorRedNetPort)guiContainer).onChannelChanged(this.channel);
		}
	}

	@Override
	public void onSlotSet() {
		currentCircuitType = ((RedNetConfigGrabbable)this.grabbable).getCircuitType();
		tooltip = ((RedNetConfigGrabbable)this.grabbable).getName();
		
		if(guiContainer instanceof GuiReactorRedNetPort) {
			((GuiReactorRedNetPort)guiContainer).onChannelChanged(this.channel);
		}		
	}

	@Override
	public boolean isAcceptedGrab(IBeefGuiGrabbable grabbedItem) {
		return grabbedItem instanceof RedNetConfigGrabbable;
	}
	
	public boolean hasChanged() {
		return currentCircuitType != this.port.getChannelCircuitType(channel);
	}
	
	public int getChannel() { return channel; }
	public TileEntityReactorRedNetPort.CircuitType getCircuitType() { return this.currentCircuitType; }

	@Override
	public String[] getTooltip() {
		return new String[] { tooltip };
	}
}
*/