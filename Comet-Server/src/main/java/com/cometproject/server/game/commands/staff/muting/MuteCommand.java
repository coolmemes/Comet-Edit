package com.cometproject.server.game.commands.staff.muting;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class MuteCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        int playerId = PlayerManager.getInstance().getPlayerIdByUsername(params[0]);
        int length = Integer.parseInt(params[1]);

        PlayerData playerData = PlayerManager.getInstance().getDataByPlayerId(playerId);

        if (playerId != -1) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (session != null) {
                session.send(new AdvancedAlertMessageComposer(Locale.get("command.mute.muted").replace("%length%", length + "").replace("%by%", client.getPlayer().getData().getUsername() + "")));
            }

            client.getPlayer().getStats().setBans(client.getPlayer().getStats().getBans() + 1);
            long expire = Comet.getTime() + (length * 60);
            BanManager.getInstance().banPlayer(BanType.MUTE, playerData.getId() + "", length, expire, params.length > 2 ? this.merge(params, 2) : "", client.getPlayer().getId());
        }

    }

    @Override
    public String getPermission() {
        return "mute_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.mute.description");
    }
}
