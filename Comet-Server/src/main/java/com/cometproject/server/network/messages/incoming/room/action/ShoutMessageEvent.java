package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.PrivateChatFloorItem;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.ShoutMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserNameChangeMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;


public class ShoutMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        String message = msg.readString();
        int colour = msg.readInt();

        if (message.length() < 1) return;

        if (!TalkMessageEvent.isValidColour(colour, client)) {
            colour = 0;
        }

        String filteredMessage = TalkMessageEvent.filterMessage(message);

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null)
            return;

        if(!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        if (!client.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(filteredMessage);

            if (filterResult.isBlocked()) {
                client.getPlayer().sendBubble(Locale.get("command.filter.icon"),Locale.get("filter.prohibited.message"));
                final List<ChatMessage> chatMessage = Lists.newArrayList();
                chatMessage.add(new ChatMessage(client.getPlayer().getId(), filteredMessage));
                ModerationManager.getInstance().createTicket(client.getPlayer().getId(), message, 22, client.getPlayer().getId(), Comet.getTimeInt(), client.getPlayer().getEntity().getRoom().getId(), chatMessage);

                Date date = new Date();
                NetworkManager.getInstance().getSessions().broadcastToModerators(new NotificationMessageComposer(Locale.get("spam.notification.image"),
                        Locale.get("spam.notification.message").replace("%date%", String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes()) + ":" + String.valueOf(date.getSeconds())).replace("%roomname%", client.getPlayer().getEntity().getRoom().getData().getName()).replace("%username%", client.getPlayer().getData().getUsername()).replace("%type%", Locale.get("spam.type.shout")),
                        "event:navigator/goto/" + client.getPlayer().getEntity().getRoom().getId()));
                return;

            } else if (filterResult.wasModified()) {
                filteredMessage = filterResult.getMessage();
            }

            filteredMessage = client.getPlayer().getEntity().getRoom().getFilter().filter(client.getPlayer().getEntity(), filteredMessage);
        }

        if (client.getPlayer().getEntity().onChat(filteredMessage)) {
            try {
                if (LogManager.ENABLED)
                    LogManager.getInstance().getStore().getLogEntryContainer().put(new RoomChatLogEntry(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getId(), message));
            } catch (Exception ignored) {

            }

            if(client.getPlayer().getEntity().getPrivateChatItemId() != 0) {
                // broadcast message only to players in the tent.
                RoomItemFloor floorItem = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(client.getPlayer().getEntity().getPrivateChatItemId());

                if(floorItem != null) {
                    ((PrivateChatFloorItem) floorItem).broadcastMessage(new ShoutMessageComposer(client.getPlayer().getEntity().getId(), filteredMessage, RoomManager.getInstance().getEmotions().getEmotion(filteredMessage), colour));
                    client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UserNameChangeMessageComposer(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getEntity().getId(), client.getPlayer().getData().getUsername()));

                }
            } else {
                client.getPlayer().getEntity().getRoom().getEntities().broadcastChatMessage(new ShoutMessageComposer(client.getPlayer().getEntity().getId(), filteredMessage, RoomManager.getInstance().getEmotions().getEmotion(filteredMessage), colour), client.getPlayer().getEntity());
                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UserNameChangeMessageComposer(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getEntity().getId(), client.getPlayer().getData().getUsername()));

            }
        }

        client.getPlayer().getEntity().postChat(filteredMessage);
    }
}
