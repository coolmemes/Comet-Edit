package com.cometproject.server.network.messages.incoming.moderation.guides.guardian;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.moderation.guides.guardian.GuardianNewReportReceivedComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GuardianVoteMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
    }
}