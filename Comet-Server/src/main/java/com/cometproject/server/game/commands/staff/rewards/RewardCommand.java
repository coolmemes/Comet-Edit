package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.nuxs.NuxGiftSelectionViewMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RewardCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1)
            return;

        final String username = params[0];

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session != null) {
            session.send(new NuxGiftSelectionViewMessageComposer(2));
        }
    }

    @Override
    public String getPermission() {
        return "givereward_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.givereward.description");
    }
}
