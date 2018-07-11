package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class HotelAlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        if (message.length == 0) {
            return;
        }

        NetworkManager.getInstance().getSessions().broadcast(new SimpleAlertMessageComposer(this.merge(message) + "\n\n- " + client.getPlayer().getData().getUsername()));
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getPermission() {
        return "hotelalert_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.hotelalert.description");
    }
}
