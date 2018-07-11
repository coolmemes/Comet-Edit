package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class StaffAlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(client == null || client.getPlayer() == null || client.getPlayer().getData() == null) { return; }

        NetworkManager.getInstance().getSessions().broadcastToModerators(new MotdNotificationMessageComposer(Locale.getOrDefault("command.staffalert.content", "Staff Alert:\r\n%message%\r\n\r\n-" + client.getPlayer().getData().getUsername()).replace("%message%", this.merge(params)).replace("username", client.getPlayer().getData().getUsername())));
    }

    @Override
    public String getPermission() {
        return "staffalert_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.staffalert.description");
    }
}
