package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class BullyReportRequestMessageComposer extends MessageComposer {
    private int status;

    public BullyReportRequestMessageComposer(int status) {
        this.status = status;
    }

    @Override
    public short getId() {
        return Composers.BullyReportRequestMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(status);
    }
}
