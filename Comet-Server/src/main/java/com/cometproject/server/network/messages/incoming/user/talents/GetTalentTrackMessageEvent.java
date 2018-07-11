package com.cometproject.server.network.messages.incoming.user.talents;

        import com.cometproject.server.network.messages.incoming.Event;
        import com.cometproject.server.network.messages.outgoing.user.talents.TalentTrackMessageComposer;
        import com.cometproject.server.protocol.messages.MessageEvent;
        import com.cometproject.server.network.sessions.Session;


public class GetTalentTrackMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client.getPlayer().getEntity() == null) {
            return;
        }

        String type = msg.readString();
        client.send(new TalentTrackMessageComposer(client.getPlayer().getAchievements()));
    }
}
