package com.cometproject.server.network.messages.outgoing.user.camera;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

/**
 * Created by Salinas on 12/02/2018.
 */
public class CameraPublishedMessageComposer extends MessageComposer {

    private static String imageHash;

    public CameraPublishedMessageComposer(String imageHash) {
        this.imageHash = imageHash;
    }

    @Override
    public short getId() {
        return Composers.CameraPublishedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.imageHash);
    }
}
