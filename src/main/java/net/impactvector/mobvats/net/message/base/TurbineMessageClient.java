package net.impactvector.mobvats.net.message.base;

import net.impactvector.mobvats.common.ModEventLog;
import net.impactvector.mobvats.common.multiblock.MultiblockTurbine;
import net.impactvector.mobvats.common.multiblock.tileentity.TileEntityTurbinePartBase;
import it.zerono.mods.zerocore.lib.network.ModTileEntityMessage;
import it.zerono.mods.zerocore.lib.network.ModTileEntityMessageHandlerClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class TurbineMessageClient extends ModTileEntityMessage {

	protected final MultiblockTurbine TURBINE;

	protected TurbineMessageClient() {
		this.TURBINE = null;
	}

	protected TurbineMessageClient(MultiblockTurbine turbine) {

		super(turbine.getReferenceCoord());
		this.TURBINE = turbine;
	}

	public static abstract class Handler<MessageT extends TurbineMessageClient> extends ModTileEntityMessageHandlerClient<MessageT> {

		@Override
		protected void processTileEntityMessage(MessageT message, MessageContext ctx, TileEntity tileEntity) {

			BlockPos position = null != tileEntity ? tileEntity.getPos() : null;

			if (tileEntity instanceof TileEntityTurbinePartBase) {

				MultiblockTurbine turbine = ((TileEntityTurbinePartBase)tileEntity).getTurbine();

				if (null != turbine) {

					this.processTurbineMessage(message, ctx, turbine);

				} else {

					ModEventLog.error("Received TurbineMessageClient for a turbine part @ %d, %d, %d which has no attached turbine",
							position.getX(), position.getY(), position.getZ());
				}
			} else if (null != position) {

				ModEventLog.error("Received TurbineMessageClient for a non-turbine-part block @ %d, %d, %d",
						position.getX(), position.getY(), position.getZ());
			}
		}

		protected abstract void processTurbineMessage(final MessageT message, final MessageContext ctx, final MultiblockTurbine turbine);
	}
}
