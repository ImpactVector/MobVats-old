package erogenousbeef.bigreactors.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import erogenousbeef.bigreactors.common.BigReactors;
import erogenousbeef.bigreactors.common.block.BlockReactorPart;
import erogenousbeef.bigreactors.common.tileentity.TileEntityReactorAccessPort;
import erogenousbeef.bigreactors.net.PacketWrapper;
import erogenousbeef.bigreactors.net.Packets;
import erogenousbeef.core.common.CoordTriplet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;

public class GuiReactorAccessPort extends BeefGuiBase {

	private GuiButton _togglePort;
	private TileEntityReactorAccessPort _port;
	
	public GuiReactorAccessPort(Container container, TileEntityReactorAccessPort accessPort) {
		super(container);
		
		_port = accessPort;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int xCenter = guiLeft + this.xSize / 2;
		int yCenter = this.ySize / 2;

		int metadata = _port.worldObj.getBlockMetadata(_port.xCoord, _port.yCoord, _port.zCoord);
		
		_togglePort = new GuiButton(1, xCenter + 31, guiTop + 4, 50, 20, getStringFromMetadata(metadata));
		buttonList.add(_togglePort);
	}

	@Override
	protected String getGuiBackground() {
		return BigReactors.GUI_DIRECTORY + "ReactorAccessPort.png";
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		
		int metadata = _port.worldObj.getBlockMetadata(_port.xCoord, _port.yCoord, _port.zCoord);
		_togglePort.displayString = getStringFromMetadata(metadata);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float gameTicks) {
		super.drawScreen(mouseX, mouseY, gameTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == 1) {
			int metadata = _port.worldObj.getBlockMetadata(_port.xCoord, _port.yCoord, _port.zCoord);
			byte newMetadata = BlockReactorPart.ACCESSPORT_INLET;
			if(metadata == BlockReactorPart.ACCESSPORT_INLET) {
				newMetadata = BlockReactorPart.ACCESSPORT_OUTLET;
			}
			
			PacketDispatcher.sendPacketToServer(PacketWrapper.createPacket(BigReactors.CHANNEL, Packets.AccessPortButton,
						new Object[] { _port.xCoord, _port.yCoord, _port.zCoord, newMetadata }));
		}
	}
	
	private String getStringFromMetadata(int metadata) {
		if(metadata == BlockReactorPart.ACCESSPORT_INLET) {
			return "Dir: IN";
		}
		else {
			return "Dir: OUT";
		}
	}
}
