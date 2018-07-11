package com.cometproject.server.game.achievements.types;

public class Achievement {
    private final int level;
    private final int rewardActivityPoints;
    private final int rewardAchievement;
    private final int progressNeeded;
    private final int gameId;

    public Achievement(int level, int rewardActivityPoints, int rewardAchievement, int progressNeeded, int gameId) {
        this.level = level;
        this.rewardActivityPoints = rewardActivityPoints;
        this.rewardAchievement = rewardAchievement;
        this.progressNeeded = progressNeeded;
        this.gameId = gameId;
    }

    public int getLevel() {
        return level;
    }

    public int getGameId() {
        return gameId;
    }

    public int getRewardActivityPoints() {
        return rewardActivityPoints;
    }

    public int getRewardAchievement() {
        return rewardAchievement;
    }

    public int getProgressNeeded() {
        return progressNeeded;
    }
}
