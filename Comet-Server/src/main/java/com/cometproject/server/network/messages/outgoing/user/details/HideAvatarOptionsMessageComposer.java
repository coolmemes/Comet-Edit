package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Salinas on 05/02/2018.
 */
public class HideAvatarOptionsMessageComposer extends MessageComposer {

    private final boolean state;

    public HideAvatarOptionsMessageComposer(final boolean state) {
        this.state = state;
    }

    @Override
    public short getId() {
        return Composers.HideAvatarOptionsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(this.state);
    }
}
