package com.cometproject.server.utilities.comporators;

import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.scoreboard.entries.HighscoreTeamEntry;

import java.util.Comparator;

public class HighscoreTeamComparator implements Comparator<HighscoreTeamEntry> {
    @Override
    public int compare(HighscoreTeamEntry o1, HighscoreTeamEntry o2) {
        return o1.getTeamScore() < o2.getTeamScore() ? 1 : -1;
    }
}
