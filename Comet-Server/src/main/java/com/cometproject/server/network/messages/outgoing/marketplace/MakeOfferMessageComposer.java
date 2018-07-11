package com.cometproject.server.network.messages.outgoing.marketplace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class MakeOfferMessageComposer extends MessageComposer {
    private final int code;

    public MakeOfferMessageComposer(int code) {
        this.code = code;
    }

    public short getId() {
        return Composers.MakeOfferMessageComposer;
    }

    public void compose(IComposer msg) {
        msg.writeInt(this.code);
    }
}