package com.cometproject.server.game.rooms.types.components.games;

public class GamePlayer {
    private final int playerId;
    private int playerScore;

    public GamePlayer(int playerId) {
        this.playerId = playerId;
        this.playerScore = 0;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public void resetScore() {
        this.playerScore = 0;
    }

    public void increaseScore(int amount) {
        this.playerScore += amount;
    }

    public void decreaseScore(int amount) {
        this.playerScore -= amount;
    }

    public void dispose() {
        this.playerScore = 0;
    }
}
