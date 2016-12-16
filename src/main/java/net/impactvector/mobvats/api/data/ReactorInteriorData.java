package net.impactvector.mobvats.api.data;

public class ReactorInteriorData {
	public float absorption, burnRate, nutrition;

	/**
	 * @param burnRate How much the material affects the speed of maturation. 0.1 = 10% speed, 10 = 10x speed
	 * @param absorption	How efficiently the material transfers energy. 0.1 = 10% efficiency, 10 = 10x efficiency.
	 * @param nutrition	How healthy the mob will be when it matures. Floor() = looting level.
	 */
	public ReactorInteriorData(float absorption, float burnRate, float nutrition) {
		this.burnRate = Math.max(0.1f, Math.min(10f, burnRate));
		this.absorption = Math.max(0.1f, Math.min(10f, absorption));
		this.nutrition = Math.max(0f, Math.min(5f, nutrition));
	}
}
