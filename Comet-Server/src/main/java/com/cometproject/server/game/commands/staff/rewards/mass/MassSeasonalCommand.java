package com.cometproject.server.game.commands.staff.rewards.mass;

import com.cometproject.server.config.Locale;


public class MassSeasonalCommand extends MassCurrencyCommand {
    @Override
    public String getPermission() {
        return "massseasonal_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.massseasonal.description");
    }
}
