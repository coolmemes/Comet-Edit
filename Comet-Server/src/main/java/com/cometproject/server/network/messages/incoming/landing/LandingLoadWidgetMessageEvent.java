package com.cometproject.server.network.messages.incoming.landing;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.landing.LandingManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.landing.HallOfFameMessageComposer;
import com.cometproject.server.network.messages.outgoing.landing.HotelViewItemMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class LandingLoadWidgetMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String text = msg.readString();
        final String[] splitText = text.split(",");

        if (text.isEmpty() || splitText.length < 2) {
            client.send(new HotelViewItemMessageComposer("", ""));

            if(CometSettings.hallOfFameEnabled) {
                client.send(new HallOfFameMessageComposer(CometSettings.hallOfFameTextsKey, LandingManager.getInstance().getHallOfFame()));
            }

            return;
        }

        //if (splitText[1].equals("gamesmaker")) return;

        client.send(new HotelViewItemMessageComposer(text, splitText[1]));

        if(CometSettings.hallOfFameEnabled) {
            client.send(new HallOfFameMessageComposer(CometSettings.hallOfFameTextsKey, LandingManager.getInstance().getHallOfFame()));
        }
    }
}
