package com.cometproject.server.network.messages.outgoing.user.verification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class SMSVerificationOfferMessageComposer extends MessageComposer {

    public SMSVerificationOfferMessageComposer() {
    }

    @Override
    public short getId() {
        return Composers.SMSVerificationOfferMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
    }
}
