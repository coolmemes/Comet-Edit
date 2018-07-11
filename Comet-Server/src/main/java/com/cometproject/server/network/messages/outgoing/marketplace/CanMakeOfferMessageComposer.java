package com.cometproject.server.network.messages.outgoing.marketplace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class CanMakeOfferMessageComposer extends MessageComposer {
    private final int code;

    public CanMakeOfferMessageComposer(int code) {
        this.code = code;
    }

    public short getId() {
        return Composers.CanMakeOfferMessageComposer;
    }

    public void compose(IComposer msg) {
        msg.writeInt(this.code);
        msg.writeInt(0);
    }
}