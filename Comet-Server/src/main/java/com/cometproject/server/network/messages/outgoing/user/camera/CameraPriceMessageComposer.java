package com.cometproject.server.network.messages.outgoing.user.camera;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Salinas on 12/02/2018.
 */
public class CameraPriceMessageComposer extends MessageComposer {

    private static int priceCredits = 0;
    private static int priceDuckets = 0;
    private static int pricePublishDuckets = 0;

    public CameraPriceMessageComposer(int priceCredits, int priceDuckets, int pricePublishDuckets) {
        this.priceCredits = priceCredits;
        this.priceDuckets = priceDuckets;
        this.pricePublishDuckets = pricePublishDuckets;
    }

    @Override
    public short getId() {
        return Composers.CameraPriceMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.priceCredits);
        msg.writeInt(this.priceDuckets);
        msg.writeInt(this.pricePublishDuckets);
    }
}
