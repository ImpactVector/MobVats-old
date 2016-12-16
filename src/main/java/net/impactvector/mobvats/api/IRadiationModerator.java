package net.impactvector.mobvats.api;

import net.impactvector.mobvats.common.data.RadiationData;
import net.impactvector.mobvats.common.data.RadiationPacket;

public interface IRadiationModerator {
	public void moderateRadiation(RadiationData returnData, RadiationPacket radiation);
}
