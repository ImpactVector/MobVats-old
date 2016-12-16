package net.impactvector.mobvats.gui;


public interface IBeefTooltipControl {
	boolean isMouseOver(int mouseX, int mouseY);
	String[] getTooltip();
	
	public boolean isVisible();
}
