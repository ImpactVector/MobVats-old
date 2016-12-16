package net.impactvector.mobvats.gui.controls;

import net.impactvector.mobvats.client.gui.BeefGuiBase;
import net.impactvector.mobvats.common.tileentity.base.TileEntityPoweredInventory;
import net.impactvector.mobvats.gui.BeefGuiControlBase;
import net.minecraft.client.renderer.texture.TextureManager;

public class BeefGuiProgressArrow extends BeefGuiControlBase {

	TileEntityPoweredInventory entity;
	private int arrowU;
	private int arrowV;
	
	public BeefGuiProgressArrow(BeefGuiBase container, int x, int y, int arrowU, int arrowV, TileEntityPoweredInventory entity) {
		super(container, x, y, 25, 16);
		this.arrowU = arrowU;
		this.arrowV = arrowV;
		this.entity = entity;
	}

	@Override
	public void drawBackground(TextureManager renderEngine, int mouseX, int mouseY) {
		if(entity.getCycleCompletion() > 0.0) {
			int progressWidth = (int)(entity.getCycleCompletion() * (float)(this.width-1));
			renderEngine.bindTexture(this.guiContainer.getGuiBackground());
			guiContainer.drawTexturedModalRect(this.absoluteX, this.absoluteY, arrowU, arrowV, 1+progressWidth, this.height);
		}
	}

	@Override
	public void drawForeground(TextureManager renderEngine, int mouseX, int mouseY) {
	}

}
