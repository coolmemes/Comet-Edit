package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.MessengerChatLogEntry;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.GroupChatMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;


public class PrivateChatMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int userId = msg.readInt();
        String message = msg.readString();

        if (userId == Integer.MAX_VALUE && client.getPlayer().getPermissions().getRank().messengerStaffChat()) {
            for (Session player : ModerationManager.getInstance().getModerators()) {
                if (player == client) continue;
                player.send(new GroupChatMessageComposer(Integer.MAX_VALUE, client.getPlayer().getData().getUsername() + ": " + message, client.getPlayer().getData().getUsername() + "/" + client.getPlayer().getData().getFigure() + "/" + client.getPlayer().getId()));
            }
            return;
        }

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(userId);

        if (friend == null) {
            return;
        }

        Session friendClient = friend.getSession();

        if (friendClient == null) {
            return;
        }

        final long time = System.currentTimeMillis();

        if (!client.getPlayer().getPermissions().getRank().floodBypass()) {
            if (time - client.getPlayer().getMessengerLastMessageTime() < 750) {
                client.getPlayer().setMessengerFloodFlag(client.getPlayer().getMessengerFloodFlag() + 1);

                if (client.getPlayer().getMessengerFloodFlag() >= 4) {
                    client.getPlayer().setMessengerFloodTime(client.getPlayer().getPermissions().getRank().floodTime());
                    client.getPlayer().setMessengerFloodFlag(0);

                }
            }

            if (client.getPlayer().getMessengerFloodTime() >= 1) {
                return;
            }

            client.getPlayer().setMessengerLastMessageTime(time);
        }

        if (!client.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(message);

            if (filterResult.isBlocked()) {
                client.getPlayer().sendBubble(Locale.get("command.filter.icon"),Locale.get("filter.prohibited.message"));
                final List<ChatMessage> chatMessage = Lists.newArrayList();
                chatMessage.add(new ChatMessage(client.getPlayer().getId(), message));
                ModerationManager.getInstance().createTicket(client.getPlayer().getId(), message, 22, client.getPlayer().getId(), Comet.getTimeInt(), client.getPlayer().getEntity().getRoom().getId(), chatMessage);

                Date date = new Date();
                NetworkManager.getInstance().getSessions().broadcastToModerators(new NotificationMessageComposer(Locale.get("spam.notification.image"),
                Locale.get("spam.notification.message").replace("%date%", String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes()) + ":" + String.valueOf(date.getSeconds())).replace("%username%", client.getPlayer().getData().getUsername()).replace("%type%", Locale.get("spam.type.private")),
                ""));
                return;

            } else if (filterResult.wasModified()) {
                message = filterResult.getMessage();
            }

            if(client.getPlayer().getEntity().getRoom() != null){
                message = client.getPlayer().getEntity().getRoom().getFilter().filter(client.getPlayer().getEntity(), message);
            }
        }

        try {
            if (LogManager.ENABLED && CometSettings.messengerLogMessages)
                LogManager.getInstance().getStore().getLogEntryContainer().put(new MessengerChatLogEntry(client.getPlayer().getId(), userId, message));
        } catch (Exception ignored) {

        }

        friendClient.send(new InstantChatMessageComposer(message, client.getPlayer().getId()));
    }
}
