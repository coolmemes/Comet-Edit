package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class HCRequiredNotificationMessageComposer extends MessageComposer {
    private int code;

    public HCRequiredNotificationMessageComposer(int code) {
        this.code = code;
    }

    @Override
    public short getId() {
        return Composers.HCRequiredNotificationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.code);

    }

    /*
    0 -> None.
    1 -> You don't belong to the VIP Club.
    2 -> You don't belong to the VIP Club. (With interactions)
    3 -> You don't belong to the VIP Club. (With interactions)
    4 -> Nametag custom alert.
    5 -> Custom alert.
     */
}
