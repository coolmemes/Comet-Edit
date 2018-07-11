package com.cometproject.server.game.rooms.competition;

public class RoomCompetition {
    private int roomId;
    private int playerId;
    private int competition;

    public RoomCompetition(int playerId, int roomId, int competition) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.competition = competition;
    }

    public int getRoomId() { return this.roomId; }

    public int getPlayerId() { return this.playerId; }

    public int getCompetition() { return this.competition; }
}
