package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Administrator on 7/16/2017.
 */
public class AvatarChangeNameMessageComposer extends MessageComposer {
    private int roomid;
    private int userid;
    private String username;

    public AvatarChangeNameMessageComposer(int RoomId, int UserId, String Username) {
        this.roomid = RoomId;
        this.userid = UserId;
        this.username = Username;
    }

    @Override
    public short getId() {
        return Composers.AvatarChangeNameMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomid);
        msg.writeInt(userid);
        msg.writeString(username);
    }
}