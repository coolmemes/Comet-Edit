package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class BullyReportedMessageMessageComposer extends MessageComposer {
    private int status;

    public BullyReportedMessageMessageComposer(int status) {
        this.status = status;
    }

    @Override
    public short getId() {
        return Composers.BullyReportedMessageMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(status);
    }

    /*
    0 = Bully report has been sent properly.
    1 = You've lost the ability to report bully users.
    2 = The user that you've reported has not spoken.
    3 = The user that you've reported is already reported.
    4 = Do not abuse the bully report system.
     */
}
