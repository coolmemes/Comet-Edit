package com.cometproject.server.network.messages.outgoing.marketplace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class BuyOfferMessageComposer extends MessageComposer {
    private final int code;
    private final int offerId;
    private final int price;

    public BuyOfferMessageComposer(int code, int offerId, int price) {
        this.code = code;
        this.offerId = offerId;
        this.price = price;
    }

    public short getId() {
        return Composers.BuyOfferMessageComposer;
    }

    public void compose(IComposer msg) {
        msg.writeInt(this.code);
        msg.writeInt(0);
        msg.writeInt(this.offerId);
        msg.writeInt(this.price);
    }
}