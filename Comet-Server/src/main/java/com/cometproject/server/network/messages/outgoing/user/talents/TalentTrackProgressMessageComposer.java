package com.cometproject.server.network.messages.outgoing.user.talents;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.components.AchievementComponent;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class TalentTrackProgressMessageComposer extends MessageComposer {

    private final AchievementComponent achievementComponent;

    public TalentTrackProgressMessageComposer(final AchievementComponent achievementComponent) {
        this.achievementComponent = achievementComponent;
    }

    @Override
    public short getId() {
        return Composers.TalentTrackProgressMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("citizenship");
        msg.writeInt(5);
        msg.writeInt(0);
    }
}