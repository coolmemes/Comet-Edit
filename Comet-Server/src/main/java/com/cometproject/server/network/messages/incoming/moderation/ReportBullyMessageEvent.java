package com.cometproject.server.network.messages.incoming.moderation;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.moderation.BullyReportRequestMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class ReportBullyMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {

        client.send(new BullyReportRequestMessageComposer(0));
    }
}