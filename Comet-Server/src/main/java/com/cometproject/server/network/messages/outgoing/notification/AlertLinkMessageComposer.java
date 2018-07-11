package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class AlertLinkMessageComposer extends MessageComposer {
    private final String message;
    private final String url;

    public AlertLinkMessageComposer(final String message, final String url) {
        this.message = message;
        this.url = url;
    }


    @Override
    public short getId() {
        return Composers.HotelAlertLinkMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(message);
        msg.writeString(url);
    }
}
