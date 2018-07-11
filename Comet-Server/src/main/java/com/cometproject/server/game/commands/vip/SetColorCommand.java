package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

/**
 * Created by Administrator on 7/16/2017.
 */
public class SetColorCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        /*if(params.length != 1) {
            client.getPlayer().sendNotif("Error!", "Select a color ID!");
            return;
        }

        String[] colors = client.getPlayer().getData().getVipColors().split(";");

        String colorId = params[0];
        String colorName = colors[Integer.parseInt(colorId)];

        if(!client.getPlayer().getData().haveVipColor(colorName)) {
            client.getPlayer().sendNotif("Error!", "You dont have these color!");
            return;
        }

        client.getPlayer().getData().setVipNameColor(colorName);
        PlayerDao.updateVipNameColor(colorName, client.getPlayer().getId());
        client.send(new SimpleAlertMessageComposer(Locale.getOrDefault("color.selected.succefully", "Name Color changed to [" + colorName + "] succefully")));
    */
    }

    @Override
    public String getPermission() {
        return "setcolor_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.setcolor.description");
    }
}

