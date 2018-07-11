package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class NuxAlertMessageComposer  extends MessageComposer {
    private String msg = "";
    private String type = "";

    public NuxAlertMessageComposer(String type, String msg) {
        this.msg = msg;
        this.type = type;
    }

    @Override
    public short getId() {
        return Composers.NuxAlertMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        switch (type) {
            case "CUSTOM":
                msg.writeString(this.msg);
                break;
            case "WINDOW":
                msg.writeString(this.msg);
                break;
            case "DIAMONDS":
                msg.writeString("helpBubble/add/DIAMONDS_BUTTON/" + this.msg);
                break;
            case "CHAT":
                msg.writeString("helpBubble/add/chat_input/" + this.msg);
                break;
            case "INVENTORY":
                msg.writeString("helpBubble/add/BOTTOM_BAR_INVENTORY/" + this.msg);
                break;
            case "MEMENU":
                msg.writeString("helpBubble/add/MEMENU_PROFILE/" + this.msg);
                break;
            case "NAVIGATOR":
                msg.writeString("helpBubble/add/BOTTOM_BAR_NAVIGATOR/" + this.msg);
                break;
            case "SHOP":
                msg.writeString("helpBubble/add/BOTTOM_BAR_CATALOGUE/" + this.msg);
                break;
            case "STORIES":
                msg.writeString("helpBubble/add/BOTTOM_BAR_STORIES/" + this.msg);
                break;
        }
    }
}