package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Administrator on 7/15/2017.
 */

public class SimpleAlertMessageComposer extends MessageComposer {
    private String message;

    public SimpleAlertMessageComposer(String message) {
        this.message = message;
    }

    @Override
    public short getId() {
        return Composers.ModeratorSupportTicketResponseMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);
        msg.writeString(message);
    }
}
