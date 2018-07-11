package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

/**
 * Created by Administrator on 7/16/2017.
 */
public class ColorCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        /*String[] mycolors = client.getPlayer().getData().getVipColors().split(";");

        if(mycolors.length == 0) {
            client.getPlayer().sendNotif("Error!", "You dont have colors!");
            return;
        }

        StringBuilder colors = new StringBuilder();
        colors.append("Use :setcolor <ID> for use a name chat color.\n\n");
        for(int i=0; i<mycolors.length; ++i) {
            colors.append("[" + i + "] - " + mycolors[i] + "\n");
        }

        client.getPlayer().sendMotd(colors.toString());
        */
    }

    @Override
    public String getPermission() {
        return "colors_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.colors.description");
    }
}

