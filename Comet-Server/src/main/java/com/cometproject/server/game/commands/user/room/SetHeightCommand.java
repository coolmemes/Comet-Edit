package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class SetHeightCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        Player player = client.getPlayer();

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())) {
            sendImageNotif((Locale.get("command.setheight.error_icon")), (Locale.get("command.setheight.error")), client);
            return;
        }

        if (params[0] == null || !NumberUtils.isNumber(params[0])) {
            sendNotif(Locale.get("sh_command_error_2"), client);
            return;
        }

        double height = Double.parseDouble(params[0]);
        if (height > 99 || height < 0) {
            sendNotif(Locale.get("sh_command_error_1"), client);
            return;
        }

        if (height == 0) {
            player.getData().setStackHeight(height);
            sendNotif(Locale.get("sh_command_reset"), client);
            return;
        } else {
            player.getData().setStackHeight(height);
            String msg = Locale.get("sh_command_success");
            msg = msg.replaceFirst("%height%", Double.toString(height));
            sendNotif(msg, client);
            return;
        }
    }

    @Override
    public String getPermission() {
        return "setheight_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.sh.description");
    }
}
