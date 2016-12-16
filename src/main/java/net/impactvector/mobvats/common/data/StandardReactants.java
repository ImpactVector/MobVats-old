package net.impactvector.mobvats.common.data;

import net.impactvector.mobvats.api.data.SourceProductMapping;
import net.impactvector.mobvats.api.registry.Reactants;
import net.impactvector.mobvats.common.MobVats;

public class StandardReactants {

	public static final String yellorium = "yellorium";
	public static final String cyanite = "cyanite";
	public static final String blutonium = "blutonium";
	
	public static final int colorYellorium = MobVats.defaultFluidColorFuel;
	public static final int colorCyanite = MobVats.defaultFluidColorWaste;
	
	// These are used as fallbacks
	public static SourceProductMapping yelloriumMapping;
	public static SourceProductMapping cyaniteMapping;
	
	public static void register() {
		Reactants.registerReactant(yellorium, 0, colorYellorium);
		Reactants.registerReactant(cyanite, 1, colorCyanite);
		Reactants.registerReactant(blutonium, 0, colorYellorium);
	}
	
}
