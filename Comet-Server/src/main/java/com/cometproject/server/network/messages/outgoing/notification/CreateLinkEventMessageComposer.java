package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class CreateLinkEventMessageComposer extends MessageComposer {
    private String event;

    public CreateLinkEventMessageComposer(String event) {
        this.event = event;
    }

    @Override
    public short getId() {
        return Composers.CreateLinkEventMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(event);

    }
}