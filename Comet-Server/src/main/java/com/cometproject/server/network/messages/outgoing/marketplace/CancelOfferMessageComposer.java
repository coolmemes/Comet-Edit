package com.cometproject.server.network.messages.outgoing.marketplace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class CancelOfferMessageComposer extends MessageComposer {
    private final int offerId;
    private final boolean success;

    public CancelOfferMessageComposer(int offerId, boolean success) {
        this.offerId = offerId;
        this.success = success;
    }

    public short getId() {
        return Composers.CancelOfferMessageComposer;
    }

    public void compose(IComposer msg) {
        msg.writeInt(this.offerId);
        msg.writeBoolean(Boolean.valueOf(this.success));
    }
}