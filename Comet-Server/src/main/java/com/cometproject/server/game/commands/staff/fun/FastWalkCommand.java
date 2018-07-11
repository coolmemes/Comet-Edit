package com.cometproject.server.game.commands.staff.fun;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class FastWalkCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        client.getPlayer().getEntity().toggleFastWalk();

        if(client.getPlayer().getEntity().isFastWalkEnabled()) {
            client.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getId(), Locale.get("command.fastwalk.enabled"), 34));
        } else {
            client.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getId(), Locale.get("command.fastwalk.disabled"), 34));
        }
    }

    @Override
    public String getPermission() {
        return "fastwalk_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.fastwalk.description");
    }
}
