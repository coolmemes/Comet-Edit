package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class MallOfferMessageComposer extends MessageComposer{

    public MallOfferMessageComposer() {

    }


    @Override
    public short getId() {
        return Composers.HabboMall;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("test");
    }
}

