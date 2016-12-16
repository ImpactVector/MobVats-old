package net.impactvector.mobvats.net.message.multiblock;

import net.impactvector.mobvats.common.multiblock.MultiblockReactor;
import net.impactvector.mobvats.net.message.base.ReactorMessageClient;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ReactorUpdateWasteEjectionMessage extends ReactorMessageClient {

    public ReactorUpdateWasteEjectionMessage() {
        this._newSetting = 0;
    }

    public ReactorUpdateWasteEjectionMessage(MultiblockReactor reactor) {

    	super(reactor);
        this._newSetting = reactor.getWasteEjection().ordinal();
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    	super.fromBytes(buf);
        this._newSetting = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {

    	super.toBytes(buf);
        buf.writeInt(this._newSetting);
    }

    private int _newSetting;

    public static class Handler extends ReactorMessageClient.Handler<ReactorUpdateWasteEjectionMessage> {

        @Override
        protected void processReactorMessage(ReactorUpdateWasteEjectionMessage message, MessageContext ctx, MultiblockReactor reactor) {
            reactor.setWasteEjection(MultiblockReactor.s_EjectionSettings[message._newSetting]);
        }
    }
}
