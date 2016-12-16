package net.impactvector.mobvats.api.registry;

import net.impactvector.mobvats.api.data.ReactorInteriorData;
import net.impactvector.mobvats.common.ModEventLog;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class ReactorInterior {
	private static Map<String, ReactorInteriorData> _reactorModeratorBlocks = new HashMap<String, ReactorInteriorData>();
	private static Map<String, ReactorInteriorData> _reactorModeratorFluids = new HashMap<String, ReactorInteriorData>();

	/**
	 * @param burnRate How much the material affects the speed of maturation. 0.1 = 10% speed, 10 = 10x speed
	 * @param absorption	How efficiently the material transfers energy. 0.1 = 10% efficiency, 10 = 10x efficiency.
	 * @param nutrition	How healthy the mob will be when it matures. Floor() = looting level.
	 */
	public static void registerBlock(String oreDictName, float absorption, float burnRate, float nutrition) {
		if(_reactorModeratorBlocks.containsKey(oreDictName)) {
			ModEventLog.warning("Overriding existing radiation moderator block data for oredict name <%s>", oreDictName);
			ReactorInteriorData data = _reactorModeratorBlocks.get(oreDictName);
			data.absorption = absorption;
			data.burnRate = burnRate;
			data.nutrition = nutrition;
		}
		else {
			_reactorModeratorBlocks.put(oreDictName, new ReactorInteriorData(absorption, burnRate, nutrition));
		}
	}

	/**
	 * @param burnRate How much the material affects the speed of maturation. 0.1 = 10% speed, 10 = 10x speed
	 * @param absorption	How efficiently the material transfers energy. 0.1 = 10% efficiency, 10 = 10x efficiency.
	 * @param nutrition	How healthy the mob will be when it matures. Floor() = looting level.
	 */
	public static void registerFluid(String fluidName, float absorption, float burnRate, float nutrition) {
		if(_reactorModeratorFluids.containsKey(fluidName)) {
			ModEventLog.warning("Overriding existing radiation moderator fluid data for fluid name <%s>", fluidName);
			ReactorInteriorData data = _reactorModeratorFluids.get(fluidName);
			data.absorption = absorption;
			data.burnRate = burnRate;
			data.nutrition = nutrition;
		}
		else {
			_reactorModeratorFluids.put(fluidName, new ReactorInteriorData(absorption, burnRate, nutrition));
		}
	}
	
	public static ReactorInteriorData getBlockData(String oreDictName) {
		return _reactorModeratorBlocks.get(oreDictName);
	}

	public static ReactorInteriorData getBlockData(ItemStack stack) {

		int[] ids = null != stack ? OreDictionary.getOreIDs(stack) : null;
		int len;

		if (null == ids || 0 == (len = ids.length))
			return null;

		String name;

		for (int i = 0; i < len; ++i) {

			name = OreDictionary.getOreName(ids[i]);

			if (_reactorModeratorBlocks.containsKey(name))
				return  _reactorModeratorBlocks.get(name);
		}

		return null;
	}

	public static ReactorInteriorData getFluidData(String fluidName) {
		return _reactorModeratorFluids.get(fluidName);
	}	
}
