package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.quests.types.QuestType;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.help.TicketSentMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;
import com.google.common.collect.Lists;

import java.util.List;


public class ReportFriendMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {

        String message = msg.readString();
        int category = msg.readInt();
        int reportedId = msg.readInt();
        int timestamp = (int) Comet.getTime();
        int chatCount = msg.readInt();

        final List<ChatMessage> chatMessages = Lists.newArrayList();

        for (int i = 0; i < chatCount; i++) {
            final int playerId = msg.readInt();

            if (reportedId == 0) {
                reportedId = playerId;
            }

            final String chatMessage = msg.readString();

            chatMessages.add(new ChatMessage(playerId, chatMessage));
        }

        ModerationManager.getInstance().createTicket(client.getPlayer().getId(), message, category, reportedId, timestamp, 0, chatMessages);
        client.send(new TicketSentMessageComposer());
    }
}