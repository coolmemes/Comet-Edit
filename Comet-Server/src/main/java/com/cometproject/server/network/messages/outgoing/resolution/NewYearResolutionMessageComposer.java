package com.cometproject.server.network.messages.outgoing.resolution;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.quests.types.Quest;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class NewYearResolutionMessageComposer extends MessageComposer {

    public NewYearResolutionMessageComposer() {
    }

    @Override
    public short getId() {
        return Composers.NewYearResolutionMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(30000); // time
        msg.writeInt(2); // size to foreach

        msg.writeInt(1); // ID
        msg.writeInt(2); // ???
        msg.writeString("AMB"); // Name
        msg.writeInt(3); // ???
        msg.writeInt(0); // ???

        msg.writeInt(2); // ID
        msg.writeInt(1); // ???
        msg.writeString("ADM"); // Name
        msg.writeInt(2); // ???
        msg.writeInt(0); // ???

        msg.writeInt(9999); // ???
    }
}
