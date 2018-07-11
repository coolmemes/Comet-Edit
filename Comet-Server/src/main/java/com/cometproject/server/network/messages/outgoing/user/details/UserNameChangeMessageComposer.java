package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class UserNameChangeMessageComposer extends MessageComposer {
    private final int roomId;
    private final int virtualId;
    private final String username;

    public UserNameChangeMessageComposer(int roomId, int virtualId, String username) {

        this.roomId = roomId;
        this.virtualId = virtualId;
        this.username = username;
    }

    @Override
    public short getId() {
        return Composers.UserNameChangeMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.roomId);
        msg.writeInt(this.virtualId);
        msg.writeString(this.username);
    }
}
