package com.cometproject.server.network.messages.outgoing.user.talents;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.components.AchievementComponent;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class TalentTrackMessageComposer extends MessageComposer {

    private final AchievementComponent achievementComponent;

    public TalentTrackMessageComposer(final AchievementComponent achievementComponent) {
        this.achievementComponent = achievementComponent;
    }

    @Override
    public short getId() {
        return Composers.TalentTrackMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("citizenship");
        msg.writeInt(5);
        msg.writeInt(0);
        msg.writeInt(1); // Passed Quiz  (2/1)
        msg.writeInt(1);
        msg.writeInt(0x7d);
        msg.writeInt(1);
        msg.writeString("ACH_SafetyQuizGraduate1");
        msg.writeInt(checkProgress(AchievementType.ACH_109, 0) == 0 ? 1 : checkProgress(AchievementType.ACH_109, 0)); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(achievementComponent.getProgress(AchievementType.ACH_109) == null ? 0 : achievementComponent.getProgress(AchievementType.ACH_109).getProgress()); // ACH Current Progress
        msg.writeInt(1);
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeString("A1 KUMIANKKA");
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(checkPrestiges(1)); // Flag 1  (1/0)
        msg.writeInt(4);
        msg.writeInt(6);
        msg.writeInt(1);
        msg.writeString("ACH_SurvivalBullets3");
        msg.writeInt(checkProgress(AchievementType.ACH_104, 49)); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(achievementComponent.getProgress(AchievementType.ACH_104).getProgress()); // ACH Current Progress
        msg.writeInt(1);
        msg.writeInt(0x12);
        msg.writeInt(1);
        msg.writeString("ACH_SurvivalChest3");
        msg.writeInt(checkProgress(AchievementType.ACH_105, 49)); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(achievementComponent.getProgress(AchievementType.ACH_105).getProgress()); // ACH Current Progress
        msg.writeInt(2);
        msg.writeInt(0x13);
        msg.writeInt(1);
        msg.writeString("ACH_SurvivalKills3");
        msg.writeInt(checkProgress(AchievementType.ACH_106, 49)); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(achievementComponent.getProgress(AchievementType.ACH_106).getProgress()); // ACH Current Progress
        msg.writeInt(30);
        msg.writeInt(8);
        msg.writeInt(1);
        msg.writeString("ACH_RegistrationDuration3");
        msg.writeInt(checkProgress(AchievementType.ACH_41, 5)); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(achievementComponent.getProgress(AchievementType.ACH_41).getProgress()); // ACH Current Progress
        msg.writeInt(5);
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeString("THRONE");
        msg.writeInt(0); // Flag 2  (1/0)
        msg.writeInt(2);
        msg.writeInt(checkPrestiges(1));
        msg.writeInt(4);
        msg.writeInt(0x91);
        msg.writeInt(1);
        msg.writeString("ACH_GuideAdvertisementReader1");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(11);
        msg.writeInt(1);
        msg.writeString("ACH_RegistrationDuration1");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(0x13);
        msg.writeInt(2);
        msg.writeString("ACH_AllTimeHotelPresence2");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(60);
        msg.writeInt(8);
        msg.writeInt(2);
        msg.writeString("ACH_RoomEntry2");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(20);
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeString("A1 KUMIANKKA");
        msg.writeInt(3); // Flag 3  (1/0)
        msg.writeInt(3);
        msg.writeInt(0);
        msg.writeInt(4);
        msg.writeInt(11);
        msg.writeInt(2);
        msg.writeString("ACH_RegistrationDuration2");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(3);
        msg.writeInt(0x5e);
        msg.writeInt(1);
        msg.writeString("ACH_HabboWayGraduate1");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(0x13);
        msg.writeInt(3);
        msg.writeString("ACH_AllTimeHotelPresence3");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(120);
        msg.writeInt(0x8e);
        msg.writeInt(1);
        msg.writeString("ACH_FriendListSize1");
        msg.writeInt(0); // Has top ACHIEVEMENT? (1/0)
        msg.writeInt(0);
        msg.writeInt(2);
        msg.writeInt(1);
        msg.writeString("TRADE");
        msg.writeInt(1);
        msg.writeString("A1 KUMIANKKA");
        msg.writeInt(2); // Flag 3  (1/0)
        msg.writeInt(4);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeString("CITIZEN");
        msg.writeInt(2);
        msg.writeString("A1 KUMIANKKA");
        msg.writeInt(0);
        msg.writeString("HABBO_CLUB_CITIZENSHIP_VIP_REWARD");
        msg.writeInt(7);
    }

    private int checkProgress (AchievementType achievement, int requirement) {
        AchievementProgress achievementProgress = this.achievementComponent.getProgress(achievement);
        if(achievementProgress != null) {
            if (achievementProgress.getProgress() >= requirement) {
                return 2;
            }

            if (achievementProgress.getProgress() > 1 && achievementProgress.getProgress() < requirement) {
                return 1;
            }
        }

        return 0;
    }

    private int checkPrestiges (int prestige) {
        switch (prestige) {
            case 1:
                if(checkProgress(AchievementType.ACH_104, 10) == 2
                && checkProgress(AchievementType.ACH_105, 10) == 2
                && checkProgress(AchievementType.ACH_106, 10) == 2
                && checkProgress(AchievementType.ACH_41, 10) == 2){
                    return 1;
                } else return 0;

            case 2:
                if(checkProgress(AchievementType.ACH_104, 10) == 2
                && checkProgress(AchievementType.ACH_105, 10) == 2
                && checkProgress(AchievementType.ACH_106, 10) == 2
                && checkProgress(AchievementType.ACH_41, 10) == 2){
                    return 2;
                } else return 1;

        }

        return 0;
    }
}

