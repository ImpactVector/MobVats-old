package net.impactvector.mobvats.client.gui;

import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.block.BlockBRDevice;
import net.impactvector.mobvats.common.tileentity.TileEntityCyaniteReprocessor;
import net.impactvector.mobvats.gui.controls.BeefGuiFluidBar;
import net.impactvector.mobvats.gui.controls.BeefGuiLabel;
import net.impactvector.mobvats.gui.controls.BeefGuiPowerBar;
import net.impactvector.mobvats.gui.controls.BeefGuiProgressArrow;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiCyaniteReprocessor extends BeefGuiDeviceBase {

	private GuiButton _togglePort;
	private TileEntityCyaniteReprocessor _entity;

	private BeefGuiLabel titleString;
	
	private BeefGuiPowerBar powerBar;
	private BeefGuiFluidBar fluidBar;
	private BeefGuiProgressArrow progressArrow;
	
	public GuiCyaniteReprocessor(Container container, TileEntityCyaniteReprocessor entity) {
		super(container, entity);
		
		_entity = entity;
		xSize = 245;
		ySize = 175;
	}
	
	@Override
	public void initGui() {
		super.initGui();

		// TODO Commented temporarily to allow this thing to compile...
		titleString = new BeefGuiLabel(this, "FIXME"/*_entity.getInventoryName()*/, guiLeft + 8, guiTop + 6);

		fluidBar = new BeefGuiFluidBar(this, guiLeft + 8, guiTop + 16, /*_entity , 0*/null /* TODO fix */);
		// TODO Commented temporarily to allow this thing to compile...
		//powerBar = new BeefGuiPowerBar(this, guiLeft + 148, guiTop + 16, _entity);
		progressArrow = new BeefGuiProgressArrow(this, guiLeft + 76, guiTop + 41, 0, 178, _entity);
		
		registerControl(titleString);
		registerControl(powerBar);
		registerControl(fluidBar);
		registerControl(progressArrow);

		createInventoryExposureButtons(guiLeft + 180, guiTop + 4);
	}

	@Override
	public ResourceLocation getGuiBackground() {

		if (null == GuiCyaniteReprocessor.s_guiTexture)
			GuiCyaniteReprocessor.s_guiTexture = MobVats.createGuiResourceLocation("CyaniteReprocessor.png");

		return GuiCyaniteReprocessor.s_guiTexture;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float gameTicks) {
		super.drawScreen(mouseX, mouseY, gameTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}

	@Override
	protected int getBlockMetadata() {
		return BlockBRDevice.META_CYANITE_REPROCESSOR;
	}

	private static ResourceLocation s_guiTexture;
}
