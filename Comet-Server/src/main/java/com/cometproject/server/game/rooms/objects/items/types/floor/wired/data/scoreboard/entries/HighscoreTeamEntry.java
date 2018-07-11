package com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.scoreboard.entries;

import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.scoreboard.types.HighscorePlayer;

import java.util.List;

public class HighscoreTeamEntry {
    private List<HighscorePlayer> users;
    private int teamScore;

    public HighscoreTeamEntry(List<HighscorePlayer> users, int teamScore) {
        this.users = users;
        this.teamScore = teamScore;
    }

    public List<HighscorePlayer> getUsers() {
        return this.users;
    }

    public void setUsers(List<HighscorePlayer> users) {
        this.users = users;
    }

    public int getTeamScore() {
        return this.teamScore;
    }

    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
    }
}
