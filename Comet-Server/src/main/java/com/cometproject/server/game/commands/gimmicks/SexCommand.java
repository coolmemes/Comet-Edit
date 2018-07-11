package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class SexCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.user.invalid", "Invalid username!"), client);
            return;
        }

        String sexedPlayer = params[0];

        RoomEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(sexedPlayer, RoomEntityType.PLAYER);

        if (entity.getUsername() == client.getPlayer().getData().getUsername()) {
            sendNotif(Locale.getOrDefault("command.sex.himself", "You can't sex yourself!"), client);
            return;
        }
        
        if (entity == null) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not in a room."), client);
            return;
        }

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " sexed " + entity.getUsername() + " *", 34));
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new ActionMessageComposer(entity.getId(), 7));
    }

    @Override
    public String getPermission() {
        return "sex_command";
    }
    
    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.sex.description");
    }
}
