package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.InviteFriendMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class InviteFriendsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final long time = System.currentTimeMillis();

        if (!client.getPlayer().getPermissions().getRank().floodBypass()) {
            if (time - client.getPlayer().getMessengerLastMessageTime() < 750) {
                client.getPlayer().setMessengerFloodFlag(client.getPlayer().getMessengerFloodFlag() + 1);

                if (client.getPlayer().getMessengerFloodFlag() >= 3) {
                    client.getPlayer().setMessengerFloodTime(client.getPlayer().getPermissions().getRank().floodTime());
                    client.getPlayer().setMessengerFloodFlag(0);

                }
            }

            if (client.getPlayer().getMessengerFloodTime() >= 1) {
                return;
            }

            client.getPlayer().setMessengerLastMessageTime(time);
        }

        int friendCount = msg.readInt();
        List<Integer> friends = new ArrayList<>();

        for (int i = 0; i < friendCount; i++) {
            friends.add(msg.readInt());
        }

        String message = msg.readString();

        if (!client.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(message);

            if (filterResult.isBlocked()) {
                client.getPlayer().sendBubble(Locale.get("command.filter.icon"),Locale.get("filter.prohibited.message"));
                final List<ChatMessage> chatMessage = Lists.newArrayList();
                chatMessage.add(new ChatMessage(client.getPlayer().getId(), message));
                ModerationManager.getInstance().createTicket(client.getPlayer().getId(), message, 22, client.getPlayer().getId(), Comet.getTimeInt(), client.getPlayer().getEntity().getRoom().getId(), chatMessage);

                Date date = new Date();
                NetworkManager.getInstance().getSessions().broadcastToModerators(new NotificationMessageComposer(Locale.get("spam.notification.image"),
                Locale.get("spam.notification.message").replace("%date%", String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes()) + ":" + String.valueOf(date.getSeconds())).replace("%username%", client.getPlayer().getData().getUsername()).replace("%type%", Locale.get("spam.type.invite")),
                ""));
                return;

            } else if (filterResult.wasModified()) {
                message = filterResult.getMessage();
            }

            message = client.getPlayer().getEntity().getRoom().getFilter().filter(client.getPlayer().getEntity(), message);
        }

        client.getPlayer().getMessenger().broadcast(friends, new InviteFriendMessageComposer(message, client.getPlayer().getId()));
        friends.clear();
    }
}
