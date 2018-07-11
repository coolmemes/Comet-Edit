package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class FilterCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            sendImageNotif(Locale.get("command.filter.icon"), Locale.getOrDefault("command.filter.none", "You need to introduce the type and the word to filter."), client);
            return;
        }

        String type = params[0];
        String word = params[1];

        if (word == null) {
            sendNotif(Locale.getOrDefault("command.filter.null", "You can't introduce a null word!"), client);
            return;
        }

        switch (type){
            case "add":
                RoomManager.getInstance().getFilter().addWord(word);
                sendImageNotif(Locale.get("command.filter.icon"), Locale.get("command.filter.add.success").replace("%word%", word.toUpperCase()), client);
                break;
            case "remove":
                RoomManager.getInstance().getFilter().removeWord(word);
                sendImageNotif(Locale.get("command.filter.icon"), Locale.get("command.filter.remove.success").replace("%word%", word.toUpperCase()), client);
                break;

        }
    }

    @Override
    public String getPermission() {
        return "filter_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.filter.description");
    }
}