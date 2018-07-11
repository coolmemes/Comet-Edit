package com.cometproject.server.network.messages.outgoing.room.competition;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class RoomCompetitionVisitorInfoMessageComposer extends MessageComposer {
    private final Player player;
    private int type = 0;
    private String competition;

    public RoomCompetitionVisitorInfoMessageComposer(Player player, String competition, int type) {
        this.player = player;
        this.type = type;
        this.competition = competition;
    }

    @Override
    public short getId() {
        return Composers.RoomCompetitionVisitorInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(player.getId()); // IDK
        msg.writeString(competition); // competition type string
        msg.writeInt(type); // 0 = Normal // 3 = 0 votes // 4 = Voted
        msg.writeInt(player.getStats().getDailyRoomVotes());

    }
}
