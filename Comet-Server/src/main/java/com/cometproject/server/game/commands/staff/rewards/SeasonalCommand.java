package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.UpdateActivityPointsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;


public class SeasonalCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2 || !StringUtils.isNumeric(params[1]))
            return;

        String username = params[0];
        int amount = Integer.parseInt(params[1]);

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session == null || amount > CometSettings.maxSeasonalRewardPoints) {
            return;
        }

        session.getPlayer().getData().increaseSeasonalPoints(amount);
        session.getPlayer().getData().save();
        session.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_95, 1);
        
        session.send(new NotificationMessageComposer(
                Locale.get("command.seasonal.image"),
                Locale.get("command.seasonal.successmessage").replace("%amount%", String.valueOf(amount))
        ));

        session.send(session.getPlayer().composeCurrenciesBalance());

        client.send(new NotificationMessageComposer(
                Locale.get("command.seasonal.image"),
                Locale.get("command.seasonal.giver.successmessage").replace("%amount%", String.valueOf(amount)).replace("%username%", session.getPlayer().getData().getUsername())));

        if(CometSettings.eventWinnerNotification && client.getPlayer().getData().getUsername() != username){
            NetworkManager.getInstance().getSessions().broadcast(new NotificationMessageComposer("/usr/look/" + username, Locale.getOrDefault("event_winner_notification", "%winner% has just won an event!").replace("%winner%", username), ""));
        }
    }

    @Override
    public String getPermission() {
        return "seasonal_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.seasonal.description");
    }
}
