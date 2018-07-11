package com.cometproject.server.network.messages.outgoing.moderation.guides.guardian;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GuardianNewReportReceivedComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.GuardianNewReportReceivedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(60);
    }
}
