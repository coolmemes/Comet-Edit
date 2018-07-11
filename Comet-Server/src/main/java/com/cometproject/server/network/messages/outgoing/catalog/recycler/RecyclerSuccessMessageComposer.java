package com.cometproject.server.network.messages.outgoing.catalog.recycler;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Salinas on 01/02/2018.
 */
public class RecyclerSuccessMessageComposer extends MessageComposer {


    public RecyclerSuccessMessageComposer() {
    }

    @Override
    public short getId() {
        return Composers.RecyclerSuccessMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

        msg.writeInt(1);
        msg.writeInt(0);
    }
}
