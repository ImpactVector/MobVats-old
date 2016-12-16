package net.impactvector.mobvats.net.message.multiblock;

import net.impactvector.mobvats.common.multiblock.MultiblockTurbine;
import net.impactvector.mobvats.net.message.base.TurbineMessageServer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TurbineChangeInductorMessage extends TurbineMessageServer {

	public TurbineChangeInductorMessage() {
		this._newSetting = true;
	}

	public TurbineChangeInductorMessage(MultiblockTurbine turbine, boolean newSetting) {

		super(turbine);
		this._newSetting = newSetting;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {

		super.toBytes(buf);
		buf.writeBoolean(this._newSetting);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {

		super.fromBytes(buf);
		this._newSetting = buf.readBoolean();
	}

	private boolean _newSetting;
	
	public static class Handler extends TurbineMessageServer.Handler<TurbineChangeInductorMessage> {

		@Override
		protected void processTurbineMessage(TurbineChangeInductorMessage message, MessageContext ctx, MultiblockTurbine turbine) {
			turbine.setInductorEngaged(message._newSetting, true);
		}
	}
}
