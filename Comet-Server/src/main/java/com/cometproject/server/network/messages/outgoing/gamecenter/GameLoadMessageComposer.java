package com.cometproject.server.network.messages.outgoing.gamecenter;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GameLoadMessageComposer extends MessageComposer {

    private final int gameId;

    private final String swfLocation;
    private final String accessToken;
    private final String gameServerHost;
    private final String gameServerPort;
    private final String socketPolicyPort;
    private final String assetsUrl;

    public GameLoadMessageComposer(int gameId, String swfLocation, String accessToken, String gameServerHost, String gameServerPort, String socketPolicyPort, String assetsUrl) {
        this.gameId = gameId;
        this.swfLocation = swfLocation;
        this.accessToken = accessToken;
        this.gameServerHost = gameServerHost;
        this.gameServerPort = gameServerPort;
        this.socketPolicyPort = socketPolicyPort;
        this.assetsUrl = assetsUrl;
    }

    @Override
    public short getId() {
        return Composers.GameLoadMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.gameId);
        msg.writeString(Long.toString(System.currentTimeMillis()));
        msg.writeString(this.swfLocation);
        msg.writeString("best");
        msg.writeString("showAll");
        msg.writeInt(60);
        msg.writeInt(10);
        msg.writeInt(0);
        msg.writeInt(6);

        msg.writeString("habboHost");
        msg.writeString("hhus");
        msg.writeString("accessToken");
        msg.writeString(this.accessToken);
        msg.writeString("gameServerHost");
        msg.writeString(this.gameServerHost);
        msg.writeString("gameServerPort");
        msg.writeString(this.gameServerPort);
        msg.writeString("socketPolicyPort");
        msg.writeString(this.socketPolicyPort);
        msg.writeString("assetUrl");
        msg.writeString(this.assetsUrl);
    }
}
