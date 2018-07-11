package com.cometproject.server.network.messages.incoming.gamecenter;

import com.cometproject.server.game.gamecenter.GameCenterInfo;
import com.cometproject.server.game.gamecenter.GameCenterManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.gamecenter.GameLoadMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.UUID;

public class JoinGameQueueMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int gameId = msg.readInt();

        GameCenterInfo gameInfo = GameCenterManager.getInstance().getGameById(gameId);

        if(gameInfo.getGameRoomId() != 0){
            client.send(new RoomForwardMessageComposer(gameInfo.getGameRoomId()));
        }
        else{

        /*final UUID sessionId = UUID.randomUUID();

        PlayerManager.getInstance().getSsoTicketToPlayerId().put(client.getPlayer().getId() + sessionId.toString(), client.getPlayer().getId());

        client.send(new GameLoadMessageComposer(gameId, "http://localhost/comet/swf/games/gamecenter_basejump/BaseJump.swf", client.getPlayer().getId() + sessionId.toString(), "localhost", "30010", "30010", "http://localhost/comet/swf/games/gamecenter_basejump/BasicAssets.swf"));
        */
        }
    }
}
