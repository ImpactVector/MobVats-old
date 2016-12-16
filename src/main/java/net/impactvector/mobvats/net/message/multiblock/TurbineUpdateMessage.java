package net.impactvector.mobvats.net.message.multiblock;

import net.impactvector.mobvats.common.multiblock.MultiblockTurbine;
import net.impactvector.mobvats.net.message.base.TurbineMessageClient;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TurbineUpdateMessage extends TurbineMessageClient {

	public TurbineUpdateMessage() {
		this._data = null;
	}

	public TurbineUpdateMessage(MultiblockTurbine turbine) {

		super(turbine);
		this._data = null;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {

		super.toBytes(buf);
		this.TURBINE.serialize(buf);
	}
	
	@Override public void fromBytes(ByteBuf buf) {

		super.fromBytes(buf);
		this._data = buf.readBytes(buf.readableBytes());
	}

	protected ByteBuf _data;
	
	public static class Handler extends TurbineMessageClient.Handler<TurbineUpdateMessage> {

		@Override
		protected void processTurbineMessage(TurbineUpdateMessage message, MessageContext ctx, MultiblockTurbine turbine) {
			turbine.deserialize(message._data);
		}
	}
}
