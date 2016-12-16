package net.impactvector.mobvats.client.gui;

import net.impactvector.mobvats.client.ClientProxy;
import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityReactorControlRod;
import net.impactvector.mobvats.gui.controls.BeefGuiIcon;
import net.impactvector.mobvats.gui.controls.BeefGuiInsertionProgressBar;
import net.impactvector.mobvats.gui.controls.BeefGuiLabel;
import net.impactvector.mobvats.gui.controls.GuiIconButton;
import net.impactvector.mobvats.net.CommonPacketHandler;
import net.impactvector.mobvats.net.message.ControlRodChangeInsertionMessage;
import net.impactvector.mobvats.net.message.ControlRodChangeNameMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class GuiReactorControlRod extends BeefGuiBase {

	TileEntityReactorControlRod entity;
	
	BeefGuiLabel titleString;
	BeefGuiLabel rodNameLabel;
	BeefGuiLabel insertionLabel;

	GuiButton setNameBtn;
	
	BeefGuiIcon rodInsertIcon;
	GuiIconButton rodInsertBtn;
	GuiIconButton rodRetractBtn;

	BeefGuiInsertionProgressBar insertionBar;
	
    private GuiTextField rodName;
	private static ResourceLocation s_backGround;
	
	public GuiReactorControlRod(Container c, TileEntityReactorControlRod controlRod) {
		super(c);
		
		entity = controlRod;
	}

	@Override
	public ResourceLocation getGuiBackground() {

		if (null == GuiReactorControlRod.s_backGround)
			GuiReactorControlRod.s_backGround = MobVats.createResourceLocation("textures/gui/BasicBackground.png");

		return GuiReactorControlRod.s_backGround;
	}

	@Override
	public void initGui() {
		super.initGui();

		int leftX = guiLeft + 4;
		int topY = guiTop + 4;
		
		Keyboard.enableRepeatEvents(true);
		
		titleString = new BeefGuiLabel(this, "Reactor Control Rod", leftX, topY);
		topY += titleString.getHeight() + 8;
		
		rodNameLabel = new BeefGuiLabel(this, "Name:", leftX, topY + 6);
		
		rodName = new GuiTextField(1, fontRendererObj, leftX + 4 + rodNameLabel.getWidth(), topY, 100, 20);
		rodName.setCanLoseFocus(true);
		rodName.setMaxStringLength(32);
		rodName.setText(entity.getName());
		rodName.setEnabled(true);
		
		setNameBtn = new GuiButton(2, guiLeft + 140, topY, 30, 20, "Set");
		setNameBtn.enabled = false;
		topY += 28;
		
		rodInsertIcon = new BeefGuiIcon(this, leftX+42, topY, 16, 16, ClientProxy.GuiIcons.getIcon("controlRod"), new String[] { TextFormatting.AQUA + "Rod Insertion", "", "Change the control rod's insertion.", "Higher insertion slows reaction rate.", "", "Lower reaction rates reduce heat,", "energy, radiation output, and", "fuel consumption." });
		insertionLabel = new BeefGuiLabel(this, "", leftX+62, topY+5);
		topY += 20;
		insertionBar = new BeefGuiInsertionProgressBar(this, leftX+40, topY);

		topY += 12;
		rodRetractBtn = new GuiIconButton(0, leftX+70, topY, 20, 20, ClientProxy.GuiIcons.getIcon("upArrow"), new String[] { TextFormatting.AQUA + "Insert Rod", "Increase insertion by 10.", "", "Shift: +100", "Alt: +5", "Shift+Alt: +1", "", "Ctrl: Change ALL Rods" });
		topY += 20;
		rodInsertBtn = new GuiIconButton(1, leftX+70, topY, 20, 20, ClientProxy.GuiIcons.getIcon("downArrow"), new String[] { TextFormatting.AQUA + "Retract Rod", "Reduce insertion by 10.", "", "Shift: -100", "Alt: -5", "Shift+Alt: -1", "", "Ctrl: Change ALL Rods" });
		topY += 32;

		registerControl(insertionBar);
		registerControl(titleString);
		registerControl(rodNameLabel);
		registerControl(rodInsertIcon);
		registerControl(insertionLabel);

		registerControl(rodName);
		registerControl(rodRetractBtn);
		registerControl(rodInsertBtn);
		registerControl(setNameBtn);
		
		updateStrings();
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		updateStrings();
	}
	
	protected void updateStrings() {
		if(entity.isConnected()) {
			rodInsertBtn.enabled = true;
			rodRetractBtn.enabled = true;
		}
		else {
			rodInsertBtn.enabled = false;
			rodRetractBtn.enabled = false;
		}
		
		insertionLabel.setLabelText(String.format("%d%%", entity.getControlRodInsertion()));
		this.setNameBtn.enabled = !entity.getName().equals(this.rodName.getText());
		insertionBar.setInsertion((float)entity.getControlRodInsertion() / 100f);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch(button.id) {
		case 2:
            CommonPacketHandler.INSTANCE.sendToServer(new ControlRodChangeNameMessage(entity.getPos(), this.rodName.getText()));
			this.rodName.setFocused(false);
			break;
		case 0:
		case 1:
		default:
			int change = 10;
			if(isShiftKeyDown()) {
				if(isAltKeyDown()) {
					change = 1;
				}
				else {
					change = 100;
				}
			}
			else if(isAltKeyDown()) {
				change = 5;
			}
			if(button.id == 1) { change = -change; }
	        CommonPacketHandler.INSTANCE.sendToServer(new ControlRodChangeInsertionMessage(entity.getPos(), change, isCtrlKeyDown()));
		}
    }
	
	@Override
	protected void keyTyped(char inputChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE ||
        		(!this.rodName.isFocused() && keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())) {
            this.mc.thePlayer.closeScreen();
        }

		this.rodName.textboxKeyTyped(inputChar, keyCode);
		
		if(keyCode == Keyboard.KEY_TAB) {
			// Tab
			if(this.rodName.isFocused()) {
				this.rodName.setFocused(false);
			}
			else {
				this.rodName.setFocused(true);
			}
		}
		
		if(keyCode == Keyboard.KEY_RETURN) {
			// Return/enter
			this.actionPerformed(this.buttonList.get(2));
		}
	}
	
}