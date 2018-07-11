package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.api.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameCycle;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.api.stats.CometStats;
import com.cometproject.server.utilities.CometRuntime;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;


public class AboutCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        StringBuilder about = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();

        CometStats cometStats = Comet.getStats();

        boolean aboutDetailed = client.getPlayer().getPermissions().getRank().aboutDetailed();
        boolean aboutStats = client.getPlayer().getPermissions().getRank().aboutStats();

        if (CometSettings.aboutShowRoomsActive || CometSettings.aboutShowRoomsActive || CometSettings.aboutShowUptime || aboutDetailed) {
            about.append("Propulsé par Comet Server [HBG EDITION] - " + Comet.getBuild()+ "<br><br>");
            if (CometSettings.aboutShowPlayersOnline || aboutDetailed)
                about.append("<b>Joueurs en ligne: </b>" + format.format(cometStats.getPlayers()) + "/" + GameCycle.getInstance().getCurrentOnlineRecord() + "<br>");

            if (CometSettings.aboutShowRoomsActive || aboutDetailed)
                about.append("<b>Apparts actifs: </b>" + format.format(cometStats.getRooms()) + "<br>");
            if (CometSettings.aboutShowUptime || aboutDetailed)
                about.append("<b>En ligne depuis: </b>" + cometStats.getUptime() + "<br>");
        }

        if (aboutStats) {
            about.append("<br><br><b>Hotel Stats</b><br>");
            about.append("Record: " + GameCycle.getInstance().getOnlineRecord() + "<br>");
            about.append("<br><b>Server Info</b><br>");
            about.append("Allocated memory: " + format.format(cometStats.getAllocatedMemory()) + "MB<br>");
            about.append("Used memory: " + format.format(cometStats.getUsedMemory()) + "MB<br>");
            about.append("Process ID: " + CometRuntime.processId + "<br>");
            about.append("OS: " + cometStats.getOperatingSystem() + "<br>");
            about.append("CPU cores:  " + cometStats.getCpuCores() + "<br>");
            about.append("Threads:  " + ManagementFactory.getThreadMXBean().getThreadCount() + "<br>");
        }
        client.send(new AdvancedAlertMessageComposer("Information sur le serveur", about.toString(), "", "", CometSettings.aboutImg));
    }

    @Override
    public String getPermission() {
        return "about_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.about.description");
    }
}