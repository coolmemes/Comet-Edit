package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Salinas on 07/05/2017.
 */
public class LimitedEditionSoldOutMessageComposer extends MessageComposer{

    public LimitedEditionSoldOutMessageComposer() {

    }


    @Override
    public short getId() {
        return Composers.LimitedEditionSoldOutMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
    }
}

