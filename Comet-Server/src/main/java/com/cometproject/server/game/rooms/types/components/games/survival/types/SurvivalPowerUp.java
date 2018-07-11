package com.cometproject.server.game.rooms.types.components.games.survival.types;
import com.cometproject.server.utilities.RandomUtil;

public enum SurvivalPowerUp {
    None,
    Speed,
    Sniper,
    Gun,
    Bullets,
    Life,
    Shield;

    public static SurvivalPowerUp getRandom() {
        SurvivalPowerUp[] powerUps = SurvivalPowerUp.values();

        return powerUps[RandomUtil.getRandomInt(0, powerUps.length - 1)];
    }
}
