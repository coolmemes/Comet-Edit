package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GenericErrorMessageComposer extends MessageComposer {
    private final int errorCode;

    public GenericErrorMessageComposer(final int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public short getId() {
        return Composers.GenericErrorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.errorCode);
    }
}
