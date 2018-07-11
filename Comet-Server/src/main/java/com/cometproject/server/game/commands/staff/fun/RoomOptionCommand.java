package com.cometproject.server.game.commands.staff.fun;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomActionMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RoomOptionCommand extends ChatCommand {
    public void execute(Session client, String[] params) {
        final PlayerEntity playerEntity = client.getPlayer().getEntity();

        if ((playerEntity != null) && (playerEntity.getRoom() != null)) {
            if (params.length != 1) {
                playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getData().getId(), Locale.getOrDefault("command.roomoption.none", "To change the room option type :roomoption %option% (You're able to use: shake & disco)"), 0));
                return;
            }

            if (params[0].toLowerCase().equals(Locale.getOrDefault("command.roomoption.shake", "shake"))) {
                playerEntity.getPlayer().getSession().send(new RoomActionMessageComposer(1));
                return;
            }

            if (params[0].toLowerCase().equals(Locale.getOrDefault("command.roomoption.disco", "disco"))) {
                playerEntity.getPlayer().getSession().send(new RoomActionMessageComposer(3));
                return;
            }

            if ((params[0].toLowerCase().equals(Locale.getOrDefault("command.roomoption.rotate", "rotate")))) {
                playerEntity.getPlayer().getSession().send(new RoomActionMessageComposer(0));
                return;
            }

            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getData().getId(),(Locale.getOrDefault("command.roomoption.none", "To change the room option type :roomoption %option% (You're able to use: shake & disco)")), 0));
        }
    }

    public String getPermission() {
        return "roomoption_command";
    }

    public String getParameter() {
        return "%option%";
    }

    public String getDescription() {
        return Locale.get("command.roomoption.description");
    }
}
