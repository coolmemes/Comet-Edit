package com.cometproject.server.network.messages.outgoing.room.access;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.components.types.RoomBan;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

/**
 * Created by Salinas on 06/02/2018.
 */
public class GetGuestRoomResultMessageComposer  extends MessageComposer {
    private final int roomId;

    public GetGuestRoomResultMessageComposer(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public short getId() {
        return Composers.GetGuestRoomResultMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
    }
}
