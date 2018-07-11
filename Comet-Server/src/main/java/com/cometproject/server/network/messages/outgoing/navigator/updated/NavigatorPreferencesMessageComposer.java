package com.cometproject.server.network.messages.outgoing.navigator.updated;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class NavigatorPreferencesMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.NavigatorPreferencesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(68);//x
        msg.writeInt(42);//y
        msg.writeInt(425);//width
        msg.writeInt(592);//height
        msg.writeBoolean(false); // show or hide saved searches
        msg.writeInt(0);
    }
}
