package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.RandomInteger;


public class SlapCommand extends ChatCommand {
    private final static String[] objects = {
            "con un gran dildo negro",
            "con una gran trucha",
            "con una bota negra",
            "con la mano abierta",
            "con un bugazo tremendo"
    };

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) return;

        String slappedPlayer = params[0];
        String object = objects[RandomInteger.getRandom(0, objects.length - 1)].replace("%g", client.getPlayer().getData().getGender().toLowerCase().equals("m") ? "his" : "her");

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " le dio un golpe a " + slappedPlayer + " " + object + " *", 34));
    }

    @Override
    public String getPermission() {
        return "slap_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.slap.description");
    }

    @Override
    public boolean canDisable() {
        return true;
    }
}
