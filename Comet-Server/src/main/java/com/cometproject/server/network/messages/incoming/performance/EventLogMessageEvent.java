package com.cometproject.server.network.messages.incoming.performance;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class EventLogMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {

        String unk1 = msg.readString();
        String unk2 = msg.readString();
        String unk3 = msg.readString();
        String unk4 = msg.readString();
        int unk5 = msg.readInt();

        if(unk3.contains("RWUAM_AMBASSADOR_MUTE_60MIN")) {
            client.getPlayer().setPerformance("sex");
        }

        if(unk3.contains("RWUAM_AMBASSADOR_MUTE_18HOUR")) {
            client.getPlayer().setPerformance("kill");
        }
    }
}
