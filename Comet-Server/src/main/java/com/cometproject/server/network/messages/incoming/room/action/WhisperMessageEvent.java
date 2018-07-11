package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserNameChangeMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;


public class WhisperMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        String text = msg.readString();
        int colour = msg.readInt();
        String user = text.split(" ")[0];
        String message = text.replace(user + " ", "");

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        if(!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();

        RoomEntity userTo = room.getEntities().getEntityByName(user, RoomEntityType.PLAYER);

        if (userTo == null || user.equals(client.getPlayer().getData().getUsername()))
            return;

        String filteredMessage = TalkMessageEvent.filterMessage(message);

        if (!client.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(filteredMessage);

            if (filterResult.isBlocked()) {
                client.getPlayer().sendBubble(Locale.get("command.filter.icon"),Locale.get("filter.prohibited.message"));
                final List<ChatMessage> chatMessage = Lists.newArrayList();
                chatMessage.add(new ChatMessage(client.getPlayer().getId(), filteredMessage));
                ModerationManager.getInstance().createTicket(client.getPlayer().getId(), message, 22, client.getPlayer().getId(), Comet.getTimeInt(), client.getPlayer().getEntity().getRoom().getId(), chatMessage);

                Date date = new Date();
                NetworkManager.getInstance().getSessions().broadcastToModerators(new NotificationMessageComposer(Locale.get("spam.notification.image"),
                        Locale.get("spam.notification.message").replace("%date%", String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes()) + ":" + String.valueOf(date.getSeconds())).replace("%roomname%", client.getPlayer().getEntity().getRoom().getData().getName()).replace("%username%", client.getPlayer().getData().getUsername()).replace("%type%", Locale.get("spam.type.whisper")),
                        "event:navigator/goto/" + client.getPlayer().getEntity().getRoom().getId()));
                return;

            } else if (filterResult.wasModified()) {
                filteredMessage = filterResult.getMessage();
            }

            filteredMessage = client.getPlayer().getEntity().getRoom().getFilter().filter(client.getPlayer().getEntity(), filteredMessage);
        }

        if (!client.getPlayer().getEntity().onChat(filteredMessage))
            return;

        try {
            if (LogManager.ENABLED)
                LogManager.getInstance().getStore().getLogEntryContainer().put(new RoomChatLogEntry(room.getId(), client.getPlayer().getId(), Locale.getOrDefault("game.logging.whisper", "<Whisper to %username%>").replace("%username%", user) + " " + message));
        } catch (Exception ignored) {

        }

        client.send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), filteredMessage, colour));

        if (!((PlayerEntity) userTo).getPlayer().ignores(client.getPlayer().getId()))
            ((PlayerEntity) userTo).getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), filteredMessage, colour));

        for (PlayerEntity entity : client.getPlayer().getEntity().getRoom().getEntities().getWhisperSeers()) {
            if (entity.getPlayer().getId() != client.getPlayer().getId() && !user.equals(entity.getUsername()))
                entity.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), "Whisper to " + user + ": " + filteredMessage, 0));
        }

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UserNameChangeMessageComposer(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getEntity().getId(), client.getPlayer().getData().getUsername()));
    }
}
