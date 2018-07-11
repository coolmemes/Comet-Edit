package com.cometproject.server.network.messages.outgoing.user.verification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class SMSVerificationCompleteMessageComposer extends MessageComposer {
    private int unknown1;
    private int unknown2;

    public SMSVerificationCompleteMessageComposer(int unknown1, int unknown2) {
        this.unknown1 = unknown1;
        this.unknown2 = unknown2;

    }

    @Override
    public short getId() {
        return Composers.SMSVerificationCompleteMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.unknown1); // idk
        msg.writeInt(this.unknown2); // idk
    }
}

