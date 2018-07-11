package com.cometproject.server.network.messages.incoming.gamecenter;

import com.cometproject.server.game.gamecenter.GameCenterInfo;
import com.cometproject.server.game.gamecenter.GameCenterManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.gamecenter.GameAccountStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.gamecenter.GameAchievementsMessageComposer;
import com.cometproject.server.network.messages.outgoing.gamecenter.GameStatusMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GetGameStatusMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int gameId = msg.readInt();

        client.send(new GameAccountStatusMessageComposer(gameId));
        client.send(new GameStatusMessageComposer(gameId, 0));
        //client.send(new GameAchievementsMessageComposer(gameId, client.getPlayer().getAchievements()));
    }
}
