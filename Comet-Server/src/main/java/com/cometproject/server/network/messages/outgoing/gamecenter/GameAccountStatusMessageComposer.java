package com.cometproject.server.network.messages.outgoing.gamecenter;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.gamecenter.GameCenterInfo;
import com.cometproject.server.game.gamecenter.GameCenterManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GameAccountStatusMessageComposer extends MessageComposer {

    private final int gameId;
    public GameAccountStatusMessageComposer(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public short getId() {
        return Composers.GameAccountStatusMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.gameId);
        msg.writeInt(-1);// can play = -1
        msg.writeInt(-1);
    }
}
