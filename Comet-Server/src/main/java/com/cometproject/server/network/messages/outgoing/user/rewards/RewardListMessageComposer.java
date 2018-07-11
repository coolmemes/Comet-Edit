package com.cometproject.server.network.messages.outgoing.user.rewards;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class RewardListMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.RewardListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("s"); // item Type
        msg.writeInt(204); // item ID
        msg.writeString("Estoy probando."); // item name
        msg.writeString("No funciona loco."); // item Desc
    }
}

