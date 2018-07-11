package com.cometproject.server.network.messages.outgoing.room.competition;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class RoomCompetitionOwnerInfoMessageComposer extends MessageComposer {

    private final Player player;
    private int status = 0;
    private String competition;

    public RoomCompetitionOwnerInfoMessageComposer(Player player, String competition, int status) {
        this.player = player;
        this.status = status;
        this.competition = competition;
    }
    @Override
    public short getId() {
        return Composers.RoomCompetitionOwnerInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(player.getId()); // IDK
        msg.writeString(competition); // competition type string
        msg.writeInt(status); // 0 = Room Request Sent / 1 =  Room Request Offer I / 2 = Room Request Confirm II / 3 = Room Needs Items / 4 = Room Needs to be Open / 5 = Close

        msg.writeInt(3); // LOOP - Required Campaign Items
            msg.writeString("throne");
            msg.writeString("rare_dragonlamp*3");
            msg.writeString("CF_diamond_500");
        msg.writeInt(0); // LOOP - Required Campaign Items
            //msg.writeString("");
    }
}
