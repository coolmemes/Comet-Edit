package com.cometproject.server.network.messages.outgoing.user.minimail;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class NewMinimailMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.NewMinimailMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
    }
}
