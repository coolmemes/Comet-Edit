package com.cometproject.server.network.messages.incoming.room.competition;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.competition.CompetitionManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.competition.RoomCompetitionVisitorInfoMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class RoomCompetitionVoteMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {

        Room room = client.getPlayer().getEntity().getRoom();

        if(client.getPlayer().getStats().getDailyRoomVotes() == 0 || room.getData().getOwnerId() == client.getPlayer().getId()
                || CompetitionManager.getInstance().playerHasVote(client.getPlayer().getId(), room.getId(), CometSettings.competitionCampaignId))
            return;

        client.getPlayer().getStats().decrementDailyRoomVotes();
        CompetitionManager.getInstance().addNewCompetitionData(room.getId(), client.getPlayer().getId());
        client.send(new RoomCompetitionVisitorInfoMessageComposer(client.getPlayer(), CometSettings.competitionCampaignName, 2));

    }
}