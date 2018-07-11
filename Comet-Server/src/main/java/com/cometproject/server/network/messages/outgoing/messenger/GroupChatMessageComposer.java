package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GroupChatMessageComposer extends MessageComposer {
    private final int fromId;
    private final String message;
    private final String data;

    public GroupChatMessageComposer(final int fromId, final String message, final String data) {
        this.fromId = fromId;
        this.message = message;
        this.data = data;
    }

    @Override
    public short getId() {
        return Composers.NewConsoleMessageMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(fromId);
        msg.writeString(message);
        msg.writeInt(0);
        msg.writeString(data);
    }
}
