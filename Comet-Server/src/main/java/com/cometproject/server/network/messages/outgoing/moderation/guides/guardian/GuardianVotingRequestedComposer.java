package com.cometproject.server.network.messages.outgoing.moderation.guides.guardian;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GuardianVotingRequestedComposer extends MessageComposer {

    public GuardianVotingRequestedComposer() {
    }

    @Override
    public short getId() {
        return Composers.GuardianVotingRequestedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(60);
        msg.writeString("1523288460;1;Antonio es un puto napa de mierda.\r1523288461;2;Antonio es un puto napa de mierda.");
    }
}
