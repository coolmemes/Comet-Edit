package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.api.game.rooms.settings.RoomMuteState;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class MutePlayerMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int playerId = msg.readInt();
        int unk = msg.readInt();
        int lengthMinutes = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();
        PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(playerId);

        if(client.getPlayer().getPerformance() != "" && !room.hasGameRoom()) {

            Position actorPosition = client.getPlayer().getEntity().getPosition();

            if(actorPosition.distanceTo(playerEntity.getPosition()) > 2) {
                client.getPlayer().sendBubble(Locale.getOrDefault("action.distance.error", "action_distance_error"),
                        Locale.getOrDefault("action.distance.error.message","You need to be closer to perform that action!"));
                return;
            }

            final long currentTimeMs = System.currentTimeMillis();
            final long timeSinceLastUpdate = currentTimeMs - client.getPlayer().getLastActionRequest();

            if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null || timeSinceLastUpdate < 7500) {
                client.getPlayer().sendBubble(Locale.getOrDefault("action.time.error", "action_distance_error"),
                        Locale.getOrDefault("action.time.error.message","You need to wait 7.5 seconds to perform another action!"));
                return;
            }

            switch (client.getPlayer().getPerformance()) {
                case "kill":
                    playerEntity.applyEffect(new PlayerEffect(53, 5));
                    room.getEntities().broadcastMessage(new TalkMessageComposer(playerEntity.getId(),
                    Locale.getOrDefault("action.kill.recieved","*Was killed by %user%*").replace("%user%", client.getPlayer().getEntity().getUsername() + ""),
                            RoomManager.getInstance().getEmotions().getEmotion("kiss"), 10));
                    playerEntity.addStatus(RoomEntityStatus.LAY, "0.5");
                    playerEntity.markNeedsUpdate();
                    break;
                case "sex":
                    playerEntity.addStatus(RoomEntityStatus.LAY, "0.5");
                    playerEntity.markNeedsUpdate();

                    client.getPlayer().getEntity().applyEffect(new PlayerEffect(168,3));
                    playerEntity.applyEffect(new PlayerEffect(168,3));

                    client.getPlayer().getEntity().addStatus(RoomEntityStatus.LAY, "0.5");
                    client.getPlayer().getEntity().markNeedsUpdate();
                    break;
            }

            client.getPlayer().setPerformance("");
            client.getPlayer().setLastActionRequest(System.currentTimeMillis());
            return;
        }

        if (playerEntity.getPlayer().getData().getRank() > client.getPlayer().getData().getRank() || client.getPlayer().getId() != room.getData().getOwnerId() && room.getData().getMuteState() != RoomMuteState.RIGHTS && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            return;
        }

        if (playerId == room.getData().getOwnerId()) {
            return;
        }

        room.getRights().addMute(playerId, lengthMinutes);
    }
}
