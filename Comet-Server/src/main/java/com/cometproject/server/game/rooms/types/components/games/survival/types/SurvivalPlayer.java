package com.cometproject.server.game.rooms.types.components.games.survival.types;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.freeze.UpdateFreezeLivesMessageComposer;

public class SurvivalPlayer {
    private final PlayerEntity entity;

    private SurvivalPowerUp powerUp = SurvivalPowerUp.None;
    private int lives = 100;
    private int bullets = 0;

    public int shield = 0;
    public int speedTime = 0;

    public SurvivalPlayer(PlayerEntity playerEntity) {
        this.entity = playerEntity;
    }

    public void powerUp(SurvivalPowerUp powerUp, SurvivalPowerUp oldPowerUp) {
        boolean needsHold = false;
        switch (powerUp) {
            case Life:
                this.lives += 75;
                this.entity.getPlayer().getSession().send(new WhisperMessageComposer(this.entity.getId(), Locale.getOrDefault("survival.health.recieved", "You've just taken stamina from the chest! You actually have %life% HP.").replace("%life%", this.getLives() + ""), 0));
                this.entity.getPlayer().getSession().send(new UpdateFreezeLivesMessageComposer(this.entity.getId(), this.lives));
                needsHold = true;
            break;

            case Shield:
                if(this.shield < 100) {
                    this.shield = 100;
                } else this.shield += 10;
                this.entity.getPlayer().getSession().send(new WhisperMessageComposer(this.entity.getId(), Locale.getOrDefault("survival.health.recieved", "You've just taken shield power from the chest! You actually have %shield% PWR.").replace("%shield%", this.getShield() + ""), 0));
                needsHold = true;
            break;

            case Sniper:
                this.entity.getPlayer().getSession().send(new WhisperMessageComposer(this.entity.getId(), Locale.getOrDefault("survival.sniper.recieved", "You've just taken a sniper from the chest!").replace("%bullets%", this.getBullets() + ""), 0));
                this.bullets += 10;
                this.entity.applyEffect(new PlayerEffect(587, 0));
            break;

            case Gun:
                this.entity.getPlayer().getSession().send(new WhisperMessageComposer(this.entity.getId(), Locale.getOrDefault("survival.shotgun.recieved", "You've just taken a revolver from the chest!").replace("%bullets%", this.getBullets() + ""), 0));
                this.bullets += 15;
                this.entity.applyEffect(new PlayerEffect(532, 0));
            break;

            case Bullets:
                this.bullets += 10;
                this.entity.getPlayer().getSession().send(new WhisperMessageComposer(this.entity.getId(), Locale.getOrDefault("survival.bullets.recieved", "You've just taken 10 bullets from the chest, you have a total of %bullets% now!").replace("%bullets%", this.getBullets() + ""), 0));
                this.entity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_104, 1);
                needsHold = true;
            break;

            case Speed:
                this.entity.setFastWalkEnabled(true);
                this.setSpeedTime(9);
                needsHold = true;
            break;
        }
        if(needsHold) {
            this.powerUp = oldPowerUp;
        } else this.powerUp = powerUp;
    }

    public PlayerEntity getEntity() {
        return entity;
    }
    public SurvivalPowerUp getPowerUp() {
        return powerUp;
    }
    public int getLives() {
        return lives;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }
    public void decrementLives(int lives) {
        this.lives -= lives;
    }
    public int getBullets() {
        return bullets;
    }
    public int getShield() {
        return shield;
    }
    public void decrementShield(int shield) {
        this.shield -= shield;
    }
    public void resetShield() { this.shield = 0; }
    public void incrementBullets() {
        this.bullets += 1;
    }
    public void setBullets(int bullets) {
        this.bullets = bullets;
    }
    public int getSpeedTime() {
        return speedTime;
    }
    public void setSpeedTime(int survivalTimer) {
        this.speedTime = survivalTimer;
    }
    public void decrementSpeedTime() {
        this.speedTime--;
    }
}
