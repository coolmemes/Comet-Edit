package com.cometproject.server.network.messages.incoming.room.competition;

import com.cometproject.api.game.rooms.settings.RoomAccessType;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.competition.CompetitionManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.competition.*;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import org.apache.commons.lang.StringUtils;

public class GetRoomCompetitionMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if(!CometSettings.competitionCampaignEnabled) return;

        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null || room.getData() == null || room.getData().getCategory() == null || !room.getData().getCategory().getCategoryId().equals("rooms_competition")) return;

        boolean invalidCompetition = false;
        boolean isActive = CompetitionManager.getInstance().competitionIsActive(client.getPlayer().getId(), room.getId(), CometSettings.competitionCampaignId);
        String campaign = CometSettings.competitionCampaignName;

        int regDate = StringUtils.isNumeric(client.getPlayer().getData().getRegDate()) ? Integer.parseInt(client.getPlayer().getData().getRegDate()) : client.getPlayer().getData().getRegTimestamp();

        boolean canVote = (int) Math.floor((((int) Comet.getTime()) - regDate) / 86400) > 10;

        if(room.getData().getAccess() != RoomAccessType.OPEN)
            invalidCompetition = true;

        if(invalidCompetition && room.getData().getOwnerId() == client.getPlayer().getId()) {
            client.send(new RoomCompetitionOwnerInfoMessageComposer(client.getPlayer(), campaign, 4));
            return;
        } else if (invalidCompetition) {
            client.send(new RoomCompetitionVisitorInfoMessageComposer(client.getPlayer(), campaign, 1));
            return;
        }

        if (room.getData().getOwnerId() == client.getPlayer().getId() && !isActive) {
            client.send(new RoomCompetitionOwnerInfoMessageComposer(client.getPlayer(), campaign, 1));
        } else if(room.getData().getOwnerId() == client.getPlayer().getId()) {
            client.send(new RoomCompetitionOwnerInfoMessageComposer(client.getPlayer(), campaign, 3));
        } else if (!isActive) {
            client.send(new RoomCompetitionVisitorInfoMessageComposer(client.getPlayer(), campaign, 3));
        } else if (!canVote) {
            client.send(new RoomCompetitionVisitorInfoMessageComposer(client.getPlayer(), campaign, 1));
        } else if (client.getPlayer().getStats().getDailyRoomVotes() > 0 && !CompetitionManager.getInstance().playerHasVote(client.getPlayer().getId(), room.getId(), CometSettings.competitionCampaignId) && isActive) {
            client.send(new RoomCompetitionVisitorInfoMessageComposer(client.getPlayer(), campaign, 0));
        } else {
            client.send(new RoomCompetitionVisitorInfoMessageComposer(client.getPlayer(), campaign, 2));
        }
    }
}

