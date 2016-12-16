package net.impactvector.mobvats.common;

import net.impactvector.mobvats.api.registry.Reactants;
import net.impactvector.mobvats.common.block.BlockBR;
import net.impactvector.mobvats.common.block.BlockBRGenericFluid;
import net.impactvector.mobvats.common.data.StandardReactants;
import net.impactvector.mobvats.common.item.ItemBase;
import net.impactvector.mobvats.init.ModBlocks;
import net.impactvector.mobvats.init.ModItems;
import net.impactvector.mobvats.plugins.top.TOPCompat;
import net.impactvector.mobvats.plugins.waila.WailaCompatibility;
import net.impactvector.mobvats.utils.intermod.IMCHelper;
import net.impactvector.mobvats.utils.intermod.ModHelperBase;
import net.impactvector.mobvats.utils.intermod.ModHelperComputerCraft;
import net.impactvector.mobvats.utils.intermod.ModHelperMekanism;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy implements IModInitializationHandler {

	public BlockBR register(BlockBR block) {

		GameRegistry.register(block);
		block.setCreativeTab(MobVats.TAB);
		block.onPostRegister();
		return block;
	}

	public BlockBRGenericFluid register(BlockBRGenericFluid block) {

		GameRegistry.register(block);
		block.setCreativeTab(MobVats.TAB);
		block.onPostRegister();
		return block;
	}

	public ItemBase register(ItemBase item) {

		GameRegistry.register(item);
		item.setCreativeTab(MobVats.TAB);
		item.onPostRegister();
		return item;
	}

	public void register(Class<? extends TileEntity> tileEntityClass) {

		GameRegistry.registerTileEntity(tileEntityClass, MobVats.MODID + tileEntityClass.getSimpleName());
	}

	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded("theoneprobe"))
			TOPCompat.register();
        if (Loader.isModLoaded("Waila"))
            WailaCompatibility.register();
	}

	@Override
	public void onInit(FMLInitializationEvent event) {

		// Mods interaction

		sendInterModAPIMessages();

		/*
		if(Loader.isModLoaded("VersionChecker")) {
			FMLInterModComms.sendRuntimeMessage(BRLoader.MOD_ID, "VersionChecker", "addVersionCheck", "http://big-reactors.com/version.json");
		}*/
	}

	@Override
	public void onPostInit(FMLPostInitializationEvent event) {

		if (MobVats.CONFIG.autoAddUranium)
			Reactants.registerSolid("ingotUranium", StandardReactants.yellorium);

		this.registerWithOtherMods();
	}

	private void sendInterModAPIMessages() {

		ItemStack yelloriteOre = new ItemStack(ModBlocks.brOre, 1);

		MetalType[] metals = MetalType.values();
		int length = metals.length;
		ItemStack[] ingots = new ItemStack[length];
		ItemStack[] dusts = new ItemStack[length];
		
		for(int i = 0; i < length; ++i) {

			ingots[i] = ModItems.ingotMetals.createItemStack(metals[i], 1);
			dusts[i] = ModItems.dustMetals.createItemStack(metals[i], 1);
		}

		ItemStack doubledYelloriumDust = ModItems.dustMetals.createItemStack(MetalType.Yellorium, 2);

		// TODO disabled as there is no ThermalExpansion for 1.9.x
		/*
		if(Loader.isModLoaded("ThermalExpansion")) {

			ItemStack sandStack = new ItemStack(Blocks.sand, 1);
			ItemStack doubleYelloriumIngots = ModItems.ingotMetals.createItemStack(MetalType.Yellorium, 2);

			// TODO: Remove ThermalExpansionHelper once addSmelterRecipe and addPulverizerRecipe aren't broken
			if(ingots[YELLORIUM] != null) {

				ThermalExpansionHelper.addFurnaceRecipe(400, yelloriteOre, ingots[yelloriumIndex]);
				ThermalExpansionHelper.addSmelterRecipe(1600, yelloriteOre, sandStack, doubleYelloriumIngots);
			}

			if(doubledYelloriumDust != null) {

				ThermalExpansionHelper.addPulverizerRecipe(4000, yelloriteOre, doubledYelloriumDust);
				ThermalExpansionHelper.addSmelterRecipe(200, doubledYelloriumDust, sandStack, doubleYelloriumIngots);
			}

			for(int i = 0; i < ingots.length; i++) {
				if(ingots[i] == null || dusts[i] == null) { continue; }

				ThermalExpansionHelper.addPulverizerRecipe(2400, ingots[i], dusts[i]);
				ThermalExpansionHelper.addSmelterRecipe(200, doubledYelloriumDust, sandStack, doubleYellorium);

				ItemStack doubleDust = dusts[i].copy();
				doubleDust.stackSize = 2;
				ItemStack doubleIngot = ingots[i].copy();
				doubleIngot.stackSize = 2;

				ThermalExpansionHelper.addSmelterRecipe(200, doubleDust, sandStack, doubleIngot);
			}
		} // END: IsModLoaded - ThermalExpansion
		*/
		
		if(Loader.isModLoaded("MineFactoryReloaded")) {
			// Add yellorite to yellow focus list.
			IMCHelper.MFR.addOreToMiningLaserFocus(yelloriteOre, 2);
            
            // Make Yellorite the 'preferred' ore for lime focus
            IMCHelper.MFR.setMiningLaserFocusPreferredOre(yelloriteOre, 9);
		} // END: IsModLoaded - MineFactoryReloaded
		
		if(Loader.isModLoaded("appliedenergistics2")) {
			if(doubledYelloriumDust != null) {
				IMCHelper.AE2.addGrinderRecipe(yelloriteOre, doubledYelloriumDust, 4);
			}
		
			for(int i = 0; i < ingots.length; i++) {
				if(ingots[i] == null || dusts[i] == null) { continue; }
				IMCHelper.AE2.addGrinderRecipe(ingots[i], dusts[i], 2);
			}
		} // END: IsModLoaded - AE2
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
		
		ModHelperBase.detectMods();

		modHelper = new ModHelperComputerCraft();
		modHelper.register();
		
		modHelper = new ModHelperMekanism();
		modHelper.register();
	}
}
