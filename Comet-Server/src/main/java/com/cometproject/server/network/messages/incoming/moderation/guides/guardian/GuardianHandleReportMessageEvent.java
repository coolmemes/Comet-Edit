package com.cometproject.server.network.messages.incoming.moderation.guides.guardian;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.moderation.guides.guardian.GuardianNewReportReceivedComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GuardianHandleReportMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        client.send(new GuardianNewReportReceivedComposer());
    }
}