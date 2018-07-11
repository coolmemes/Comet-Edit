package com.cometproject.server.network.messages.incoming.moderation.ambassador;

        import com.cometproject.server.config.Locale;
        import com.cometproject.server.game.achievements.types.AchievementType;
        import com.cometproject.server.game.rooms.RoomManager;
        import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
        import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
        import com.cometproject.server.game.rooms.objects.misc.Position;
        import com.cometproject.server.game.rooms.types.Room;
        import com.cometproject.server.network.messages.incoming.Event;
        import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
        import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
        import com.cometproject.server.network.sessions.Session;
        import com.cometproject.server.protocol.messages.MessageEvent;

public class SendAmbassadorAlertMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int playerId = msg.readInt();

        final long currentTimeMs = System.currentTimeMillis();
        final long timeSinceLastUpdate = currentTimeMs - client.getPlayer().getLastActionRequest();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null || timeSinceLastUpdate < 7500) {
            client.getPlayer().sendBubble(Locale.getOrDefault("action.time.error", "action_distance_error"),
                    Locale.getOrDefault("action.time.error.message","You need to wait 7.5 seconds to perform another action!"));
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();

        if(room.hasGameRoom()) // TODO: Read room config for enable/disable this function.
            return;

        PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(playerId);

        Position actorPosition = client.getPlayer().getEntity().getPosition();

        if(actorPosition.distanceTo(playerEntity.getPosition()) > 2) {
            client.getPlayer().sendBubble(Locale.getOrDefault("action.distance.error", "action_distance_error"),
                    Locale.getOrDefault("action.distance.error.message","You need to be closer to perform that action!"));
            return;
        }

        room.getEntities().broadcastMessage(new ActionMessageComposer(client.getPlayer().getEntity().getId(), 2));
        room.getEntities().broadcastMessage(new ActionMessageComposer(playerEntity.getId(), 2));

        room.getEntities().broadcastMessage(new TalkMessageComposer(playerEntity.getId(),
                Locale.getOrDefault("action.kiss.recieved","*Was kissed by %user%*").replace("%user%", client.getPlayer().getEntity().getUsername() + ""),
                RoomManager.getInstance().getEmotions().getEmotion("kiss"), 16));

        client.getPlayer().setLastActionRequest(System.currentTimeMillis());
        client.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_102, 1);
    }
}

