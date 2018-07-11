package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.HabboEpicPopupViewMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HelpCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            client.send(new HabboEpicPopupViewMessageComposer("habbopages/chat/global.txt"));
            return;
        }

        String type = params[0];

        switch (type) {
            case "wired":
                client.send(new HabboEpicPopupViewMessageComposer("habbopages/chat/wired.txt"));
                break;
            case "enables":
                client.send(new HabboEpicPopupViewMessageComposer("habbopages/chat/enables.txt"));
                break;
            case "handitems":
                client.send(new HabboEpicPopupViewMessageComposer("habbopages/chat/infoshanditem.txt"));
                break;
            case "transf":
                client.send(new HabboEpicPopupViewMessageComposer("habbopages/chat/transform.txt"));
                break;
            default:
                client.send(new HabboEpicPopupViewMessageComposer("habbopages/chat/global.txt"));
                break;
        }
    }

    @Override
    public String getPermission() {
        return "help_command";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.help.description", "help");
    }
}
