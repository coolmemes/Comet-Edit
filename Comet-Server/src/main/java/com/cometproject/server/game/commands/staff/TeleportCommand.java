package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class TeleportCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && client.getPlayer().getData().getRank() < 2) {
            sendImageNotif((Locale.get("command.teleport.error_icon")), (Locale.get("command.teleport.error")), client);
            return;
        }

        if (client.getPlayer().getEntity().hasAttribute("teleport")) {
            client.getPlayer().getEntity().removeAttribute("teleport");
            sendNotif(Locale.get("command.teleport.disabled"), client);
        } else {
            client.getPlayer().getEntity().setAttribute("teleport", true);
            sendImageNotif((Locale.get("advice")),(Locale.get("command.teleport.enabled")), client);
        }
    }

    @Override
    public String getPermission() {
        return "teleport_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.teleport.description");
    }
}
