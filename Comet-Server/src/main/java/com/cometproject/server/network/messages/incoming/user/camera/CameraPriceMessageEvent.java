package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.camera.CameraPriceMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

/**
 * Created by Edu on 30/01/2017.
 */
public class CameraPriceMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) throws InterruptedException {
        client.send(new CameraPriceMessageComposer(0, 0, 250));
    }
}