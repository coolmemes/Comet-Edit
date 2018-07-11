package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.PrivateChatFloorItem;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarChangeNameMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.MutedMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserNameChangeMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Date;
import java.util.List;


public class TalkMessageEvent implements Event {

    public static boolean isValidColour(int colour, Session client) {
        if ((colour == 23 || colour == 37) && !client.getPlayer().getPermissions().getRank().modTool())
            return false;
        return true;
    }

    public static String filterMessage(String message) {
        if (message.contains("You can type here to talk!")) {
            message = message.replace("You can type here to talk!", "");
        }

        return message.replace((char) 13 + "", "");
    }

    public void verifyMention(String username, Session client){
        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);
        String mentionedBy = client.getPlayer().getData().getUsername();

        if (session == null || mentionedBy.equals(username)) {
            return;
        }

        if(!session.getPlayer().getSettings().allowsMentions()){
            client.getPlayer().sendBubble("/usr/look/" + username, Locale.getOrDefault("mention_notification_error", "%user% has blocked mentions, you can't notice him!").replace("%user%", username));
            return;
        }

        session.getPlayer().sendInteractiveBubble("/usr/look/" + mentionedBy, Locale.getOrDefault("mention_notification", "%user% has just mentioned you, click here to visit him!").replace("%user%", mentionedBy), "navigator/goto/" + client.getPlayer().getEntity().getRoom().getId());
    }

    public void handle(Session client, MessageEvent msg) {
        String message = msg.readString();
        int colour = msg.readInt();

        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        final int timeMutedExpire = client.getPlayer().getData().getTimeMuted() - (int) Comet.getTime();

        PlayerEntity playerEntity = client.getPlayer().getEntity();

        if (playerEntity == null || playerEntity.getRoom() == null || playerEntity.getRoom().getEntities() == null)
            return;

        if (!playerEntity.isVisible() && !playerEntity.getPlayer().isInvisible()) {
            return;
        }

        if (client.getPlayer().getData().getTimeMuted() != 0) {
            if (client.getPlayer().getData().getTimeMuted() > (int) Comet.getTime()) {
                client.getPlayer().getSession().send(new MutedMessageComposer(timeMutedExpire));
                return;
            }
        }

        if (!TalkMessageEvent.isValidColour(colour, client))
            colour = 0;

        if (client.getPlayer().getChatMessageColour() != null) {
            message = "@" + client.getPlayer().getChatMessageColour() + "@" + message;

            if (message.toLowerCase().startsWith("@" + client.getPlayer().getChatMessageColour() + "@:")) {
                message = message.toLowerCase().replace("@" + client.getPlayer().getChatMessageColour() + "@:", ":");
            }
        }

        String filteredMessage = filterMessage(message);

        if (filteredMessage == null) {
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
                Locale.get("spam.notification.message").replace("%date%", String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes()) + ":" + String.valueOf(date.getSeconds())).replace("%roomname%", client.getPlayer().getEntity().getRoom().getData().getName()).replace("%username%", client.getPlayer().getData().getUsername()).replace("%type%", Locale.get("spam.type.talk")),
                 "event:navigator/goto/" + client.getPlayer().getEntity().getRoom().getId()));
                return;

            } else if (filterResult.wasModified()) {
                filteredMessage = filterResult.getMessage();
            }

            filteredMessage = playerEntity.getRoom().getFilter().filter(playerEntity, filteredMessage);
        }

        if (playerEntity.onChat(filteredMessage)) {
            try {
                if (LogManager.ENABLED && !message.replace(" ", "").isEmpty())
                    LogManager.getInstance().getStore().getLogEntryContainer().put(new RoomChatLogEntry(playerEntity.getRoom().getId(), client.getPlayer().getId(), message));
            } catch (Exception ignored) {

            }

            if (client.getPlayer().getEntity().getPrivateChatItemId() != 0) {
                // broadcast message only to players in the tent.
                RoomItemFloor floorItem = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(client.getPlayer().getEntity().getPrivateChatItemId());

                if (floorItem != null) {
                    ((PrivateChatFloorItem) floorItem).broadcastMessage(new TalkMessageComposer(client.getPlayer().getEntity().getId(), filteredMessage, RoomManager.getInstance().getEmotions().getEmotion(filteredMessage), colour));
                    client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UserNameChangeMessageComposer(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getEntity().getId(), client.getPlayer().getData().getUsername()));
                }
            } else {
                client.getPlayer().getEntity().getRoom().getEntities().broadcastChatMessage(new TalkMessageComposer(client.getPlayer().getEntity().getId(), filteredMessage, RoomManager.getInstance().getEmotions().getEmotion(filteredMessage), colour), client.getPlayer().getEntity());
                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UserNameChangeMessageComposer(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getEntity().getId(), client.getPlayer().getData().getUsername()));
            }


            playerEntity.postChat(filteredMessage);

            if(message.contains("@")){
                String[] mentioned = message.split("@");
                String[] clean = mentioned[1].split(" ");
                verifyMention(clean[0], client);
            }
        }
    }
}