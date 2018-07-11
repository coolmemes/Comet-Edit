package com.cometproject.server.network.messages.outgoing.catalog.recycler;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.recycler.FurniMaticLevel;
import com.cometproject.server.game.catalog.recycler.FurniMaticReward;
import com.cometproject.server.game.catalog.recycler.RecycleManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.ArrayList;
import java.util.List;


public class RecyclerRewardsMessageComposer extends MessageComposer {
    private List<FurniMaticReward> rewards;
    private List<FurniMaticReward> levelRewards;
    private List<FurniMaticLevel> levels;

    public RecyclerRewardsMessageComposer(List<FurniMaticReward> rewards, List<FurniMaticLevel> levels) {
        this.rewards = rewards;
        this.levels = levels;
        this.levelRewards = new ArrayList<FurniMaticReward>();
    }

    @Override
    public short getId() {
        return Composers.RecyclerRewardsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

        msg.writeInt(levels.size());
        for (FurniMaticLevel level : this.levels)
        {
            msg.writeInt(level.getLevelId());
            msg.writeInt(level.getChance());

            for(FurniMaticReward reward : this.rewards) {
                if(reward.getLevel() != level.getLevelId()) { continue; }
                this.levelRewards.add(reward);
            }

            msg.writeInt(this.levelRewards.size());
            for(FurniMaticReward reward : this.levelRewards) {
                msg.writeString(reward.getDefinition().getItemName());
                msg.writeInt(reward.getDisplayId());
                msg.writeString(reward.getDefinition().getType().toLowerCase());
                msg.writeInt(reward.getDefinition().getSpriteId());
            }

            this.levelRewards.clear();
        }
    }
}
