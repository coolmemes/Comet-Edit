package com.cometproject.server.network.messages.outgoing.marketplace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class MarketplaceSettingsMessageComposer extends MessageComposer {
    private final int minPrice;
    private final int maxPrice;

    public MarketplaceSettingsMessageComposer(int minPrice, int maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public short getId() {
        return Composers.MarketplaceSettingsMessageComposer;
    }

    public void compose(IComposer msg) {
        msg.writeBoolean(true);
        msg.writeInt(this.minPrice);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(this.maxPrice);
        msg.writeInt(48);
        msg.writeInt(7);
    }
}