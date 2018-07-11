package com.cometproject.server.game.commands.staff.fun;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.LinkedList;
import java.util.List;


public class WarpCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(client == null || client.getPlayer() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) { return; }

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && client.getPlayer().getData().getRank() < 2) {
            sendImageNotif((Locale.get("command.warp.error_icon")), (Locale.get("command.warp.error")), client);
            return;
        }

        Position positionToWarp = client.getPlayer().getEntity().getPosition();
        String myUsername = client.getPlayer().getData().getUsername();

        if(params.length > 0) {
            PlayerEntity target = (PlayerEntity) client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(params[0], RoomEntityType.PLAYER);

            if(target == null || target.getUsername().equals(myUsername)) return; //Player not existent in room or its me xd

            target.getPlayer().getSession().send(new WhisperMessageComposer(target.getId(), Locale.getOrDefault("warp.received", "You've just been warped by %username%.").replace("%username%", myUsername), 0));
            target.setWalkCancelled(true);
            target.setPosition(positionToWarp);
            target.markNeedsUpdate();

            return;
        }

        for(PlayerEntity pEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
            if(pEntity.getUsername().equals(myUsername)) continue;

            pEntity.getPlayer().getSession().send(new WhisperMessageComposer(pEntity.getId(), Locale.getOrDefault("warp.received", "You've just been warped by %username%.").replace("%username%", myUsername), 0));
            pEntity.setWalkCancelled(true);
            pEntity.setPosition(positionToWarp);
            pEntity.markNeedsUpdate();
        }
    }

    @Override
    public String getPermission() {
        return "warp_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.warp.description");
    }
}
