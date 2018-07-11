package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertLinkMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HotelAlertLinkCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

        String link = params[1];
        String text = this.merge(params, 2);
        NetworkManager.getInstance().getSessions().broadcast(new AlertLinkMessageComposer(Locale.getOrDefault("command.hotelalertlink.title", "Alert") + "<br><br>" + text + "<br><br>- " + client.getPlayer().getData().getUsername(), link));
    }

    @Override
    public String getPermission() {
        return "hotelalertlink_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.hotelalertlink.description");
    }
}
