package com.cometproject.server.network.messages.incoming.user.verification;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.GroupChatMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.CfhTopicsInitMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.SimpleAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.SMSVerificationCompleteMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class VerifyEmailMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client == null || client.getPlayer() == null || client.getPlayer().getData() == null) {
            return;
        }

        if(client.getPlayer().getData().getRank() < CometSettings.minRankPinCodeRequired)
            return;

        String email = msg.readString();
        if(email.toLowerCase().contains(client.getPlayer().getData().getPinCode().toLowerCase())) {
            client.send(new SimpleAlertMessageComposer(Locale.getOrDefault("pin.code.success", "You have successfuly introduced your pin, enjoy the session.")));
            client.getPlayer().setPinSuccess();
            client.sendQueue(new ModToolMessageComposer());
            client.sendQueue(new SMSVerificationCompleteMessageComposer(2,2));
            return;
        }

        if(client.getPlayer().getPinTries() > 3) {
            for (Session player : ModerationManager.getInstance().getModerators()) {
                if (player == client) continue;
                player.send(new GroupChatMessageComposer(Integer.MAX_VALUE, client.getPlayer().getData().getUsername() + " " + Locale.getOrDefault("pin.code.staffalert", "has failed more than 3 times the Pin Verification."), client.getPlayer().getData().getUsername() + "/" + client.getPlayer().getData().getFigure() + "/" + client.getPlayer().getId()));
            }
            client.disconnect();
            return;
        }

        else {
            client.getPlayer().incrementPinTries();
            client.send(new SimpleAlertMessageComposer(Locale.getOrDefault("pin.code.error", "You have introduced a wrong pin, try again.")));
            client.sendQueue(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }
    }
}
