package com.cometproject.server.game.commands.user.settings;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.ShoutMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ToggleMentionsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getSettings().allowsMentions()) {
            client.getPlayer().getSettings().setAllowMentions(false);
            client.send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("command.togglementions.disabled", "You have disabled mentions."), 33));
        } else {
            client.getPlayer().getSettings().setAllowMentions(true);
            client.send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("command.togglementions.enabled", "You have enabled mentions."), 33));

        }

        PlayerDao.saveMentions(client.getPlayer().getSettings().allowsMentions(), client.getPlayer().getId());
    }

    @Override
    public String getPermission() {
        return "togglementions_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.togglementions.description");
    }
}
