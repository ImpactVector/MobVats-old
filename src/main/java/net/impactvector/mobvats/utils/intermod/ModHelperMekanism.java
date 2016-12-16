package net.impactvector.mobvats.utils.intermod;

import net.impactvector.mobvats.common.MetalType;
import net.impactvector.mobvats.common.item.ItemBRMetal;
import net.impactvector.mobvats.init.ModBlocks;
import net.impactvector.mobvats.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import java.lang.reflect.Method;

public class ModHelperMekanism extends ModHelperBase {

	@Optional.Method(modid = "Mekanism")
	@Override
	public void register() {
		ItemBRMetal ingotGeneric = ModItems.ingotMetals;
		
		ItemStack yelloriteOre 	= new ItemStack(ModBlocks.brOre, 1);
		ItemStack ingotYellorium= ModItems.ingotMetals.createItemStack(MetalType.Yellorium, 1);
		ItemStack ingotCyanite 	= ModItems.ingotMetals.createItemStack(MetalType.Cyanite, 1);
		ItemStack ingotGraphite = ModItems.ingotMetals.createItemStack(MetalType.Graphite, 1);
		ItemStack ingotBlutonium= ModItems.ingotMetals.createItemStack(MetalType.Blutonium, 1);
		ItemStack dustYellorium = ModItems.dustMetals.createItemStack(MetalType.Yellorium, 1);
		ItemStack dustCyanite 	= ModItems.dustMetals.createItemStack(MetalType.Cyanite, 1);
		ItemStack dustGraphite 	= ModItems.dustMetals.createItemStack(MetalType.Graphite, 1);
		ItemStack dustBlutonium = ModItems.dustMetals.createItemStack(MetalType.Blutonium, 1);

		// Some mods make me do this myself. :V
		ItemStack doubledYelloriumDust = null;
		if(dustYellorium != null) {
			doubledYelloriumDust = dustYellorium.copy();
			doubledYelloriumDust.stackSize = 2;
		}

		if(yelloriteOre != null && doubledYelloriumDust != null) {
			addMekanismEnrichmentChamberRecipe(yelloriteOre.copy(), doubledYelloriumDust.copy());
			ItemStack octupledYelloriumDust = dustYellorium.copy();
			octupledYelloriumDust.stackSize = 8;
			addMekanismCombinerRecipe(octupledYelloriumDust, yelloriteOre.copy());
		}
	
		if(ingotYellorium != null && dustYellorium != null) {
			addMekanismCrusherRecipe(ingotYellorium.copy(), dustYellorium.copy());
		}

		if(ingotCyanite != null && dustCyanite != null) {
			addMekanismCrusherRecipe(ingotCyanite.copy(), dustCyanite.copy());
		}

		if(ingotGraphite != null && dustGraphite != null) {
			addMekanismCrusherRecipe(ingotGraphite.copy(), dustGraphite.copy());
		}

		if(ingotBlutonium != null && dustBlutonium != null) {
			addMekanismCrusherRecipe(ingotBlutonium.copy(), dustBlutonium.copy());
		}
	}
	
	/// Mekanism Compat - taken from Mekanism's API. Extracted to allow compat with last known green build.
	/**
	 * Add an Enrichment Chamber recipe. (Ore -> 2 Dust)
	 * @param input - input ItemStack
	 * @param output - output ItemStack
	 */
	public static void addMekanismEnrichmentChamberRecipe(ItemStack input, ItemStack output)
	{
		try {
			Class recipeClass = Class.forName("mekanism.api.RecipeHelper");
			Method m = recipeClass.getMethod("addEnrichmentChamberRecipe", ItemStack.class, ItemStack.class);
			m.invoke(null, input, output);
		} catch(Exception e) {
			System.err.println("[Mekanism] Error while adding recipe: " + e.getMessage());
		}
	}

	/**
	 * Add a Combiner recipe. (8 Dust + Cobble -> Ore)
	 * @param input - input ItemStack
	 * @param output - output ItemStack
	 */
	public static void addMekanismCombinerRecipe(ItemStack input, ItemStack output)
	{
		try {
			Class recipeClass = Class.forName("mekanism.api.RecipeHelper");
			Method m = recipeClass.getMethod("addCombinerRecipe", ItemStack.class, ItemStack.class);
			m.invoke(null, input, output);
		} catch(Exception e) {
			System.err.println("[Mekanism] Error while adding recipe: " + e.getMessage());
		}
	}
	
	/**
	 * Add a Crusher recipe. (Ingot -> Dust)
	 * @param input - input ItemStack
	 * @param output - output ItemStack
	 */
	public static void addMekanismCrusherRecipe(ItemStack input, ItemStack output)
	{
		try {
			Class recipeClass = Class.forName("mekanism.api.RecipeHelper");
			Method m = recipeClass.getMethod("addCrusherRecipe", ItemStack.class, ItemStack.class);
			m.invoke(null, input, output);
		} catch(Exception e) {
			System.err.println("[Mekanism] Error while adding recipe: " + e.getMessage());
		}
	}
}
