package erogenousbeef.bigreactors.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.network.PacketDispatcher;
import erogenousbeef.bigreactors.common.BigReactors;
import erogenousbeef.bigreactors.common.multiblock.MultiblockReactor;
import erogenousbeef.bigreactors.common.multiblock.MultiblockReactor.WasteEjectionSetting;
import erogenousbeef.bigreactors.common.multiblock.tileentity.TileEntityReactorPart;
import erogenousbeef.bigreactors.gui.controls.BeefGuiFuelMixBar;
import erogenousbeef.bigreactors.gui.controls.BeefGuiHeatBar;
import erogenousbeef.bigreactors.gui.controls.BeefGuiLabel;
import erogenousbeef.bigreactors.gui.controls.BeefGuiPowerBar;
import erogenousbeef.bigreactors.gui.controls.GuiIconButton;
import erogenousbeef.bigreactors.net.PacketWrapper;
import erogenousbeef.bigreactors.net.Packets;
import erogenousbeef.bigreactors.utils.FloatAverager;
import erogenousbeef.core.common.CoordTriplet;

public class GuiReactorStatus extends BeefGuiBase {

	private GuiButton _reactorWastePolicy;
	private GuiButton _ejectWaste;
	
	private TileEntityReactorPart part;
	private MultiblockReactor reactor;
	
	private BeefGuiLabel titleString;
	private BeefGuiLabel statusString;
	private BeefGuiLabel heatString;
	private BeefGuiLabel fuelRodsString;
	private BeefGuiLabel energyGeneratedString;
	private BeefGuiLabel fuelConsumedString;
	private BeefGuiLabel fuelHeatString;
	private BeefGuiLabel fuelFertilityString;

	private BeefGuiPowerBar powerBar;
	private BeefGuiHeatBar heatBar;
	private BeefGuiFuelMixBar fuelMixBar;
	
	private FloatAverager averagedHeat;
	private FloatAverager averagedRfOutput;
	private FloatAverager averagedFuelConsumption;
	
	private GuiIconButton onButton;
	private GuiIconButton offButton;
	
	public GuiReactorStatus(Container container, TileEntityReactorPart tileEntityReactorPart) {
		super(container);
		
		ySize = 186;
		
		this.part = tileEntityReactorPart;
		this.reactor = part.getReactorController();
		
		this.averagedHeat = new FloatAverager(30);
		this.averagedRfOutput = new FloatAverager(30); // About 1.5 seconds
		this.averagedFuelConsumption = new FloatAverager(30);
	}
	
	// Add controls, etc.
	@Override
	public void initGui() {
		super.initGui();
		
		int xCenter = guiLeft + this.xSize / 2;
		int yCenter = this.ySize / 2;
		
		_reactorWastePolicy = new GuiButton(2, xCenter - (this.xSize/2) + 4, yCenter + (this.height / 2) - 46, 114, 20, getReactorWastePolicyText(reactor.getWasteEjection()));
		_ejectWaste = new GuiButton(3, xCenter - (this.xSize/2) + 122, yCenter + (this.height / 2) - 46, 50, 20, "Eject");

		this.buttonList.add(_reactorWastePolicy);
		this.buttonList.add(_ejectWaste);

		int leftX = guiLeft + 4;
		int topY = guiTop + 4;
		
		titleString = new BeefGuiLabel(this, "Reactor Control", leftX, topY);
		topY += titleString.getHeight() + 4;
		
		statusString = new BeefGuiLabel(this, "Status: -- updating --", leftX, topY);
		topY += statusString.getHeight() + 4;
		
		heatString = new BeefGuiLabel(this, "Heat: -- updating --", leftX, topY);
		topY += heatString.getHeight() + 4;
		
		fuelRodsString = new BeefGuiLabel(this, "Fuel Rods: -- updating --", leftX, topY);
		topY += fuelRodsString.getHeight() + 4;

		// Now move topY down so we're below the various status bars
		topY = guiTop + 88;
		energyGeneratedString = new BeefGuiLabel(this, "Power Output: -- updating --", leftX, topY);
		topY += energyGeneratedString.getHeight() + 4;
		
		fuelConsumedString = new BeefGuiLabel(this, "Fuel Consumed: -- updating --", leftX, topY);
		topY += fuelConsumedString.getHeight() + 4;
		
		fuelHeatString = new BeefGuiLabel(this, "Fuel Heat: -- updating --", leftX, topY);
		topY += fuelHeatString.getHeight() + 4;
		
		fuelFertilityString = new BeefGuiLabel(this, "Fertility: -- updating --", leftX, topY);
		topY += fuelFertilityString.getHeight() + 4;
		
		powerBar = new BeefGuiPowerBar(this, guiLeft + 152, guiTop + 22, this.reactor);
		heatBar = new BeefGuiHeatBar(this, guiLeft + 130, guiTop + 22, this.part);
		fuelMixBar = new BeefGuiFuelMixBar(this, guiLeft + 108, guiTop + 22, this.reactor);

		// TODO: onButton = new GuiIconButton(0, guiLeft + 4, guiTop + 160, 18, 18,  )
		
		registerControl(titleString);
		registerControl(statusString);
		registerControl(heatString);
		registerControl(fuelRodsString);
		registerControl(energyGeneratedString);
		registerControl(fuelConsumedString);
		registerControl(fuelHeatString);
		registerControl(fuelFertilityString);
		registerControl(powerBar);
		registerControl(heatBar);
		registerControl(fuelMixBar);
		
		averagedHeat.setAll(reactor.getReactorHeat());
		averagedRfOutput.setAll(reactor.getEnergyGeneratedLastTick());
		averagedFuelConsumption.setAll(reactor.getFuelConsumedLastTick());
	}

	private String getReactorWastePolicyText(WasteEjectionSetting setting) {
		switch(setting) {
		case kAutomatic:
			return "Waste: Auto-Eject";
		case kAutomaticOnlyIfCanReplace:
			return "Waste: Auto-Replace";
		case kManual:
		default:
			return "Waste: Manual";
		}
	}

	@Override
	public ResourceLocation getGuiBackground() {
		return new ResourceLocation(BigReactors.GUI_DIRECTORY + "ReactorController.png");
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		
		if(reactor.isActive()) {
			statusString.setLabelText("Status: Online");
		}
		else {
			statusString.setLabelText("Status: Offline");
		}
		
		fuelRodsString.setLabelText("Fuel Rods: " + Integer.toString(reactor.getFuelColumnCount()));
		
		MultiblockReactor.WasteEjectionSetting wasteSetting = reactor.getWasteEjection();
		_reactorWastePolicy.displayString = getReactorWastePolicyText(wasteSetting);
		
		_ejectWaste.enabled = (wasteSetting == MultiblockReactor.WasteEjectionSetting.kManual);
		
		// Grab averaged values
		averagedRfOutput.add(reactor.getEnergyGeneratedLastTick());
		averagedHeat.add(reactor.getReactorHeat());
		averagedFuelConsumption.add(reactor.getFuelConsumedLastTick());
		
		float averagedOutput = averagedRfOutput.average();
		if(averagedOutput >= 100f) {
			energyGeneratedString.setLabelText(String.format("Power Output: %1.0f RF/t", averagedRfOutput.average()));			
		}
		else {
			energyGeneratedString.setLabelText(String.format("Power Output: %1.1f RF/t", averagedRfOutput.average()));			
		}

		heatString.setLabelText("Heat: " + Integer.toString((int)averagedHeat.average()) + " C");
		
		float averagedConsumption = averagedFuelConsumption.average();
		if(averagedConsumption < 0.1f) {
			fuelConsumedString.setLabelText(String.format("Fuel Usage: %1.3f mB/t", averagedFuelConsumption.average()));
		}
		else if(averagedConsumption < 1f) {
			fuelConsumedString.setLabelText(String.format("Fuel Usage: %1.2f mB/t", averagedFuelConsumption.average()));
		}
		else if(averagedConsumption < 10f) {
			fuelConsumedString.setLabelText(String.format("Fuel Usage: %1.1f mB/t", averagedFuelConsumption.average()));
		}
		else {
			fuelConsumedString.setLabelText(String.format("Fuel Usage: %1.0f mB/t", averagedFuelConsumption.average()));
		}
		
		fuelHeatString.setLabelText(String.format("Fuel Heat: %1.1f C", reactor.getFuelHeat()));
		fuelFertilityString.setLabelText(String.format("Fuel Fertility: %2.0f%%", reactor.getFuelFertility() * 100f));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		CoordTriplet saveDelegate = reactor.getReferenceCoord();
		if(button.id == 1) {
			boolean newValue = !reactor.isActive();
			PacketDispatcher.sendPacketToServer(PacketWrapper.createPacket(BigReactors.CHANNEL, Packets.MultiblockControllerButton,
						new Object[] { saveDelegate.x, saveDelegate.y, saveDelegate.z, "activate", newValue }));
		}
		else if(button.id == 2) {
			PacketDispatcher.sendPacketToServer(PacketWrapper.createPacket(BigReactors.CHANNEL, Packets.ReactorWasteEjectionSettingUpdate, 
						new Object[] { saveDelegate.x, saveDelegate.y, saveDelegate.z } ));
		}
		else if(button.id == 3) {
			// Boolean value is ignored here.
			PacketDispatcher.sendPacketToServer(PacketWrapper.createPacket(BigReactors.CHANNEL, Packets.MultiblockControllerButton,
						new Object[] { saveDelegate.x, saveDelegate.y, saveDelegate.z, "ejectWaste", false }));
		}
	}
}
