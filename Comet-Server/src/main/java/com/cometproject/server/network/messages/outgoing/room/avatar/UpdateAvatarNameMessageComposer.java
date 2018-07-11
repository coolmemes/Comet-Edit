package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Administrator on 7/16/2017.
 */
public class UpdateAvatarNameMessageComposer extends MessageComposer {
    private String username;

    public UpdateAvatarNameMessageComposer(String Username) {
        this.username = Username;
    }

    @Override
    public short getId() {
        return Composers.UpdateAvatarNameMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(0);
        msg.writeString(username);
        msg.writeInt(0);
    }
}