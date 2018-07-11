package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class HabboEpicPopupViewMessageComposer extends MessageComposer {
    private String image;

    public HabboEpicPopupViewMessageComposer(String image) {
        this.image = image;
    }

    @Override
    public short getId() {
        return Composers.HabboEpicPopupViewMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("${image.library.url}" + image + ".png");;

    }
}
