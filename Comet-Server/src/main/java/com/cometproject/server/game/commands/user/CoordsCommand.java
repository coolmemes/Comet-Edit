package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameCycle;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.CometStats;

import java.text.NumberFormat;

/**
 * Created by Salinas on 08/02/2018.
 */
public class CoordsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(client == null || client.getPlayer() == null) return;

        final RoomTile tile = client.getPlayer().getEntity().getRoom().getMapping().getTile(client.getPlayer().getEntity().getPosition());

        client.send(new WhisperMessageComposer(client.getPlayer().getData().getId(), Locale.get("command.coords.content").replace("%x%", client.getPlayer().getEntity().getPosition().getX() + "")
                                                                                        .replace("%y%", client.getPlayer().getEntity().getPosition().getY() + "")
                                                                                        .replace("%z%", client.getPlayer().getEntity().getPosition().getZ() + "")
                                                                                        .replace("%tile%", tile.getEntities().size() + ""), 34));

    }

    @Override
    public String getPermission() {
        return "coords_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.coords.description");
    }
}
