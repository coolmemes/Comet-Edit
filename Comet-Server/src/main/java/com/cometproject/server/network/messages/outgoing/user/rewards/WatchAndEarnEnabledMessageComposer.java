package com.cometproject.server.network.messages.outgoing.user.rewards;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class WatchAndEarnEnabledMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.WatchAndEarnEnabledMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(true);
    }
}

