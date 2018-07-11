package com.cometproject.server.game.items.crackable;

import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.achievements.types.AchievementType;

import java.util.ArrayList;
import java.util.List;

public class CrackableItem {
    private int itemId;
    private int max;
    private int effect;
    private AchievementType tickAchievement;
    private AchievementType crackedAchievement;
    private List<Integer> rewardsId = new ArrayList<>();

    public enum CrackableType{
        INTERACTION,
        PINATA,
        CAMPAIGN
    }

    private CrackableType type;

    public CrackableItem(int itemId, CrackableType type, int max, int effect, AchievementType tickAchievement, AchievementType crackedAchievement, String rewards) {
        this.itemId = itemId;
        this.type = type;
        this.max = max;
        this.effect = effect;
        this.tickAchievement = tickAchievement;
        this.crackedAchievement = crackedAchievement;
        if(!rewards.contains(",")) {
            this.rewardsId.add(Integer.valueOf(rewards));
        } else {
            for(String rewardId : rewards.split(",")) { this.rewardsId.add(Integer.valueOf(rewardId)); }
        }
    }

    public int getItemId() { return this.itemId; }
    public int getMaxTouch() { return this.max; }
    public int getEffectNeeded() { return this.effect; }
    public AchievementType getTickAchievement() { return this.tickAchievement; }
    public AchievementType getCrackedAchievement() { return this.crackedAchievement; }
    public CrackableType getCrackableType() { return this.type; }
    public List<Integer> getRewardsId() { return this.rewardsId; }
    public int getRandomReward() { return this.getRewardsId().get((int)Math.floor(Math.random() * this.getRewardsId().size())); }
    public int getCrackableState(int count, int interactions) { return (int)Math.floor((1.0D / ((double)this.max / (double) count) * interactions)); }
}
