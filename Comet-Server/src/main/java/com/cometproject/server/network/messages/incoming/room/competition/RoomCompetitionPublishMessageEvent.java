package com.cometproject.server.network.messages.incoming.room.competition;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.competition.CompetitionManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.competition.RoomCompetitionOwnerInfoMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class RoomCompetitionPublishMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {

        if(CometSettings.competitionCampaignEnabled) {
            Room room = client.getPlayer().getEntity().getRoom();

            if(room.getData().getOwnerId() != client.getPlayer().getId())
                return;

            CompetitionManager.getInstance().addNewCompetitionData(room.getId(), client.getPlayer().getId());
            client.send(new RoomCompetitionOwnerInfoMessageComposer(client.getPlayer(), CometSettings.competitionCampaignName, 0));
        }
    }
}