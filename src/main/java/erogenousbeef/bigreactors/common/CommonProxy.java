package erogenousbeef.bigreactors.common;

import java.util.Calendar;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import erogenousbeef.bigreactors.common.data.ReactorSolidMapping;
import erogenousbeef.bigreactors.common.item.ItemIngot;
import erogenousbeef.bigreactors.gui.BigReactorsGUIHandler;
import erogenousbeef.bigreactors.net.CommonPacketHandler;
import erogenousbeef.bigreactors.utils.IMCHelper;
import erogenousbeef.bigreactors.utils.intermod.ModHelperBase;
import erogenousbeef.bigreactors.utils.intermod.ModHelperComputerCraft;
import erogenousbeef.bigreactors.utils.intermod.ModHelperMekanism;
import erogenousbeef.core.multiblock.MultiblockServerTickHandler;

public class CommonProxy {

	public void preInit() {
	}

	public void init() {
		BigReactors.registerTileEntities();
		
		CommonPacketHandler.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(BRLoader.instance, new BigReactorsGUIHandler());
		BigReactors.tickHandler = new BigReactorsTickHandler();
		FMLCommonHandler.instance().bus().register(BigReactors.tickHandler);
        FMLCommonHandler.instance().bus().register(new MultiblockServerTickHandler());
		
		sendInterModAPIMessages();
	}

	private void sendInterModAPIMessages() {
		ItemIngot ingotGeneric = BigReactors.ingotGeneric;
		
		ItemStack yelloriteOre 	= new ItemStack(BigReactors.blockYelloriteOre, 1);
		ItemStack ingotYellorium= ingotGeneric.getItemStackForType("ingotYellorium");
		ItemStack ingotCyanite 	= ingotGeneric.getItemStackForType("ingotCyanite");
		ItemStack ingotGraphite = ingotGeneric.getItemStackForType("ingotGraphite");
		ItemStack ingotBlutonium= ingotGeneric.getItemStackForType("ingotBlutonium");
		ItemStack dustYellorium = ingotGeneric.getItemStackForType("dustYellorium");
		ItemStack dustCyanite 	= ingotGeneric.getItemStackForType("dustCyanite");
		ItemStack dustGraphite 	= ingotGeneric.getItemStackForType("dustGraphite");
		ItemStack dustBlutonium = ingotGeneric.getItemStackForType("dustBlutonium");

		ItemStack doubledYelloriumDust = null;
		if(dustYellorium != null) {
			doubledYelloriumDust = dustYellorium.copy();
			doubledYelloriumDust.stackSize = 2;
		}

		if(Loader.isModLoaded("ThermalExpansion")) {
			ItemStack sandStack = new ItemStack(Blocks.sand, 1);
			ItemStack doubleYellorium = ingotYellorium.copy();
			doubleYellorium.stackSize = 2;

			if(yelloriteOre != null && ingotYellorium != null) {
				IMCHelper.ThermalExpansion.addInductionSmelterRecipe(yelloriteOre, sandStack, doubleYellorium, 1600);
			}
			
			if(yelloriteOre != null && doubledYelloriumDust != null) {
				IMCHelper.ThermalExpansion.addPulverizerRecipe(yelloriteOre, doubledYelloriumDust, 4000);
			}
			
			if(ingotYellorium != null && dustYellorium != null) {
				IMCHelper.ThermalExpansion.addPulverizerRecipe(ingotYellorium, dustYellorium, 2400);
				IMCHelper.ThermalExpansion.addInductionSmelterRecipe(doubledYelloriumDust, sandStack, doubleYellorium, 200);
			}

			if(ingotCyanite != null && dustCyanite != null) {
				IMCHelper.ThermalExpansion.addPulverizerRecipe(ingotCyanite, dustCyanite, 2400);
				
				ItemStack doubleDust = dustCyanite.copy();
				doubleDust.stackSize = 2;
				ItemStack doubleIngot = ingotCyanite.copy();
				doubleIngot.stackSize = 2;
				IMCHelper.ThermalExpansion.addInductionSmelterRecipe(doubleDust, sandStack, doubleIngot, 200);
			}

			if(ingotGraphite != null && dustGraphite != null) {
				IMCHelper.ThermalExpansion.addPulverizerRecipe(ingotGraphite, dustGraphite, 2400);

				ItemStack doubleDust = dustGraphite.copy();
				doubleDust.stackSize = 2;
				ItemStack doubleIngot = ingotGraphite.copy();
				doubleIngot.stackSize = 2;
				IMCHelper.ThermalExpansion.addInductionSmelterRecipe(doubleDust, sandStack, doubleIngot, 200);
			}

			if(ingotBlutonium != null && dustBlutonium != null) {
				IMCHelper.ThermalExpansion.addPulverizerRecipe(ingotBlutonium, dustBlutonium, 2400);

				ItemStack doubleDust = dustBlutonium.copy();
				doubleDust.stackSize = 2;
				ItemStack doubleIngot = ingotBlutonium.copy();
				doubleIngot.stackSize = 2;
				IMCHelper.ThermalExpansion.addInductionSmelterRecipe(doubleDust, sandStack, doubleIngot, 200);
			}
		} // END: IsModLoaded - ThermalExpansion
		
		if(Loader.isModLoaded("MineFactoryReloaded")) {
			// Add yellorite to yellow focus list.
			IMCHelper.MFR.addOreToMiningLaserFocus(yelloriteOre, 2);
            
            // Make Yellorite the 'preferred' ore for lime focus
            IMCHelper.MFR.setMiningLaserFocusPreferredOre(yelloriteOre, 9);
		} // END: IsModLoaded - MineFactoryReloaded
		
		if(Loader.isModLoaded("appliedenergistics2")) {
			if(yelloriteOre != null && dustYellorium != null) {
				IMCHelper.AE2.addGrinderRecipe(yelloriteOre, doubledYelloriumDust, 4);
			}
		
			if(ingotYellorium != null && dustYellorium != null) {
				IMCHelper.AE2.addGrinderRecipe(ingotYellorium, dustYellorium, 2);
			}

			if(ingotCyanite != null && dustCyanite != null) {
				IMCHelper.AE2.addGrinderRecipe(ingotCyanite, dustCyanite, 2);
			}

			if(ingotGraphite != null && dustGraphite != null) {
				IMCHelper.AE2.addGrinderRecipe(ingotGraphite, dustGraphite, 2);
			}

			if(ingotBlutonium != null && dustBlutonium != null) {
				IMCHelper.AE2.addGrinderRecipe(ingotBlutonium, dustBlutonium, 2);
			}
		} // END: IsModLoaded - AE2
	}

	public void postInit() {
		BRConfig.CONFIGURATION.load();
		boolean autoAddUranium = BRConfig.CONFIGURATION.get("Compatibility", "autoAddUranium",
															true,
															"If true, automatically adds all "
															+"unregistered ingots found as clones"
															+"of standard yellorium fuel").getBoolean(true);
		if(autoAddUranium) {
			List<ItemStack> candidates = OreDictionary.getOres("ingotUranium");
			for(ItemStack candidate : candidates) {
				// If they're already registered, this will NOT overwrite the existing registration
				BRRegistry.registerReactorSolidToFuelMapping(new ReactorSolidMapping(candidate, new FluidStack(BigReactors.fluidYellorium, 1000)));
			}
		}

		BRConfig.CONFIGURATION.save();
		
		registerWithOtherMods();
		
		// Easter Egg - Check if today is valentine's day. If so, change all particles to hearts.
		Calendar calendar = Calendar.getInstance();
		BigReactors.isValentinesDay = (calendar.get(Calendar.MONTH) == 2 && calendar.get(Calendar.DAY_OF_MONTH) == 14);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerIcons(TextureStitchEvent.Pre event) {
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void setIcons(TextureStitchEvent.Post event) {
	}
	
	/// Mod Interoperability ///
	void registerWithOtherMods() {
		ModHelperBase modHelper;
		
		modHelper = new ModHelperComputerCraft();
		modHelper.register();
		
		modHelper = new ModHelperMekanism();
		modHelper.register();
	}
}