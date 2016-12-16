package net.impactvector.mobvats.api.registry;

import net.impactvector.mobvats.api.IReactorFuel;
import net.impactvector.mobvats.api.data.FluidToReactantMapping;
import net.impactvector.mobvats.api.data.OreDictToReactantMapping;
import net.impactvector.mobvats.api.data.ReactantData;
import net.impactvector.mobvats.api.data.SourceProductMapping;
import net.impactvector.mobvats.common.ModEventLog;
import net.impactvector.mobvats.common.MobVats;
import net.impactvector.mobvats.common.data.ReactorSolidMapping;
import it.zerono.mods.zerocore.util.OreDictionaryHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Reactants {
	
	public static final int standardSolidReactantAmount = 1000; // 1 item = 1000 mB, standard
	public static final int standardFluidReactantAmount = 1; // 1 mB = 1 mB

	private static Map<String, ReactantData> _reactants = new HashMap<String, ReactantData>();
	
	// 1:1
	private static Map<String, OreDictToReactantMapping> _solidToReactant = new HashMap<String, OreDictToReactantMapping>();
	private static Map<String, FluidToReactantMapping>   _fluidToReactant = new HashMap<String, FluidToReactantMapping>();
	
	// 1:many
	private static Map<String, List<SourceProductMapping>> _reactantToSolid = new HashMap<String, List<SourceProductMapping>>();
	private static Map<String, List<SourceProductMapping>> _reactantToFluid = new HashMap<String, List<SourceProductMapping>>();

	private static Map<String, ItemStack> _reactorFluidToSolid = new HashMap<String, ItemStack>();
	private static Set<ReactorSolidMapping> _reactorSolidToFuel = new CopyOnWriteArraySet<ReactorSolidMapping>(); // This won't work
	private static Set<ReactorSolidMapping> _reactorSolidToWaste = new CopyOnWriteArraySet<ReactorSolidMapping>(); // This won't work

	private static Map<String, IReactorFuel> _reactorFluids = new HashMap<String, IReactorFuel>();
	
	//// REGISTRATION
	
	/**
	 * Simple wrapper for quickly registering standard reactants.
	 * Sets default colors for fuel/waste.
	 * @param name Name of the reactant
	 * @param fuel True if fuel, false if waste.
	 */
	public static void registerReactant(String name, boolean fuel) {
		registerReactant(name,
						 fuel ? 0 : 1,
						 fuel ? MobVats.defaultFluidColorFuel : MobVats.defaultFluidColorWaste
						);
	}
	
	/**
	 * Register a new type of reactant.
	 * @param name The name of the reactant.
	 * @param type The type of the reactant. 0 = fuel, 1 = waste. Other values unsupported.
	 * @param color The color of the reactant, format 0xRRGGBB.
	 */
	public static void registerReactant(String name, int type, int color) {
		if(type < 0 || type >= ReactantData.TYPES.length) {
			throw new IllegalArgumentException("Unsupported type; value may only be 0 or 1");
		}
		
		if(_reactants.containsKey(name)) {
			ModEventLog.warning("Overwriting data for reactant %s - someone may be altering BR game data or have duplicate reactant names!", name);
		}
		
		ReactantData data = new ReactantData(name, ReactantData.TYPES[type], color);
		_reactants.put(name, data);
	}
	
	/**
	 * Register a solid ItemStack as a valid reactant for the reactor.
	 * For fuels, it will allow access ports to accept the item in the inlet slot.
	 * For wastes, it will allow access ports to eject the item into the outlet slot.
	 * @param itemStack The item which represents a reactant. Its quantity indicates how many items will be used to make 1000mB of reactant.
	 * @param reactantName The name of the reactant.
	 */
	public static SourceProductMapping registerSolid(ItemStack itemStack, String reactantName) {

		return registerSolid(itemStack, reactantName, Reactants.standardSolidReactantAmount);
	}

	/**
	 * Register a solid ItemStack as a valid reactant for the reactor.
	 * For fuels, it will allow access ports to accept the item in the inlet slot.
	 * For wastes, it will allow access ports to eject the item into the outlet slot.
	 * @param itemStack The item which represents a reactant. Its quantity indicates how many items will be used to make the defined units of reactant.
	 * @param reactantName The name of the reactant.
	 * @param reactantQty The quantity of the reactant produced by the itemStack.stackSize units of the item.
	 */
	public static SourceProductMapping registerSolid(ItemStack itemStack, String reactantName, int reactantQty) {

		if (!_reactants.containsKey(reactantName)) {
			throw new IllegalArgumentException("Unknown reactantName " + reactantName);
		}

		String[] oreDictNames = OreDictionaryHelper.getOreNames(itemStack);
		SourceProductMapping firstMapping = null;

		if (null == oreDictNames) {

			ModEventLog.warning("Reactants.registerSolid: Could not resolve ore dict name for %s", itemStack.getUnlocalizedName());
			return null;
		}

		for(String name : oreDictNames) {

			OreDictToReactantMapping mapping = new OreDictToReactantMapping(name, reactantName, reactantQty);
			SourceProductMapping reverseMapping = mapping.getReverse();

			_solidToReactant.put(mapping.getSource(), mapping);
			mapReactant(reverseMapping.getSource(), reverseMapping, _reactantToSolid);

			if(firstMapping == null) { firstMapping = mapping; }
		}
		
		return firstMapping;		
	}

	/**
	 * Register a raw ore dictionary name to convert to reactant. Uses standard quantities
	 * of 1 unit of ore = 1000 units of fuel.
	 * @param oreDictName OreDict name of the solid to register as a reactant.
	 * @param reactantName Name of the reactant.
	 */
	public static SourceProductMapping registerSolid(String oreDictName, String reactantName, int reactantAmount) {
		if(!_reactants.containsKey(reactantName)) {
			throw new IllegalArgumentException("Unknown reactantName " + reactantName);
		}
		
		OreDictToReactantMapping mapping = new OreDictToReactantMapping(oreDictName, reactantName, reactantAmount);
		
		_solidToReactant.put(mapping.getSource(), mapping);
		
		SourceProductMapping reverseMapping = mapping.getReverse();
		mapReactant(reverseMapping.getSource(), reverseMapping, _reactantToSolid);
		return mapping;
	}
	
	/**
	 * Register a raw ore dictionary name to convert to reactant. Uses standard quantities
	 * of 1 unit of ore = 1000 units of fuel.
	 * @param oreDictName OreDict name of the solid to register as a reactant.
	 * @param reactantName Name of the reactant.
	 */
	public static SourceProductMapping registerSolid(String oreDictName, String reactantName) {
		if(!_reactants.containsKey(reactantName)) {
			throw new IllegalArgumentException("Unknown reactantName " + reactantName);
		}
		
		OreDictToReactantMapping mapping = new OreDictToReactantMapping(oreDictName, reactantName);
		SourceProductMapping reverseMapping = mapping.getReverse();
		
		_solidToReactant.put(mapping.getSource(), mapping);
		mapReactant(reverseMapping.getSource(), reverseMapping, _reactantToSolid);
		return mapping;
	}
	
	/**
	 * Register a fluid as a valid reactant for the reactor.
	 * Currently unused. Will be used for the fluid fueling cycle in later releases.
	 * This method produces a fluidStack.amount : 1 ratio of fluid to reactant.
	 * Generally, you should pass in fluidStacks with an amount set to 1.
	 * @param fluidStack The fluid representing a unit of input to convert into reactant. Generally should be 1.
	 * @param reactantName The name of the reactant.
	 */
	public static void registerFluid(FluidStack fluidStack, String reactantName) {
		if(!_reactants.containsKey(reactantName)) {
			throw new IllegalArgumentException("Unknown reactantName " + reactantName);
		}

		FluidToReactantMapping mapping = new FluidToReactantMapping(fluidStack, reactantName);
		SourceProductMapping reverseMapping = mapping.getReverse();

		_fluidToReactant.put(mapping.getSource(), mapping);
		mapReactant(reverseMapping.getSource(), reverseMapping, _reactantToFluid);
	}
	
	/**
	 * Register a fluid as a valid reactant for the reactor.
	 * Currently unused.
	 * This method enforces a 1:1 ratio for fluid mB to reactant mB.
	 * @param fluid The input fluid to convert to reactant.
	 * @param reactantName The name of the created reactant.
	 */
	public static void registerFluid(Fluid fluid, String reactantName) {

		if (null == fluid) {
			FMLLog.info("TEMP - Skipping registration of NULL fluid for reactant %s",reactantName );
			return;
		}

		if(!_reactants.containsKey(reactantName)) {
			throw new IllegalArgumentException("Unknown reactantName " + reactantName);
		}

		FluidToReactantMapping mapping = new FluidToReactantMapping(fluid, reactantName);
		SourceProductMapping reverseMapping = mapping.getReverse();

		_fluidToReactant.put(mapping.getSource(), mapping);
		mapReactant(reverseMapping.getSource(), reverseMapping, _reactantToFluid);
	}
	
	//// GETTERS
	public static boolean isKnown(String reactantName) {
		return _reactants.containsKey(reactantName);
	}
	
	public static ReactantData getReactant(String name) {
		return _reactants.get(name);
	}
	
	public static OreDictToReactantMapping getSolidToReactant(ItemStack stack) {

		int[] oreIDs = OreDictionary.getOreIDs(stack);
		int oreCount;

		if (oreIDs == null || (oreCount = oreIDs.length) < 1)
			return null;

		String oreDictName;
		OreDictToReactantMapping mapping;

		for (int i = 0; i < oreCount; ++i) {

			oreDictName = OreDictionary.getOreName(oreIDs[i]);
			mapping = _solidToReactant.get(oreDictName);

			if (null != mapping)
				return mapping;
		}

		return null;
	}
	
	public static FluidToReactantMapping getFluidToReactant(FluidStack fluid) {
		return _fluidToReactant.get(fluid.getFluid().getName());
	}
	
	public static FluidToReactantMapping getFluidToReactant(Fluid fluid) {
		return _fluidToReactant.get(fluid.getName());
	}
	
	/**
	 * @param reactant The reactant to query
	 * @return A. list of Reactant => OreDict mappings. Note that reactant is the source and OreDict names are the product.
	 */
	public static List<SourceProductMapping> getReactantToSolids(String reactant) {
		return _reactantToSolid.get(reactant);
	}
	
	public static List<SourceProductMapping> getFluidsForReactant(String reactant) {
		return _reactantToFluid.get(reactant);
	}
	
	/// CONVENIENCE METHODS
	public static boolean isFuel(ItemStack stack) {
		if(stack == null) { return false; }
		
		return isFuel(getReactantName(stack));
	}

	/**
	 * Returns the first registered reactant name for a given item stack,
	 * based on its ore dictionary entry.
	 * @param stack The item stack to query.
	 * @return The name of the reactant represented by this item stack, or null.
	 */
	public static String getReactantName(ItemStack stack) {
		SourceProductMapping mapping = getSolidToReactant(stack);
		return mapping != null ? mapping.getProduct() : null;
	}
	
	/**
	 * Returns true if a given name represents a reactant.
	 * @param name
	 * @return
	 */
	public static boolean isFuel(String name) {
		if(name == null) { return false; }
		else {
			ReactantData data = getReactant(name);
			return data != null && data.isFuel();
		}
	}
	
	public static boolean isWaste(ItemStack stack) {
		if(stack == null) { return false; }
		
		return isWaste(getReactantName(stack));
	}
	
	public static boolean isWaste(String name) {
		if(name == null) { return false; }
		else {
			ReactantData data = getReactant(name);
			return data != null && data.isWaste();
		}
	}
	
	/**
	 * @return The smallest amount of reactant found in a given reactant<>solid mapping set
	 * @throws IllegalArgumentException if no reactants were mapped.
	 */
	public static int getMinimumReactantToProduceSolid(String reactantName) {
		List<SourceProductMapping> mappings = getReactantToSolids(reactantName);
		if(mappings == null || mappings.size() <= 0) {
			throw new IllegalArgumentException("No solid products mapped for reactant " + reactantName);
		}
		
		int minimumAmount = Integer.MAX_VALUE;
		for(SourceProductMapping mapping : mappings) {
			if(mapping.getSourceAmount() < minimumAmount) {
				minimumAmount = mapping.getSourceAmount();
			}
		}
		
		return minimumAmount;
	}

	//// HELPERS
	private static void mapReactant(String reactantName, SourceProductMapping mapping, Map<String, List<SourceProductMapping>> map) {
		List<SourceProductMapping> list = null;
		if(!map.containsKey(reactantName) || map.get(reactantName) == null) {
			list = new ArrayList<SourceProductMapping>();
			map.put(reactantName, list);
		}
		else {
			list = map.get(reactantName);
			list.add(mapping);
		}
		
		list.add(mapping);
	}	
}
