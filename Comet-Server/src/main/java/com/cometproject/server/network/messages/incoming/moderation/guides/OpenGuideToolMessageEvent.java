package com.cometproject.server.network.messages.incoming.moderation.guides;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.moderation.guides.GuideToolsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.verification.EmailVerificationWindowMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class OpenGuideToolMessageEvent implements Event {
    public boolean fromLanding = false;

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client.getPlayer().getData().getRank() >= CometSettings.minRankPinCodeRequired && !client.getPlayer().pinSuccess()) {
            client.getPlayer().sendBubble("pincode", Locale.getOrDefault("pin.code.required", "You need to verify your Pin code before making any action."));
            client.send(new EmailVerificationWindowMessageComposer(1,1));
            return;
        }

        final boolean onDuty = msg.readBoolean();

        if(client.getPlayer().getEntity() == null) {
            fromLanding = true;
            return;
        }

        final boolean handleTourRequests = msg.readBoolean();
        final boolean handleHelpRequests = msg.readBoolean();
        final boolean handleBullyRequests = msg.readBoolean();

        if(handleTourRequests && !fromLanding) {
            boolean isVisible = false;
            if (!client.getPlayer().getEntity().isVisible()) {
                isVisible = true;
            }
            client.getPlayer().setInvisible(isVisible);
            client.getPlayer().getEntity().updateVisibility(isVisible);
            client.send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), Locale.get("command.invisible." + (isVisible ? "disabled" : "enabled")),0));
        }

        else if (fromLanding){
            client.getPlayer().setInvisible(false);
            client.getPlayer().getEntity().updateVisibility(true);
        }

        client.send(new GuideToolsMessageComposer(onDuty, 0,0));
    }
}
